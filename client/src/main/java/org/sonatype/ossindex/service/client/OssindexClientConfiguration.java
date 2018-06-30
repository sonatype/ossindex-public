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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;

/**
 * {@link OssindexClient} configuration.
 *
 * @since ???
 */
public class OssindexClientConfiguration
{
  public static final URI DEFAULT_BASE_URL = URI.create("https://ossindex.sonatype.org/");

  public static final int DEFAULT_BATCH_SIZE = 64;

  public static final CacheBuilderSpec DEFAULT_CACHE = CacheBuilderSpec.parse(
      "maximumSize=256,expireAfterAccess=2m,softValues"
  );

  @JsonProperty
  private URI baseUrl = DEFAULT_BASE_URL;

  @JsonProperty
  private int batchSize = DEFAULT_BATCH_SIZE;

  @JsonProperty
  private CacheBuilderSpec reportCache = DEFAULT_CACHE;

  @Nullable
  @JsonProperty("auth")
  private AuthConfiguration authConfiguration;

  @Nullable
  @JsonProperty("proxy")
  private ProxyConfiguration proxyConfiguration;

  public URI getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(URI baseUrl) {
    this.baseUrl = baseUrl;
  }

  public void setBaseUrl(final String baseUrl) {
    setBaseUrl(URI.create(baseUrl));
  }

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public CacheBuilderSpec getReportCache() {
    return reportCache;
  }

  public void setReportCache(CacheBuilderSpec reportCache) {
    this.reportCache = reportCache;
  }

  @Nullable
  public AuthConfiguration getAuthConfiguration() {
    return authConfiguration;
  }

  public void setAuthConfiguration(@Nullable final AuthConfiguration authConfiguration) {
    this.authConfiguration = authConfiguration;
  }

  @Nullable
  public ProxyConfiguration getProxyConfiguration() {
    return proxyConfiguration;
  }

  public void setProxyConfiguration(@Nullable final ProxyConfiguration proxyConfiguration) {
    this.proxyConfiguration = proxyConfiguration;
  }
}
