/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.ossindex.service.client.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import javax.annotation.Nullable;

import org.sonatype.goodies.packageurl.PackageUrl;
import org.sonatype.ossindex.service.api.componentreport.ComponentReport;
import org.sonatype.ossindex.service.client.marshal.GsonMarshaller;
import org.sonatype.ossindex.service.client.marshal.Marshaller;
import org.sonatype.ossindex.service.client.util.FileLocker;
import org.sonatype.ossindex.service.client.util.FileLocker.FileFunction;
import org.sonatype.ossindex.service.client.util.UserDataLocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.hash.Hashing;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Directory {@link Cache}.
 *
 * Persistent report cache, storing cache entries into a segmented directory structure.
 * Expiration is done via file last-modified timestamp.
 *
 * @since 1.1.0
 */
public class DirectoryCache
    implements Cache
{
  private static final Logger log = LoggerFactory.getLogger(DirectoryCache.class);

  private final Marshaller marshaller;

  private final Path baseDir;

  private final Duration expireAfter;

  private volatile boolean closed;

  public DirectoryCache(final Marshaller marshaller, final Configuration config) throws IOException {
    checkNotNull(config);
    this.marshaller = checkNotNull(marshaller);

    this.baseDir = checkNotNull(config.getBaseDir(), "Missing required base-directory");
    this.expireAfter = checkNotNull(config.getExpireAfter(), "Missing required expiration duration");

    if (!Files.exists(baseDir)) {
      Files.createDirectories(baseDir);
    }
    else {
      checkState(Files.isDirectory(baseDir), "Not a directory: %s", baseDir);
    }

    log.debug("Marshaller: {}", marshaller);
    log.debug("Base-directory: {}", baseDir);
    log.debug("Expire after: {}", expireAfter);
  }

  private void ensureNotClosed() {
    checkState(!closed, "Closed");
  }

  @Override
  public synchronized void close() throws Exception {
    closed = true;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("baseDir", baseDir)
        .add("expireAfter", expireAfter)
        .toString();
  }

  @Nullable
  @Override
  public synchronized ComponentReport getIfPresent(final PackageUrl coordinates) {
    checkNotNull(coordinates);
    ensureNotClosed();

    Path file = entryFile(coordinates);
    if (Files.exists(file)) {
      try {
        return loadEntry(file);
      }
      catch (IOException e) {
        log.warn("Failed to load entry: {}", file, e);
      }
    }

    return null;
  }

  @Override
  public synchronized void putAll(final Map<PackageUrl, ComponentReport> reports) {
    checkNotNull(reports);
    ensureNotClosed();

    for (Map.Entry<PackageUrl, ComponentReport> entry : reports.entrySet()) {
      Path file = entryFile(entry.getKey());
      try {
        storeEntry(entry.getValue(), file);
      }
      catch (IOException e) {
        log.warn("Failed to store entry: {}", file, e);
      }
    }
  }

  /**
   * Generate an cache entry key for given coordinates.
   */
  private String entryKey(final PackageUrl coordinates) {
    return Hashing.sha1()
        .hashUnencodedChars(coordinates.toString())
        .toString();
  }

  /**
   * Returns cache entry file reference.
   */
  private Path entryFile(final PackageUrl coordinates) {
    String key = entryKey(coordinates);
    Path path = Paths.get(key.substring(0, 2), key.substring(2, 4), key);
    return baseDir.resolve(path);
  }

  //
  // Loading
  //

  /**
   * Load entry from given file.  If entry has expired returns {@literal null}.
   */
  @Nullable
  private ComponentReport loadEntry(final Path file) throws IOException {
    log.trace("Loading entry: {}", file);

    // file is assumed to exist at this point
    return FileLocker.readLock(file, new FileFunction<ComponentReport>()
    {
      @Override
      public ComponentReport apply(final RandomAccessFile raf) throws IOException {
        ComponentReport report = null;
        boolean delete = false;

        if (isEntryStale(file)) {
          log.trace("Expiring entry: {}", file);
          delete = true;
        }
        else {
          try {
            // RAF close will clean up resources
            Reader reader = new BufferedReader(Channels.newReader(raf.getChannel(), Charsets.UTF_8.name()));
            report = marshaller.unmarshal(reader, ComponentReport.class);
          }
          catch (IOException e) {
            // delete if we are unable to marshal
            log.warn("Corrupt entry: {}", file, e);
            delete = true;
          }
        }

        if (delete) {
          // close file handle to allow deletion
          raf.close();

          // sanity check; file should exist but do not fail if missing
          Files.deleteIfExists(file);
        }

        return report;
      }
    });
  }

  /**
   * Check if given entry is stale and should be expired.
   */
  private boolean isEntryStale(final Path file) throws IOException {
    BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
    FileTime lastModified = attributes.lastModifiedTime();
    log.trace("Last-modified: {}", lastModified);

    long age = System.currentTimeMillis() - lastModified.toMillis();
    log.trace("Age: {} ms", age);

    return age > expireAfter.getMillis();
  }

  //
  // Storing
  //

  /**
   * Store entry to given file.
   */
  private void storeEntry(final ComponentReport report, final Path file) throws IOException {
    log.trace("Storing entry: {} -> {}", report, file);

    // prepare directory structure
    Files.createDirectories(file.getParent());

    // maybe create file; must exist for locking
    try {
      Files.createFile(file);
    }
    catch (FileAlreadyExistsException e) {
      // ignore
      log.trace("File already exists: {}", file, e);
    }

    FileLocker.writeLock(file, new FileFunction<Void>()
    {
      @Override
      public Void apply(final RandomAccessFile raf) throws IOException {
        try {
          // RAF close will clean up resources
          Writer writer = new BufferedWriter(Channels.newWriter(raf.getChannel(), Charsets.UTF_8.name()));
          marshaller.marshal(report, writer);
          writer.flush();
        }
        catch (IOException e) {
          log.warn("Failed to store entry: {}", file, e);

          // close file handle to allow deletion
          raf.close();

          // sanity check; file should exist but do not fail if missing
          Files.deleteIfExists(file);
        }
        return null;
      }
    });
  }

  /**
   * Purge entry for coordinates.
   *
   * @since 1.3.0
   */
  @VisibleForTesting
  void purgeEntry(final PackageUrl coordinates) throws IOException {
    Path file = entryFile(coordinates);
    log.trace("Purge entry: {}", file);
    Files.deleteIfExists(file);
  }

  //
  // Configuration
  //

  /**
   * {@link DirectoryCache} configuration.
   */
  public static class Configuration
      implements CacheConfiguration
  {
    /**
     * Default base-directory.
     */
    public static final Path DEFAULT_BASE_DIR = new UserDataLocation("Sonatype", "Ossindex").get()
        .resolve("report-cache");

    /**
     * Default expiration duration.
     */
    public static final Duration DEFAULT_EXPIRE_AFTER = Duration.standardHours(12);

    @JsonProperty
    private Path baseDir = DEFAULT_BASE_DIR;

    @JsonProperty
    private Duration expireAfter = DEFAULT_EXPIRE_AFTER;

    public Path getBaseDir() {
      return baseDir;
    }

    public void setBaseDir(final Path baseDir) {
      this.baseDir = baseDir;
    }

    public Duration getExpireAfter() {
      return expireAfter;
    }

    public void setExpireAfter(final Duration expireAfter) {
      this.expireAfter = expireAfter;
    }

    @Override
    public Cache create() throws Exception {
      return new DirectoryCache(new GsonMarshaller(), this);
    }
  }
}
