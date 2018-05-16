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
package org.sonatype.ossindex.service.client.internal

import org.sonatype.goodies.testsupport.TestSupport

import org.junit.Before
import org.junit.Test

/**
 * Tests for {@link VersionSupplier}.
 */
class VersionSupplierTest
  extends TestSupport
{
  private VersionSupplier underTest

  @Before
  void setUp() {
    underTest = new VersionSupplier()
  }

  @Test
  void 'ensure version loaded'() {
    def version = underTest.get()
    assert version != null
    assert version.version != Version.UNKNOWN
  }
}
