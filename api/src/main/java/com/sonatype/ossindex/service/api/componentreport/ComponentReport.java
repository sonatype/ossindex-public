/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.api.componentreport;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.sonatype.goodies.packageurl.PackageUrl;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

/**
 * Component report.
 *
 * @since ???
 */
@ApiModel(description = "Component-report")
public class ComponentReport
    implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty
  private PackageUrl coordinates;

  @JsonProperty
  private String description;

  @JsonProperty
  private URI reference;

  @JsonProperty
  private List<ComponentReportVulnerability> vulnerabilities;

  public PackageUrl getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(PackageUrl coordinates) {
    this.coordinates = coordinates;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public URI getReference() {
    return reference;
  }

  public void setReference(URI reference) {
    this.reference = reference;
  }

  public List<ComponentReportVulnerability> getVulnerabilities() {
    if (vulnerabilities == null) {
      vulnerabilities = new ArrayList<>();
    }
    return vulnerabilities;
  }

  public void setVulnerabilities(List<ComponentReportVulnerability> vulnerabilities) {
    this.vulnerabilities = vulnerabilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponentReport that = (ComponentReport) o;
    return Objects.equals(coordinates, that.coordinates) &&
        Objects.equals(description, that.description) &&
        Objects.equals(reference, that.reference) &&
        Objects.equals(vulnerabilities, that.vulnerabilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates, description, reference, vulnerabilities);
  }

  @Override
  public String toString() {
    return "ComponentReport{" +
        "coordinates=" + coordinates +
        ", description='" + description + '\'' +
        '}';
  }
}
