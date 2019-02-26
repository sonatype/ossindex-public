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
package org.sonatype.ossindex.service.api.cvss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkState;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.BASE_GROUP;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.ENVIRONMENTAL_GROUP;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.TEMPORAL_GROUP;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.builder;

/**
 * CVSS version {@code 3.0} vector.
 *
 * @see <a href="https://www.first.org/cvss/specification-document">CVSS 3.0 Specification</a>
 * @since ???
 */
public class Cvss3Vector
    extends CvssVector
{
  public static final String VERSION = "3.0";

  public static final String PREAMBLE = "CVSS:3.0";

  public static final String ATTACK_VECTOR = "AV";

  public static final String ATTACK_COMPLEXITY = "AC";

  public static final String PRIVILEGES_REQUIRED = "PR";

  public static final String USER_INTERACTION = "UI";

  public static final String SCOPE = "S";

  public static final String CONFIDENTIALITY_IMPACT = "C";

  public static final String INTEGRITY_IMPACT = "I";

  public static final String AVAILABILITY_IMPACT = "A";

  private static final List<CvssMetric> BASE_METRICS = ImmutableList.of(
      builder().code(ATTACK_VECTOR).group(BASE_GROUP).title("Attack Vector")
          .meaning("N", "Network")
          .meaning("A", "Adjacent Network")
          .meaning("L", "Local")
          .meaning("P", "Physical")
          .build(),

      builder().code(ATTACK_COMPLEXITY).group(BASE_GROUP).title("Attack Complexity")
          .meaning("L", "Low")
          .meaning("H", "High")
          .build(),

      builder().code(PRIVILEGES_REQUIRED).group(BASE_GROUP).title("Privileges Required")
          .meaning("N", "None")
          .meaning("L", "Low")
          .meaning("H", "High")
          .build(),

      builder().code(USER_INTERACTION).group(BASE_GROUP).title("User Interaction")
          .meaning("N", "None")
          .meaning("R", "Required")
          .build(),

      builder().code(SCOPE).group(BASE_GROUP).title("Scope")
          .meaning("U", "Unchanged")
          .meaning("C", "Changed")
          .build(),

      builder().code(CONFIDENTIALITY_IMPACT).group(BASE_GROUP).title("Confidentiality Impact")
          .meaning("N", "None")
          .meaning("L", "Low")
          .meaning("H", "High")
          .build(),

      builder().code(INTEGRITY_IMPACT).group(BASE_GROUP).title("Integrity Impact")
          .meaning("N", "None")
          .meaning("L", "Low")
          .meaning("H", "High")
          .build(),

      builder().code(AVAILABILITY_IMPACT).group(BASE_GROUP).title("Availability Impact")
          .meaning("N", "None")
          .meaning("L", "Low")
          .meaning("H", "High")
          .build()
  );

  public static final String EXPLOIT_CODE_MATURITY = "E";

  public static final String REMEDIATION_LEVEL = "RL";

  public static final String REPORT_CONFIDENCE = "RC";

  public static final String CONFIDENTIALITY_REQUIREMENT = "CR";

  public static final String INTEGRITY_REQUIREMENT = "IR";

  public static final String AVAILABILITY_REQUIREMENT = "AR";

  private static final List<CvssMetric> OTHER_METRICS = ImmutableList.of(
      builder().code(EXPLOIT_CODE_MATURITY).group(TEMPORAL_GROUP).title("Exploit Code Maturity")
          .meaning("X", "Not defined")
          .meaning("H", "High")
          .meaning("F", "Functional")
          .meaning("P", "Proof-of-concept")
          .meaning("U", "Unproven")
          .build(),

      builder().code(REMEDIATION_LEVEL).group(TEMPORAL_GROUP).title("Remediation Level")
          .meaning("X", "Not defined")
          .meaning("U", "Unavailable")
          .meaning("W", "Workaround")
          .meaning("T", "Temporary fix")
          .meaning("O", "Official fix")
          .build(),

      builder().code(REPORT_CONFIDENCE).group(TEMPORAL_GROUP).title("Report Confidence")
          .meaning("X", "Not defined")
          .meaning("C", "Confirmed")
          .meaning("R", "Reasonable")
          .meaning("U", "Unknown")
          .build(),

      builder().code(CONFIDENTIALITY_REQUIREMENT).group(ENVIRONMENTAL_GROUP).title("Confidentiality Requirement")
          .meaning("X", "Not defined")
          .meaning("H", "High")
          .meaning("M", "Medium")
          .meaning("L", "Low")
          .build(),

      builder().code(INTEGRITY_REQUIREMENT).group(ENVIRONMENTAL_GROUP).title("Integrity Requirement")
          .meaning("X", "Not defined")
          .meaning("H", "High")
          .meaning("M", "Medium")
          .meaning("L", "Low")
          .build(),

      builder().code(AVAILABILITY_REQUIREMENT).group(ENVIRONMENTAL_GROUP).title("Availability Requirement")
          .meaning("X", "Not defined")
          .meaning("H", "High")
          .meaning("M", "Medium")
          .meaning("L", "Low")
          .build()
  );

  private static final CvssMetric[] METRICS;

  static {
    List<CvssMetric> all = new ArrayList<>();
    all.addAll(BASE_METRICS);
    all.addAll(OTHER_METRICS);

    // add modified variants: MAV, MAC, MPR, MUI, MS, MC, MI, MA; based on Base group
    for (CvssMetric metric : BASE_METRICS) {
      CvssMetric modified = builder().from(metric)
          .code("M" + metric.getCode())
          .title("Modified " + metric.getTitle())
          .meaning("X", "Not defined")
          .build();
      all.add(modified);
    }

    METRICS = all.toArray(new CvssMetric[all.size()]);
  }

  private static final Map<String, CvssMetric> metrics = createLookup(METRICS);

  public Cvss3Vector(final String value) {
    super(VERSION, extractVector(value));
  }

  /**
   * Extract vector value excluding {@link #PREAMBLE} prefix.
   */
  private static String extractVector(final String value) {
    String prefix = PREAMBLE + "/";
    checkState(value.startsWith(prefix), "Missing expected prefix: %s", prefix);
    return value.substring(prefix.length());
  }

  @Nullable
  @Override
  protected CvssMetric findMetric(final String code) {
    return metrics.get(code);
  }
}
