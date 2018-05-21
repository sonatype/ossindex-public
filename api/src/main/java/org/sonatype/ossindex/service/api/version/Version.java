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

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Version.
 *
 * @since ???
 */
@ApiModel(description = "Version")
public class Version
    implements Serializable
{
  private static final long serialVersionUID = 1L;

  @ApiModelProperty("Version")
  @JsonProperty
  private String version;

  // TODO: consider changing to DateTime or Instant?

  @ApiModelProperty("Build timestamp")
  @JsonProperty
  private String buildTimestamp;

  @ApiModelProperty("Build tag")
  @JsonProperty
  private String buildTag;

  public Version(final String version, final String buildTimestamp, final String buildTag) {
    this.version = version;
    this.buildTimestamp = buildTimestamp;
    this.buildTag = buildTag;
  }

  public Version() {
    // empty
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  public String getBuildTimestamp() {
    return buildTimestamp;
  }

  public void setBuildTimestamp(final String buildTimestamp) {
    this.buildTimestamp = buildTimestamp;
  }

  public String getBuildTag() {
    return buildTag;
  }

  public void setBuildTag(final String buildTag) {
    this.buildTag = buildTag;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Version version1 = (Version) o;
    return Objects.equals(version, version1.version) &&
        Objects.equals(buildTimestamp, version1.buildTimestamp) &&
        Objects.equals(buildTag, version1.buildTag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, buildTimestamp, buildTag);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("version", version)
        .add("buildTimestamp", buildTimestamp)
        .add("buildTag", buildTag)
        .toString();
  }
}
