/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

// NOTE: duplicated from com.sonatype.ossindex.dropwizard.util.Version to avoid complexity of another common module

/**
 * Helper to load version information from build via {@link #RESOURCE} resource.
 *
 * @since ???
 */
@SuppressWarnings("Duplicates")
public class Version
{
    private static final Logger log = LoggerFactory.getLogger(Version.class);

    public static final String RESOURCE = "version.properties";

    private final Class owner;

    public Version(final Class owner) {
        this.owner = checkNotNull(owner);
    }

    private Properties load() {
        Properties result = new Properties();
        URL resource = owner.getResource(RESOURCE);
        if (resource == null) {
            log.warn("Missing resource: {}", RESOURCE);
        }
        else {
            log.debug("Resource: {}", resource);
            try {
                try (InputStream input = resource.openStream()) {
                    result.load(input);
                }
            } catch (Exception e) {
                log.warn("Failed to load resource: {}", RESOURCE, e);
            }
            log.debug("Properties: {}", result);
        }
        return result;
    }

    private Properties properties;

    private Properties properties() {
        if (properties == null) {
            properties = load();
        }
        return properties;
    }

    public String getVersion() {
        return properties().getProperty("version", "unknown");
    }

    public String getTimestamp() {
        return properties().getProperty("timestamp", "unknown");
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getVersion(), getTimestamp());
    }
}
