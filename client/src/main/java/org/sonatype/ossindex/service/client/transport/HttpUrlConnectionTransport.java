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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;

import javax.annotation.Nullable;

import org.sonatype.ossindex.service.client.OssindexClientConfiguration;

import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.USER_AGENT;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * {@link HttpURLConnection} transport.
 *
 * @since 1.0.0
 */
public class HttpUrlConnectionTransport
    implements Transport
{
  private static final Logger log = LoggerFactory.getLogger(HttpUrlConnectionTransport.class);

  private final UserAgentSupplier userAgent;

  private OssindexClientConfiguration configuration;

  public HttpUrlConnectionTransport(final UserAgentSupplier userAgent) {
    this.userAgent = checkNotNull(userAgent);
  }

  @Override
  public void init(final OssindexClientConfiguration configuration) {
    this.configuration = checkNotNull(configuration);
  }

  @Override
  public void close() throws Exception {
    // empty
  }

  @Override
  public String post(final URI url, final String payloadType, final String payload, final String acceptType)
      throws TransportException, IOException
  {
    log.debug("POST {}; payload: {} ({}); accept: {}", url, payload, payloadType, acceptType);

    HttpURLConnection connection = connect(url.toURL());
    connection.setDoInput(true);
    connection.setDoOutput(true);

    connection.setRequestMethod("POST");
    connection.setRequestProperty(CONTENT_TYPE, payloadType);
    connection.setRequestProperty(ACCEPT, acceptType);

    log.debug("Connecting to: {}", url);
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
      writer.write(payload);
    }
    connection.connect();

    int status = connection.getResponseCode();
    log.trace("Status: {}", status);

    if (status == HTTP_OK) {
      try (Reader reader = new InputStreamReader(connection.getInputStream())) {
        StringWriter buff = new StringWriter();
        CharStreams.copy(reader, buff);
        return buff.toString();
      }
    }

    throw new TransportException("Unexpected response; status: " + status);
  }

  /**
   * Return connection for URL; customize as needed.
   */
  protected HttpURLConnection connect(final URL url) throws IOException {
    checkNotNull(url);
    checkState(configuration != null, "Not initialized");

    HttpURLConnection connection = null;
    try {
      Proxy proxy = maybeCreateProxy(configuration);
      if (proxy != null) {
        connection = (HttpURLConnection) url.openConnection(proxy);
      }
      else {
        connection = (HttpURLConnection) url.openConnection();
      }

      connection.setRequestProperty(USER_AGENT, userAgent.get());

      // maybe add authorization headers if configured
      String authorization = BasicAuthHelper.authorizationHeader(configuration.getAuthConfiguration());
      if (authorization != null) {
        connection.setRequestProperty(AUTHORIZATION, authorization);
      }
    }
    catch (IOException e) {
      //noinspection ConstantConditions
      if (connection != null) {
        connection.disconnect();
      }
      throw new IOException("Failed to create connection", e);
    }

    return connection;
  }

  /**
   * Maybe create a proxy if configured.
   */
  @Nullable
  protected Proxy maybeCreateProxy(final OssindexClientConfiguration configuration) {
    checkNotNull(configuration);

    ProxyConfiguration proxyConfiguration = configuration.getProxyConfiguration();
    if (proxyConfiguration == null) {
      return null;
    }

    // TODO: non-proxy-hosts matching?

    final String proxyHost = proxyConfiguration.getHost();
    final SocketAddress address = new InetSocketAddress(proxyHost, proxyConfiguration.getPort());

    AuthConfiguration auth = proxyConfiguration.getAuthConfiguration();
    if (auth != null) {
      final PasswordAuthentication credentials =
          new PasswordAuthentication(auth.getUsername(), auth.getPassword().toCharArray());

      Authenticator authenticator = new Authenticator()
      {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          if (proxyHost.equals(getRequestingHost()) || getRequestorType().equals(RequestorType.PROXY)) {
            return credentials;
          }
          else {
            return super.getPasswordAuthentication();
          }
        }
      };

      Authenticator.setDefault(authenticator);
      log.debug("Configured http-proxy authentication");
    }

    Proxy proxy = new Proxy(Type.HTTP, address);
    log.debug("Configured http-proxy: {}", proxy);

    return proxy;
  }
}
