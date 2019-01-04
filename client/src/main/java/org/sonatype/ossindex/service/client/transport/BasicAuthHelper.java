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

import java.io.UnsupportedEncodingException;

import javax.annotation.Nullable;

import com.google.common.io.BaseEncoding;

import static com.google.common.base.Preconditions.checkState;

/**
 * Basic authentication helper.
 *
 * @since 1.0.0
 */
public class BasicAuthHelper
{
  private BasicAuthHelper() {
    // empty
  }

  @Nullable
  public static String authorizationHeader(@Nullable final AuthConfiguration configuration) {
    if (configuration == null) {
      return null;
    }

    String username = configuration.getUsername();
    checkState(username != null, "Missing: username");

    String password = configuration.getPassword();
    checkState(password != null, "Missing: password");

    String encoded = BaseEncoding.base64().encode(bytesOf(username + ":" + password));
    return "Basic " + encoded;
  }

  @SuppressWarnings("CharsetObjectCanBeUsed")
  private static byte[] bytesOf(final String value) {
    try {
      return value.getBytes("ISO-8859-1");
    }
    catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
