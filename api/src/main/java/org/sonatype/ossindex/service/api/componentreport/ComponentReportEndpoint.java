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
package org.sonatype.ossindex.service.api.componentreport;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.sonatype.goodies.packageurl.PackageUrl;

import io.swagger.annotations.ApiParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.sonatype.ossindex.service.api.componentreport.ComponentReportMediaTypes.REPORT_V1_JSON;
import static org.sonatype.ossindex.service.api.componentreport.ComponentReportMediaTypes.REQUEST_V1_JSON;

/**
 * {@link ComponentReport} endpoint.
 *
 * @since 1.0.0
 */
@Path("/api/v3/component-report")
public interface ComponentReportEndpoint
{
  @GET
  @Path("{coordinates:.*}")
  @Produces({REPORT_V1_JSON, APPLICATION_JSON})
  @SuppressWarnings("RestParamTypeInspection")
  ComponentReport get(@PathParam("coordinates") @ApiParam("Coordinates") PackageUrl coordinates);

  @POST
  @Consumes({REQUEST_V1_JSON, APPLICATION_JSON})
  @Produces({REPORT_V1_JSON, APPLICATION_JSON})
  List<ComponentReport> post(ComponentReportRequest request);
}
