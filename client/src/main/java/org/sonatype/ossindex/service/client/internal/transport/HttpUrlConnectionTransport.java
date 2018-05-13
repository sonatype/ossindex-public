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
package org.sonatype.ossindex.service.client.internal.transport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.google.common.io.CharStreams;
import com.google.common.net.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link HttpURLConnection} transport.
 *
 * @since ???
 */
public class HttpUrlConnectionTransport
    implements Transport
{
  private static final Logger log = LoggerFactory.getLogger(HttpUrlConnectionTransport.class);

  private final UserAgentSupplier userAgent;

  public HttpUrlConnectionTransport(final UserAgentSupplier userAgent) {
    this.userAgent = checkNotNull(userAgent);
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
    connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, payloadType);
    connection.setRequestProperty(HttpHeaders.ACCEPT, acceptType);

    log.debug("Connecting to: {}", url);
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
      writer.write(payload);
    }
    connection.connect();

    int status = connection.getResponseCode();
    log.trace("Status: {}", status);

    if (status == HttpURLConnection.HTTP_OK) {
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
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty(HttpHeaders.USER_AGENT, userAgent.get());
    return connection;
  }
}
