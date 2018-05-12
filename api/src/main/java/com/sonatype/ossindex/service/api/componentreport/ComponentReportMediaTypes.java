/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.api.componentreport;

import javax.ws.rs.core.MediaType;

/**
 * {@link ComponentReport} media-types.
 *
 * @since ???
 */
public final class ComponentReportMediaTypes
{
  private ComponentReportMediaTypes() {
    // empty
  }

  /**
   * {@link ComponentReport} media-type.
   */
  public static final String REPORT_V1_JSON = "application/vnd.ossindex.component-report.v1+json";

  /**
   * {@link ComponentReport} media-type.
   */
  public static final MediaType REPORT_V1_JSON_TYPE =
      new MediaType("application", "vnd.ossindex.component-report.v1+json");

  /**
   * {@link ComponentReportRequest} media-type.
   */
  public static final String REQUEST_V1_JSON = "application/vnd.ossindex.component-report-request.v1+json";

  /**
   * {@link ComponentReportRequest} media-type.
   */
  public static final MediaType REQUEST_V1_JSON_TYPE =
      new MediaType("application", "vnd.ossindex.component-report-request.v1+json");
}
