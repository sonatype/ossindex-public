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

import java.util.Map;

import javax.annotation.Nullable;

import org.sonatype.goodies.packageurl.PackageUrl;
import org.sonatype.ossindex.service.api.componentreport.ComponentReport;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * In-memory {@link Cache}.
 *
 * Based on Guava {@link com.google.common.cache.Cache}.
 *
 * @since ???
 */
public class MemoryCache
    implements Cache
{
  private final CacheBuilderSpec spec;

  private final com.google.common.cache.Cache<PackageUrl,ComponentReport> storage;

  private boolean closed;

  public MemoryCache(final Configuration config) {
    checkNotNull(config);
    this.spec = checkNotNull(config.getSpec(), "Missing required cache spec");
    this.storage = CacheBuilder.from(spec).build();
  }

  private void ensureNotClosed() {
    checkState(!closed, "Closed");
  }

  @Override
  public void close() throws Exception {
    closed = true;
    storage.cleanUp();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("spec", spec)
        .toString();
  }

  @Nullable
  @Override
  public ComponentReport getIfPresent(final PackageUrl coordinates) {
    checkNotNull(coordinates);
    ensureNotClosed();
    return storage.getIfPresent(coordinates);
  }

  @Override
  public void putAll(final Map<PackageUrl, ComponentReport> reports) {
    checkNotNull(reports);
    ensureNotClosed();
    storage.putAll(reports);
  }

  //
  // Configuration
  //

  /**
   * {@link MemoryCache} configuration.
   */
  public static class Configuration
    implements CacheConfiguration
  {
    /**
     * Default cache configuration.
     */
    public static final CacheBuilderSpec DEFAULT_SPEC = CacheBuilderSpec.parse(
        "maximumSize=256,expireAfterAccess=2m,softValues"
    );

    private CacheBuilderSpec spec = DEFAULT_SPEC;

    public CacheBuilderSpec getSpec() {
      return spec;
    }

    public void setSpec(final CacheBuilderSpec spec) {
      this.spec = spec;
    }

    @Override
    public Cache create() throws Exception {
      return new MemoryCache(this);
    }
  }
}
