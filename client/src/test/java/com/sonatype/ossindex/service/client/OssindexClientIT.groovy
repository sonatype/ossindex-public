/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client

import com.sonatype.ossindex.service.OssindexApplication
import com.sonatype.ossindex.service.OssindexConfiguration
import com.sonatype.ossindex.service.client.internal.GsonMarshaller
import com.sonatype.ossindex.service.client.internal.OssindexClientImpl
import com.sonatype.ossindex.service.client.transport.HttpClientTransport
import com.sonatype.ossindex.service.client.transport.HttpUrlConnectionTransport
import com.sonatype.ossindex.service.client.transport.Marshaller
import com.sonatype.ossindex.service.client.transport.Transport
import com.sonatype.ossindex.service.client.transport.UserAgentSupplier
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.Before
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Test
import org.sonatype.goodies.packageurl.PackageUrl
import org.sonatype.goodies.testsupport.TestSupport

/**
 * Integration-tests for {@link OssindexClient}.
 */
@Ignore('Pending resolution of conflicting Guava versions')
class OssindexClientIT
    extends TestSupport
{
    static {
        System.setProperty('test.log.level', 'DEBUG')
    }

    @ClassRule
    public static final DropwizardAppRule<OssindexConfiguration> application = new DropwizardAppRule<>(
            OssindexApplication.class,
            ResourceHelpers.resourceFilePath('service.yml')
    )

    private OssindexClientConfiguration config

    private UserAgentSupplier userAgent

    private Marshaller marshaller

    @Before
    void setUp() {
        config = new OssindexClientConfiguration(
            baseUrl: URI.create("http://localhost:${application.localPort}")
        )
        userAgent = new UserAgentSupplier(getClass().simpleName, '1-TEST')
        marshaller = new GsonMarshaller()
    }

    private void basicUsage(final Transport transport) {
        def client = new OssindexClientImpl(config, transport, marshaller)

        def purl = PackageUrl.parse('maven:commons-fileupload/commons-fileupload@1.3')
        log "Package-URL: $purl"

        def report = client.requestComponentReport(purl)
        log "Report: $report"
        assert report != null
        assert report.package == purl

        // TODO: assert results
        report.vulnerabilities.each {
            log "$it -> ${it.cvssScore}; ${it.cvssVector}"
        }
    }

    @Test
    void 'basic-usage with httpclient'() {
        basicUsage new HttpClientTransport(userAgent)
    }

    @Test
    void 'basic-usage with url-connection'() {
        basicUsage new HttpUrlConnectionTransport(userAgent)
    }
}
