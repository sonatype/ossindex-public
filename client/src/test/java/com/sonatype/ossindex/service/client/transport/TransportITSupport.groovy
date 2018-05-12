/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.transport

import com.sonatype.ossindex.service.OssindexApplication
import com.sonatype.ossindex.service.OssindexConfiguration
import com.sonatype.ossindex.service.api.componentreport.ComponentReportRequest
import com.sonatype.ossindex.service.client.internal.GsonMarshaller
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.sonatype.goodies.packageurl.PackageUrl
import org.sonatype.goodies.testsupport.TestSupport

import static com.sonatype.ossindex.service.api.componentreport.ComponentReportMediaTypes.REPORT_V1_JSON
import static com.sonatype.ossindex.service.api.componentreport.ComponentReportMediaTypes.REQUEST_V1_JSON

/**
 * Support for {@link Transport} integration-tests.
 */
abstract class TransportITSupport
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

    private Transport underTest

    private Marshaller marshaller

    protected final UserAgentSupplier userAgent = new UserAgentSupplier('test', '1-TEST')

    private URI baseUrl

    @Before
    void setUp() {
        marshaller = new GsonMarshaller()
        baseUrl = URI.create("http://localhost:${application.localPort}/") // trailing "/" is important
        underTest = createTransport()
    }

    /**
     * Create the transport instance to test.
     */
    protected abstract Transport createTransport()

    @Test
    void post() throws Exception {
        def url = baseUrl.resolve("api/v3/package-report")
        def packages = [
            PackageUrl.parse('maven:commons-fileupload/commons-fileupload@1.3')
        ]

        def request = new ComponentReportRequest(packages: packages)
        def payload = marshaller.marshal(request)
        log "Payload: $payload"

        String result = underTest.post(url, REQUEST_V1_JSON, payload, REPORT_V1_JSON)
        log "Result: $result"

        assert result != ""
    }
}
