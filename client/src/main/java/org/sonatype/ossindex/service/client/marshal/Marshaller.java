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
package org.sonatype.ossindex.service.client.marshal;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.common.reflect.TypeToken;

/**
 * Object marshaller.
 *
 * @since 1.0.0
 */
public interface Marshaller
{
  //
  // Marshal
  //

  /**
   * Marshal value to string.
   */
  String marshal(Object value);

  /**
   * Marshal value to writer.
   *
   * @since ???
   */
  void marshal(Object value, Writer writer) throws IOException;

  //
  // Unmarshal
  //

  /**
   * Unmarshal value for type.
   */
  <T> T unmarshal(String value, Class<T> type);

  /**
   * Unmarshal value for type.
   */
  <T> T unmarshal(String value, TypeToken<T> type);

  /**
   * Unmarshal value for type.
   *
   * @since ???
   */
  <T> T unmarshal(Reader reader, Class<T> type) throws IOException;

  /**
   * Unmarshal value for type.
   *
   * @since ???
   */
  <T> T unmarshal(Reader reader, TypeToken<T> type) throws IOException;
}
