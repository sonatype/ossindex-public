/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client;

import com.sonatype.ossindex.service.api.componentreport.ComponentReport;
import org.sonatype.goodies.packageurl.PackageUrl;

import java.util.List;
import java.util.Map;

/**
 * Sonatype OSS Index client.
 *
 * @since ???
 */
public interface OssindexClient
{
    /**
     * Request component reports for coordinates.
     */
    Map<PackageUrl,ComponentReport> requestComponentReports(List<PackageUrl> coordinates) throws Exception;

    /**
     * Request component report for coordinates.
     */
    ComponentReport requestComponentReport(PackageUrl coordinates) throws Exception;
}
