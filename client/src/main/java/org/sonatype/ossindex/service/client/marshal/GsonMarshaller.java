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
import java.lang.reflect.Type;

import org.sonatype.goodies.packageurl.PackageUrl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Gson {@link Marshaller}.
 *
 * @since 1.0.0
 */
public class GsonMarshaller
    implements Marshaller
{
  private final Gson gson;

  public GsonMarshaller() {
    gson = new GsonBuilder()
        .registerTypeAdapter(PackageUrl.class, new PackageUrlAdapter())
        .create();
  }

  @Override
  public String marshal(final Object value) {
    checkNotNull(value);
    return gson.toJson(value);
  }

  @Override
  public void marshal(final Object value, final Writer writer) throws IOException {
    checkNotNull(value);
    try {
      gson.toJson(value, writer);
    } catch (JsonIOException e) {
      throw new IOException(e);
    } finally {
      writer.flush();
    }
  }

  @Override
  public <T> T unmarshal(final String value, final Class<T> type) throws IOException {
    checkNotNull(value);
    checkNotNull(type);
    try {
      return gson.fromJson(value, type);
    } catch (JsonSyntaxException e) {
      throw new IOException(e);
    }
  }

  @Override
  public <T> T unmarshal(final String value, final TypeToken<T> type) throws IOException {
    checkNotNull(value);
    checkNotNull(type);
    try {
      return gson.fromJson(value, type.getType());
    } catch (JsonSyntaxException e) {
      throw new IOException(e);
    }
  }

  @Override
  public <T> T unmarshal(final Reader reader, final Class<T> type) throws IOException {
    checkNotNull(reader);
    checkNotNull(type);
    try {
      return gson.fromJson(reader, type);
    } catch (JsonIOException | JsonSyntaxException e) {
      throw new IOException(e);
    }
  }

  @Override
  public <T> T unmarshal(final Reader reader, final TypeToken<T> type) throws IOException {
    checkNotNull(reader);
    checkNotNull(type);
    try {
      return gson.fromJson(reader, type.getType());
    } catch (JsonIOException | JsonSyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * {@link PackageUrl} adapter.
   */
  private static class PackageUrlAdapter
      implements JsonDeserializer<PackageUrl>, JsonSerializer<PackageUrl>
  {
    @Override
    public PackageUrl deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context)
        throws JsonParseException
    {
      return PackageUrl.parse(element.getAsString());
    }

    @Override
    public JsonElement serialize(final PackageUrl value, final Type type, final JsonSerializationContext context) {
      return new JsonPrimitive(value.toString());
    }
  }
}
