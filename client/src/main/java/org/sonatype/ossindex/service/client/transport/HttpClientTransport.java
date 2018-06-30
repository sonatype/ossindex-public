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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import org.sonatype.ossindex.service.client.AuthConfiguration;
import org.sonatype.ossindex.service.client.OssindexClientConfiguration;
import org.sonatype.ossindex.service.client.ProxyConfiguration;

import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Apache {@link org.apache.http.client.HttpClient} transport.
 *
 * @since ???
 */
public class HttpClientTransport
    implements Transport
{
  private static final Logger log = LoggerFactory.getLogger(HttpClientTransport.class);

  private final UserAgentSupplier userAgent;

  private OssindexClientConfiguration configuration;

  public HttpClientTransport(final UserAgentSupplier userAgent) {
    this.userAgent = checkNotNull(userAgent);
  }

  @Override
  public void init(final OssindexClientConfiguration configuration) {
    this.configuration = checkNotNull(configuration);
  }

  // TODO: check if we need to use httpclient.execute(request,context) form?

  @Override
  public String post(final URI url, final String payloadType, final String payload, final String acceptType)
      throws TransportException, IOException
  {
    checkArgument(configuration != null);

    log.debug("POST {}; payload: {} ({}); accept: {}", url, payload, payloadType, acceptType);

    try (CloseableHttpClient httpClient = createClient()) {
      HttpPost request = new HttpPost(url.toURL().toExternalForm());
      customize(request);
      request.setHeader(HttpHeaders.ACCEPT, acceptType);
      request.setEntity(new StringEntity(payload, ContentType.create(payloadType, Charsets.UTF_8)));

      try (CloseableHttpResponse response = httpClient.execute(request)) {
        StatusLine status = response.getStatusLine();
        log.trace("Status: {}", status);

        if (status.getStatusCode() == HttpURLConnection.HTTP_OK) {
          HttpEntity entity = response.getEntity();
          try {
            return EntityUtils.toString(entity);
          }
          finally {
            EntityUtils.consumeQuietly(entity);
          }
        }

        throw new TransportException("Unexpected response; status: " + status);
      }
    }
  }

  /**
   * Create customized client.
   */
  protected CloseableHttpClient createClient() {
    HttpClientBuilder builder = HttpClientBuilder.create();

    // disable support for cookies
    builder.disableCookieManagement();

    // prepare default credentials provider
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    builder.setDefaultCredentialsProvider(credentialsProvider);

    // maybe configure http-proxy support
    ProxyConfiguration proxyConfiguration = configuration.getProxyConfiguration();
    if (proxyConfiguration != null) {
      // TODO: non-proxy-hosts matching?

      HttpHost proxy = new HttpHost(proxyConfiguration.getHost(), proxyConfiguration.getPort(), proxyConfiguration.getProtocol());
      builder.setProxy(proxy);
      log.debug("Configured http-proxy: {}", proxy);

      // maybe configure http-proxy authentication
      AuthConfiguration auth = proxyConfiguration.getAuthConfiguration();
      if (auth != null) {
        credentialsProvider.setCredentials(
            new AuthScope(proxy),
            new UsernamePasswordCredentials(auth.getUsername(), auth.getPassword())
        );
        log.debug("Configured http-proxy authentication");
      }
    }

    return builder.build();
  }

  /**
   * Customize request.
   */
  protected void customize(final HttpPost request) {
    request.setHeader(HttpHeaders.USER_AGENT, userAgent.get());

    // maybe add authorization headers if configured
    String authorization = BasicAuthHelper.authorizationHeader(configuration.getAuthConfiguration());
    if (authorization != null) {
      request.setHeader(HttpHeaders.AUTHORIZATION, authorization);
    }
  }
}
