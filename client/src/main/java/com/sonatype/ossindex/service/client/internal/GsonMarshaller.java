/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.internal;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sonatype.ossindex.service.client.transport.Marshaller;
import org.joda.time.Instant;
import org.sonatype.goodies.packageurl.PackageUrl;

import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Gson {@link Marshaller}.
 *
 * @since ???
 */
public class GsonMarshaller
    implements Marshaller
{
    private final Gson gson;

    public GsonMarshaller() {
        gson = new GsonBuilder()
                .registerTypeAdapter(PackageUrl.class, new PackageUrlAdapter())
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }

    @Override
    public String marshal(final Object value) {
        checkNotNull(value);
        return gson.toJson(value);
    }

    @Override
    public <T> T unmarshal(final String value, final Class<T> type) {
        checkNotNull(value);
        checkNotNull(type);
        return gson.fromJson(value, type);
    }

    @Override
    public <T> T unmarshal(final String value, final TypeToken<T> type) {
        checkNotNull(value);
        checkNotNull(type);
        return gson.fromJson(value, type.getType());
    }

    /**
     * {@link PackageUrl} adapter.
     */
    private static class PackageUrlAdapter
        implements JsonDeserializer<PackageUrl>, JsonSerializer<PackageUrl>
    {
        @Override
        public PackageUrl deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
            return PackageUrl.parse(element.getAsString());
        }

        @Override
        public JsonElement serialize(final PackageUrl value, final Type type, final JsonSerializationContext context) {
            return new JsonPrimitive(value.toString());
        }
    }

    // TODO: this is presently ancillary as no time fields are exposed, but here for future reference

    /**
     * Joda-time {@link Instant} adapter.
     */
    private static class InstantAdapter
        implements JsonDeserializer<Instant>, JsonSerializer<Instant>
    {
        @Override
        public Instant deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
            return new Instant(element.getAsLong());
        }

        @Override
        public JsonElement serialize(final Instant value, final Type type, final JsonSerializationContext context) {
            return new JsonPrimitive(value.getMillis());
        }
    }
}
