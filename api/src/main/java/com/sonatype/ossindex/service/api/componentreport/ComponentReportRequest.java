/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
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

/**
 * {@link ComponentReport} request.
 *
 * @since ???
 */
@ApiModel(description = "Component-report request")
public class ComponentReportRequest
    implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty
  private List<PackageUrl> coordinates;

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
