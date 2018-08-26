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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.sonatype.ossindex.service.api.componentreport.ComponentReportMediaTypes.REPORT_V1_JSON;
import static org.sonatype.ossindex.service.api.componentreport.ComponentReportMediaTypes.REQUEST_V1_JSON;

/**
 * {@link ComponentReport} endpoint.
 *
 * @since 1.0.0
 */
@Path("/api/v3/component-report")
@Api(
    value = "Component vulnerability reports",
    authorizations = {
        @Authorization("basicAuth"),
        @Authorization("apiToken")
    }
)
public interface ComponentReportEndpoint
{
  @GET
  @Path("{coordinates:.*}")
  @Produces({REPORT_V1_JSON, APPLICATION_JSON})
  @ApiOperation(value = "Request vulnerability report for component")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Vulnerability report for component"),
      @ApiResponse(code = 404, message = "Component not found"),
      @ApiResponse(code = 400, message = "Missing coordinates version"),
      @ApiResponse(code = 429, message = "Too many requests")
  })
  @SuppressWarnings("RestParamTypeInspection")
  ComponentReport get(@PathParam("coordinates") @ApiParam("Coordinates as package-url") PackageUrl coordinates);

  @POST
  @Consumes({REQUEST_V1_JSON, APPLICATION_JSON})
  @Produces({REPORT_V1_JSON, APPLICATION_JSON})
  @ApiOperation(value = "Request vulnerability reports for components")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Vulnerability report for components"),
      @ApiResponse(code = 400, message = "Request for too many components"),
      @ApiResponse(code = 400, message = "One or more coordinates required"),
      @ApiResponse(code = 400, message = "Missing coordinates version"),
      @ApiResponse(code = 429, message = "Too many requests")
  })
  List<ComponentReport> post(ComponentReportRequest request);
}
