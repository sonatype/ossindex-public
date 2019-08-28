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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.google.common.reflect.TypeToken;

import org.sonatype.goodies.packageurl.PackageUrl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Jackson {@link Marshaller}.
 *
 * @since 1.3.0
 */
public class JacksonMarshaller
    implements Marshaller
{
  private final ObjectMapper mapper;

  public JacksonMarshaller() {
    // Mirror the Gson behavior to not auto-close streams after writing.
    JsonFactory jsonFactory = new JsonFactory();
    jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

    // Register custom (de-)serializers for PackageUrl.
    SimpleModule module = new SimpleModule();
    module.addSerializer(PackageUrl.class, new PackageUrlSerializer());
    module.addDeserializer(PackageUrl.class, new PackageUrlDeserializer());

    mapper = new ObjectMapper(jsonFactory);
    mapper.registerModule(module);
  }

  @Override
  public String marshal(final Object value) throws IOException {
    checkNotNull(value);
    return mapper.writeValueAsString(value);
  }

  @Override
  public void marshal(final Object value, final Writer writer) throws IOException {
    checkNotNull(value);
    mapper.writeValue(writer, value);
    writer.flush();
  }

  @Override
  public <T> T unmarshal(final String value, final Class<T> type) throws IOException {
    checkNotNull(value);
    checkNotNull(type);
    return mapper.readValue(value, type);
  }

  @Override
  public <T> T unmarshal(final String value, final TypeToken<T> type) throws IOException {
    checkNotNull(value);
    checkNotNull(type);
    return mapper.readValue(value, mapper.constructType(type.getType()));
  }

  @Override
  public <T> T unmarshal(final Reader reader, final Class<T> type) throws IOException {
    checkNotNull(reader);
    checkNotNull(type);
    return mapper.readValue(reader, type);
  }

  @Override
  public <T> T unmarshal(final Reader reader, final TypeToken<T> type) throws IOException {
    checkNotNull(reader);
    checkNotNull(type);
    return mapper.readValue(reader, mapper.constructType(type.getType()));
  }

  /**
   * {@link PackageUrl} serializer.
   */
  private static class PackageUrlSerializer extends StdSerializer<PackageUrl> {
    PackageUrlSerializer() {
      super(PackageUrl.class);
    }

    @Override
    public void serialize(PackageUrl packageUrl, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeString(packageUrl.toString());
    }
  }

  /**
   * {@link PackageUrl} deserializer.
   */
  private static class PackageUrlDeserializer extends StdDeserializer<PackageUrl> {
    PackageUrlDeserializer() {
      super(PackageUrl.class);
    }

    @Override
    public PackageUrl deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
      return PackageUrl.parse(jsonParser.getValueAsString());
    }
  }
}
