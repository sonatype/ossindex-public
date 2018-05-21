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

import javax.ws.rs.core.MediaType;

/**
 * {@link Version} media-types.
 *
 * @since ???
 */
public final class VersionMediaTypes
{
  private VersionMediaTypes() {
    // empty
  }

  /**
   * {@link Version} media-type.
   */
  public static final String VERSION_V1_JSON = "application/vnd.ossindex.version.v1+json";

  /**
   * {@link Version} media-type.
   */
  public static final MediaType VERSION_V1_JSON_TYPE = new MediaType("application", "vnd.ossindex.version.v1+json");
}
