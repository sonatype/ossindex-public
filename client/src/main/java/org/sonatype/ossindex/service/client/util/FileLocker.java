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
package org.sonatype.ossindex.service.client.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * File locking helper.
 *
 * @since 1.1.0
 */
public class FileLocker
{
  private static final Logger log = LoggerFactory.getLogger(FileLocker.class);

  private static final String READ_MODE = "r";

  private static final String WRITE_MODE = "rws";

  /**
   * Function to access file while under lock.
   */
  public interface FileFunction<T>
  {
    T apply(RandomAccessFile file) throws IOException;
  }

  /**
   * Apply function to given path.
   *
   * Path must already exist.
   */
  public static <T> T lock(final Path path, final boolean shared, final FileFunction<T> function) throws IOException {
    checkNotNull(path);
    checkNotNull(function);

    String mode = shared ? READ_MODE : WRITE_MODE;
    log.trace("Locking: {}; mode: {}", path, mode);
    try (RandomAccessFile file = new RandomAccessFile(path.toFile(), mode)) {
      // lock entire file
      FileLock lock = file.getChannel().lock(0L, Long.MAX_VALUE, shared);
      try {
        log.trace("Locked: {}", path);
        return function.apply(file);
      }
      finally {
        lock.release();
        log.trace("Unlocked: {}", path);
      }
    }
  }

  /**
   * Apply function to given path with (shared) read-lock.
   */
  public static <T> T readLock(final Path path, final FileFunction<T> function) throws IOException {
    return lock(path, true, function);
  }

  /**
   * Apply function to given path with (exclusive) write-lock.
   */
  public static <T> T writeLock(final Path path, final FileFunction<T> function) throws IOException {
    return lock(path, false, function);
  }
}
