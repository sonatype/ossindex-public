/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;

import java.net.URI;

/**
 * {@link OssindexClient} configuration.
 *
 * @since ???
 */
public class OssindexClientConfiguration
{
    public static final URI DEFAULT_BASE_URL = URI.create("http://ossindex.sonatype.org/");

    public static final int DEFAULT_BATCH_SIZE = 64;

    public static final CacheBuilderSpec DEFAULT_CACHE = CacheBuilderSpec.parse(
            "maximumSize=256,expireAfterAccess=2m,softValues"
    );

    @JsonProperty
    private URI baseUrl = DEFAULT_BASE_URL;

    @JsonProperty
    private int batchSize = DEFAULT_BATCH_SIZE;

    @JsonProperty
    private CacheBuilderSpec reportCache = DEFAULT_CACHE;

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        setBaseUrl(URI.create(baseUrl));
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public CacheBuilderSpec getReportCache() {
        return reportCache;
    }

    public void setReportCache(CacheBuilderSpec reportCache) {
        this.reportCache = reportCache;
    }
}
