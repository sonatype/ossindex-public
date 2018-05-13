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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.sonatype.goodies.packageurl.PackageUrl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * {@link ComponentReport} request.
 *
 * @since ???
 */
@ApiModel(description = "Component vulnerability report request")
public class ComponentReportRequest
    implements Serializable
{
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "One or more component coordinates as package-url", required = true)
  @JsonProperty
  private List<PackageUrl> coordinates;

  public ComponentReportRequest(final List<PackageUrl> coordinates) {
    this.coordinates = coordinates;
  }

  public ComponentReportRequest() {
    // empty
  }

  public List<PackageUrl> getCoordinates() {
    if (coordinates == null) {
      coordinates = new ArrayList<>();
    }
    return coordinates;
  }

  public void setCoordinates(List<PackageUrl> coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponentReportRequest that = (ComponentReportRequest) o;
    return Objects.equals(coordinates, that.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("coordinates", coordinates)
        .toString();
  }
}
