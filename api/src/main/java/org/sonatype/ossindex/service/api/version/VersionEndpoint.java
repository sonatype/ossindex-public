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
package org.sonatype.ossindex.service.api.version;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.sonatype.ossindex.service.api.version.VersionMediaTypes.VERSION_V1_JSON;

/**
 * {@link Version} endpoint.
 *
 * @since 1.0.0
 */
@Path("/api/v3/version")
@Api(value = "Version")
public interface VersionEndpoint
{
  @GET
  @Produces({VERSION_V1_JSON, APPLICATION_JSON})
  @ApiOperation(value = "Get service version information")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Version information"),
      @ApiResponse(code = 429, message = "Too many requests")
  })
  Version get();
}
