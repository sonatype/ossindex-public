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

import org.sonatype.goodies.packageurl.jackson.PackageUrlModule;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Jackson {@link Marshaller}.
 *
 * @since 1.3.0
 */
public class JacksonMarshaller
    implements Marshaller
{
  private final ObjectMapper objectMapper;

  public JacksonMarshaller() {
    this.objectMapper = new ObjectMapper()
        .registerModule(new PackageUrlModule())
        .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Override
  public String marshal(final Object value) throws IOException {
    checkNotNull(value);
    return objectMapper.writeValueAsString(value);
  }

  @Override
  public void marshal(final Object value, final Writer writer) throws IOException {
    checkNotNull(value);
    objectMapper.writeValue(writer, value);
    writer.flush();
  }

  @Override
  public <T> T unmarshal(final String value, final Class<T> type) throws IOException {
    checkNotNull(value);
    checkNotNull(type);
    return objectMapper.readValue(value, type);
  }

  @Override
  public <T> T unmarshal(final String value, final TypeToken<T> type) throws IOException {
    checkNotNull(value);
    checkNotNull(type);
    return objectMapper.readValue(value, objectMapper.constructType(type.getType()));
  }

  @Override
  public <T> T unmarshal(final Reader reader, final Class<T> type) throws IOException {
    checkNotNull(reader);
    checkNotNull(type);
    return objectMapper.readValue(reader, type);
  }

  @Override
  public <T> T unmarshal(final Reader reader, final TypeToken<T> type) throws IOException {
    checkNotNull(reader);
    checkNotNull(type);
    return objectMapper.readValue(reader, objectMapper.constructType(type.getType()));
  }
}