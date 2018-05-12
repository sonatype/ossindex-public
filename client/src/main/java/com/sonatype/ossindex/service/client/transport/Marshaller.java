/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.transport;

import com.google.common.reflect.TypeToken;

/**
 * Object marshaller.
 *
 * @since ???
 */
public interface Marshaller
{
  /**
   * Marshal value to string.
   */
  String marshal(Object value);

  /**
   * Unmarshal value for type.
   */
  <T> T unmarshal(String value, Class<T> type);

  /**
   * Unmarshal value for type.
   */
  <T> T unmarshal(String value, TypeToken<T> type);
}
