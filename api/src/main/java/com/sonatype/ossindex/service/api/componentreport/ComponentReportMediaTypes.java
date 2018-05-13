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
