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
 * CVSS version {@code 3.0} severity.
 *
 * @since ???
 */
public enum Cvss3Severity
{
  UNKNOWN("unknown"),
  NONE("None"),
  LOW("Low"),
  MEDIUM("Medium"),
  HIGH("High"),
  CRITICAL("Critical");

  private final String label;

  Cvss3Severity(final String label) {
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
  public static Cvss3Severity of(@Nullable final Float score) {
    if (score != null) {
      if (score == 0) {
        return NONE;
      }
      if (score > 0 && score < 4) {
        return LOW;
      }
      else if (score >= 4 && score < 7) {
        return MEDIUM;
      }
      else if (score >= 7 && score < 9) {
        return HIGH;
      }
      else if (score >= 9 && score <= 10) {
        return CRITICAL;
      }
    }
    return UNKNOWN;
  }
}
