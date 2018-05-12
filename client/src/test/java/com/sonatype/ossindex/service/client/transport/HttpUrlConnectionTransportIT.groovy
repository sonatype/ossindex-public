/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.transport

import org.junit.Ignore
import org.junit.experimental.categories.Category
import org.sonatype.goodies.testsupport.group.External

/**
 * Integration-tests for {@link HttpUrlConnectionTransport}.
 */
@Ignore('Pending resolution of conflicting Guava versions')
@Category(External.class)
class HttpUrlConnectionTransportIT
    extends TransportITSupport
{
    @Override
    protected Transport createTransport() {
        return new HttpUrlConnectionTransport(userAgent)
    }
}
