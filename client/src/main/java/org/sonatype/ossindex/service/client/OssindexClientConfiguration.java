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
package org.sonatype.ossindex.service.client;

import java.net.URI;

import javax.annotation.Nullable;

import org.sonatype.ossindex.service.client.cache.CacheConfiguration;
import org.sonatype.ossindex.service.client.transport.AuthConfiguration;
import org.sonatype.ossindex.service.client.transport.ProxyConfiguration;
import org.sonatype.ossindex.service.client.transport.TimeoutConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link OssindexClient} configuration.
 *
 * @since 1.0.0
 */
public class OssindexClientConfiguration
{
  /**
   * Default base URL for OSS Index service.
   */
  public static final URI DEFAULT_BASE_URL = URI.create("https://ossindex.sonatype.org/");

  /**
   * Default batch-size.
   */
  public static final int DEFAULT_BATCH_SIZE = 128;

  @JsonProperty
  private URI baseUrl = DEFAULT_BASE_URL;

  @JsonProperty
  private int batchSize = DEFAULT_BATCH_SIZE;

  @Nullable
  @JsonProperty("auth")
  private AuthConfiguration authConfiguration;

  @Nullable
  @JsonProperty("proxy")
  private ProxyConfiguration proxyConfiguration;

  @Nullable
  @JsonProperty("cache")
  private CacheConfiguration cacheConfiguration;

  @Nullable
  @JsonProperty("timeout")
  private TimeoutConfiguration timeoutConfiguration;

  /**
   * Returns the base URL for the OSS Index service.
   */
  public URI getBaseUrl() {
    return baseUrl;
  }

  /**
   * Set the base URL for the OSS Index service.
   */
  public void setBaseUrl(final URI baseUrl) {
    this.baseUrl = checkNotNull(baseUrl);
  }

  /**
   * @see #setBaseUrl(URI)
   */
  public void setBaseUrl(final String baseUrl) {
    checkNotNull(baseUrl);
    setBaseUrl(URI.create(baseUrl));
  }

  /**
   * Returns the batch-size.
   */
  public int getBatchSize() {
    return batchSize;
  }

  /**
   * Set the batch-size.
   */
  public void setBatchSize(final int batchSize) {
    this.batchSize = batchSize;
  }

  /**
   * Returns the authentication configuration; or {@literal null} if not configured.
   */
  @Nullable
  public AuthConfiguration getAuthConfiguration() {
    return authConfiguration;
  }

  /**
   * Set the authentication configuration; or {@literal null} to disable.
   */
  public void setAuthConfiguration(@Nullable final AuthConfiguration authConfiguration) {
    this.authConfiguration = authConfiguration;
  }

  /**
   * Returns the proxy configuration; or {@literal null} if not configured.
   */
  @Nullable
  public ProxyConfiguration getProxyConfiguration() {
    return proxyConfiguration;
  }

  /**
   * Set the proxy configuration; or {@literal null} to disable.
   */
  public void setProxyConfiguration(@Nullable final ProxyConfiguration proxyConfiguration) {
    this.proxyConfiguration = proxyConfiguration;
  }

  /**
   * Returns the cache configuration; or {@literal null} if not configured.
   */
  @Nullable
  public CacheConfiguration getCacheConfiguration() {
    return cacheConfiguration;
  }

  /**
   * Set the cache configuration; or {@literal null} for default.
   */
  public void setCacheConfiguration(@Nullable final CacheConfiguration cacheConfiguration) {
    this.cacheConfiguration = cacheConfiguration;
  }

  /**
   * Returns the timeout configuration; or {@literal null} if not configured.
   */
  @Nullable
  public TimeoutConfiguration getTimeoutConfiguration() {
    return timeoutConfiguration;
  }

  /**
   * Set the timout configuration; or {@literal null} for default.
   */
  public void setTimeoutConfiguration(@Nullable final TimeoutConfiguration timeoutConfiguration) {
    this.timeoutConfiguration = timeoutConfiguration;
  }
}
