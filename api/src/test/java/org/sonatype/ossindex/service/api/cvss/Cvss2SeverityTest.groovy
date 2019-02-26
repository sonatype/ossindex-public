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
package org.sonatype.ossindex.service.api.cvss

import org.sonatype.goodies.testsupport.TestSupport

import org.junit.Test

import static org.sonatype.ossindex.service.api.cvss.Cvss2Severity.HIGH
import static org.sonatype.ossindex.service.api.cvss.Cvss2Severity.LOW
import static org.sonatype.ossindex.service.api.cvss.Cvss2Severity.MEDIUM
import static org.sonatype.ossindex.service.api.cvss.Cvss2Severity.UNKNOWN
import static org.sonatype.ossindex.service.api.cvss.Cvss2Severity.of

/**
 * {@link Cvss2Severity} tests.
 */
class Cvss2SeverityTest
    extends TestSupport
{
  @Test
  void 'sanity'() {
    of(null) == UNKNOWN
    of(-1) == UNKNOWN
    of(11) == UNKNOWN
    of(0) == LOW
    of(1) == LOW
    of(4) == MEDIUM
    of(4.1) == MEDIUM
    of(7) == HIGH
    of(7.1) == HIGH
  }
}
