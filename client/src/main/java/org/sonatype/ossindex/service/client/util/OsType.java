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
package org.sonatype.ossindex.service.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper to detect OS type.
 *
 * @since 1.1.0
 */
public enum OsType
{
  /**
   * Mac OS
   */
  MAC("Mac"),

  /**
   * Windows.
   */
  WINDOWS("Windows"),

  /**
   * Linux.
   */
  LINUX("Linux", "LINUX"),

  /**
   * Other
   */
  OTHER;

  private static final Logger log = LoggerFactory.getLogger(OsType.class);

  private final String[] prefixes;

  OsType(final String... prefixes) {
    this.prefixes = prefixes;
  }

  /**
   * Detect OS type.
   */
  public static OsType detect() {
    String osName = System.getProperty("os.name");
    log.trace("OS name: {}", osName);

    for (OsType type : OsType.values()) {
      for (String prefix : type.prefixes) {
        if (osName.startsWith(prefix)) {
          return type;
        }
      }
    }
    return OTHER;
  }
}
