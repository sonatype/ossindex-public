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
import java.net.URI;

/**
 * Transport abstraction.
 *
 * @since ???
 */
public interface Transport
{
  /**
   * Perform HTTP POST request.
   */
  String post(URI url, String payloadType, String payload, String acceptType) throws TransportException, IOException;

  /**
   * Thrown in case of non-{@link IOException} failure.
   */
  class TransportException
      extends Exception
  {
    public TransportException() {
      // empty
    }

    public TransportException(final String message) {
      super(message);
    }

    public TransportException(final String message, final Throwable cause) {
      super(message, cause);
    }

    public TransportException(final Throwable cause) {
      super(cause);
    }
  }
}
