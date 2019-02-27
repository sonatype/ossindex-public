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

import javax.annotation.Nullable;

/**
 * CVSS version {@code 2.0} severity.
 *
 * @since 1.2.0
 */
public enum Cvss2Severity
{
  UNKNOWN("unknown"),
  LOW("Low"),
  MEDIUM("Medium"),
  HIGH("High");

  private final String label;

  Cvss2Severity(final String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }

  /**
   * Return severity for given score.
   *
   * If {@literal null} or out of bounds returns {@link #UNKNOWN}.
   */
  public static Cvss2Severity of(@Nullable final Float score) {
    if (score != null) {
      if (score >= 0 && score < 4) {
        return LOW;
      }
      else if (score >= 4 && score < 7) {
        return MEDIUM;
      }
      else if (score >= 7 && score <= 10) {
        return HIGH;
      }
    }
    return UNKNOWN;
  }
}
