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

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sonatype.goodies.packageurl.PackageUrl;
import org.sonatype.goodies.packageurl.jaxb.PackageUrlXmlAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Component report.
 *
 * @since 1.0.0
 */
@ApiModel(description = "Component vulnerability report")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ComponentReport
    implements Serializable
{
  private static final long serialVersionUID = 1L;

  @ApiModelProperty("Component coordinates as package-url")
  @JsonProperty
  @XmlElement
  @XmlJavaTypeAdapter(PackageUrlXmlAdapter.class)
  private PackageUrl coordinates;

  @ApiModelProperty("Component description")
  @JsonProperty
  @XmlElement
  private String description;

  @ApiModelProperty("Component details reference")
  @JsonProperty
  @XmlElement
  private URI reference;

  @ApiModelProperty("Vulnerabilities recorded for component")
  @JsonProperty
  @XmlElementWrapper
  @XmlElement(name="vulnerability")
  private List<ComponentReportVulnerability> vulnerabilities;

  @ApiModelProperty("Sonatype Ossi Score")
  @JsonProperty
  @XmlElement(name="sonatype:ossi:score")
  private float sonatypeOssiScore;

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

  public Float getSonatypeOssiScore() {
    return sonatypeOssiScore;
  }

  public void setSonatypeOssiScore(float sonatypeOssiScore) {
    this.sonatypeOssiScore = sonatypeOssiScore;
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
        (sonatypeOssiScore == that.sonatypeOssiScore) &&
        Objects.equals(vulnerabilities, that.vulnerabilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates, description, reference, sonatypeOssiScore, vulnerabilities);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("coordinates", coordinates)
        .add("description", description)
        .toString();
  }
}
