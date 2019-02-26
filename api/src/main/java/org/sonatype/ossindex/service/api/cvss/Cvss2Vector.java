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

import java.util.Map;

import javax.annotation.Nullable;

import static org.sonatype.ossindex.service.api.cvss.CvssMetric.BASE_GROUP;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.ENVIRONMENTAL_GROUP;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.TEMPORAL_GROUP;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.builder;

/**
 * CVSS version {@code 2.0} vector.
 *
 * @see <a href="https://www.first.org/cvss/v2/guide">CVSS 2.0 Guide</a>
 * @since ???
 */
public class Cvss2Vector
    extends CvssVector
{
  public static final String VERSION = "2.0";

  public static final String ACCESS_VECTOR = "AV";

  public static final String ACCESS_COMPLEXITY = "AC";

  public static final String AUTHENTICATION = "Au";

  public static final String CONFIDENTIALITY_IMPACT = "C";

  public static final String INTEGRITY_IMPACT = "I";

  public static final String AVAILABILITY_IMPACT = "A";

  public static final String EXPLOITABILITY = "E";

  public static final String REMEDIATION_LEVEL = "RL";

  public static final String REPORT_CONFIDENCE = "RC";

  public static final String COLLATERAL_DAMAGE_POTENTIAL = "CDP";

  public static final String TARGET_DISTRIBUTION = "TD";

  public static final String CONFIDENTIALITY_REQUIREMENT = "CR";

  public static final String INTEGRITY_REQUIREMENT = "IR";

  public static final String AVAILABILITY_REQUIREMENT = "AR";

  private static final CvssMetric[] METRICS = {
      builder().code(ACCESS_VECTOR).group(BASE_GROUP).title("Access Vector")
          .meaning("N", "Network")
          .meaning("A", "Adjacent Network")
          .meaning("L", "Local")
          .meaning("P", "Physical")
          .build(),

      builder().code(ACCESS_COMPLEXITY).group(BASE_GROUP).title("Access Complexity")
          .meaning("L", "Low")
          .meaning("M", "Medium")
          .meaning("H", "High")
          .build(),

      builder().code(AUTHENTICATION).group(BASE_GROUP).title("Authentication")
          .meaning("N", "None")
          .meaning("M", "Multiple")
          .meaning("S", "Single")
          .build(),

      builder().code(CONFIDENTIALITY_IMPACT).group(BASE_GROUP).title("Confidentiality Impact")
          .meaning("P", "Partial")
          .meaning("C", "Complete")
          .meaning("N", "None")
          .build(),

      builder().code(INTEGRITY_IMPACT).group(BASE_GROUP).title("Integrity Impact")
          .meaning("P", "Partial")
          .meaning("C", "Complete")
          .meaning("N", "None")
          .build(),

      builder().code(AVAILABILITY_IMPACT).group(BASE_GROUP).title("Availability Impact")
          .meaning("P", "Partial")
          .meaning("C", "Complete")
          .meaning("N", "None")
          .build(),

      builder().code(EXPLOITABILITY).group(TEMPORAL_GROUP).title("Exploitability")
          .meaning("ND", "Not defined")
          .meaning("H", "High")
          .meaning("F", "Functional")
          .meaning("POC", "Proof-of-concept")
          .meaning("U", "Unproven")
          .build(),

      builder().code(REMEDIATION_LEVEL).group(TEMPORAL_GROUP).title("Remediation Level")
          .meaning("ND", "Not defined")
          .meaning("U", "Unavailable")
          .meaning("W", "Workaround")
          .meaning("T", "Temporary fix")
          .meaning("OF", "Official fix")
          .build(),

      builder().code(REPORT_CONFIDENCE).group(TEMPORAL_GROUP).title("Report Confidence")
          .meaning("ND", "Not defined")
          .meaning("UC", "Unconfirmed")
          .meaning("C", "Confirmed")
          .meaning("UR", "Uncorroborated")
          .build(),

      builder().code(COLLATERAL_DAMAGE_POTENTIAL).group(ENVIRONMENTAL_GROUP).title("Collateral Damage Potential")
          .meaning("N", "None")
          .meaning("L", "Low")
          .meaning("LM", "Low-Medium")
          .meaning("MH", "Medium-High")
          .meaning("H", "High")
          .meaning("ND", "Not defined")
          .build(),

      builder().code(TARGET_DISTRIBUTION).group(ENVIRONMENTAL_GROUP).title("Target Distribution")
          .meaning("N", "None")
          .meaning("L", "Low")
          .meaning("M", "Medium")
          .meaning("H", "High")
          .meaning("ND", "Not defined")
          .build(),

      builder().code(CONFIDENTIALITY_REQUIREMENT).group(ENVIRONMENTAL_GROUP).title("Confidentiality Requirement")
          .meaning("L", "Low")
          .meaning("M", "Medium")
          .meaning("H", "High")
          .meaning("ND", "Not defined")
          .build(),

      builder().code(INTEGRITY_REQUIREMENT).group(ENVIRONMENTAL_GROUP).title("Integrity Requirement")
          .meaning("L", "Low")
          .meaning("M", "Medium")
          .meaning("H", "High")
          .meaning("ND", "Not defined")
          .build(),

      builder().code(AVAILABILITY_REQUIREMENT).group(ENVIRONMENTAL_GROUP).title("Availability Requirement")
          .meaning("L", "Low")
          .meaning("M", "Medium")
          .meaning("H", "High")
          .meaning("ND", "Not defined")
          .build()
  };

  private static final Map<String, CvssMetric> metrics = createLookup(METRICS);

  public Cvss2Vector(final String value) {
    super(VERSION, value);
  }

  @Nullable
  @Override
  protected CvssMetric findMetric(final String code) {
    return metrics.get(code);
  }
}
