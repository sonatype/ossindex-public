/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.transport;

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
