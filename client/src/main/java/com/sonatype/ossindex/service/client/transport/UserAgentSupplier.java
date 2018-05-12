/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.transport;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.sonatype.ossindex.service.client.internal.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Supplies {@code User-Agent} header value.
 *
 * @since ???
 */
public class UserAgentSupplier
    implements Supplier<String>
{
    private static final Logger log = LoggerFactory.getLogger(UserAgentSupplier.class);

    private String value;

    public UserAgentSupplier(final Version version) {
        this("ossindex-client", version.getVersion());
    }

    @VisibleForTesting
    public UserAgentSupplier(final String product, final String version) {
        checkNotNull(product);
        checkNotNull(version);

        value = String.format("%s/%s (%s; %s; %s; %s)",
                product,
                version,
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch"),
                System.getProperty("java.version")
        );
        log.debug("User-agent: {}", value);
    }

    @Override
    public String get() {
        return value;
    }
}
