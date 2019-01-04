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
package org.sonatype.ossindex.service.client.transport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Proxy configuration.
 *
 * @since 1.0.0
 */
public class ProxyConfiguration
{
  public static final String HTTP = "http";

  public static final String HTTPS = "https";

  @Nonnull
  @JsonProperty
  private String protocol = HTTP;

  @Nonnull
  @JsonProperty
  private String host;

  @Nonnull
  @JsonProperty
  private int port = 8080;

  @Nullable
  @JsonProperty
  private String nonProxyHosts;

  @Nullable
  @JsonProperty("auth")
  private AuthConfiguration authConfiguration;

  @Nonnull
  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(@Nonnull final String protocol) {
    this.protocol = checkNotNull(protocol);
  }

  @Nonnull
  public String getHost() {
    return host;
  }

  public void setHost(@Nonnull final String host) {
    this.host = checkNotNull(host);
  }

  @Nonnull
  public int getPort() {
    return port;
  }

  public void setPort(@Nonnull final int port) {
    this.port = port;
  }

  @Nullable
  public String getNonProxyHosts() {
    return nonProxyHosts;
  }

  public void setNonProxyHosts(@Nullable final String nonProxyHosts) {
    this.nonProxyHosts = nonProxyHosts;
  }

  @Nullable
  public AuthConfiguration getAuthConfiguration() {
    return authConfiguration;
  }

  public void setAuthConfiguration(@Nullable final AuthConfiguration authConfiguration) {
    this.authConfiguration = authConfiguration;
  }
}
