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

/**
 * Tests for {@link CvssVectorFactory}.
 */
class CvssVectorFactoryTest
    extends TestSupport
{
  private void explain(final CvssVector vector) {
    log vector
    vector.descriptions.each {
      log it
    }
  }

  @Test
  void 'parse v2'() {
    CvssVectorFactory.create('AV:N/AC:M/Au:N/C:N/I:P/A:N').with { vector ->
      explain(vector)

      assert vector instanceof Cvss2Vector
      assert vector.descriptions.size() == 6

      vector.metrics.with { metrics ->
        assert metrics[Cvss2Vector.ACCESS_VECTOR] == 'N'
        assert metrics[Cvss2Vector.ACCESS_COMPLEXITY] == 'M'
        assert metrics[Cvss2Vector.AUTHENTICATION] == 'N'
        assert metrics[Cvss2Vector.CONFIDENTIALITY_IMPACT] == 'N'
        assert metrics[Cvss2Vector.INTEGRITY_IMPACT] == 'P'
        assert metrics[Cvss2Vector.AVAILABILITY_IMPACT] == 'N'
      }
    }
  }

  @Test
  void 'parse v3'() {
    CvssVectorFactory.create('CVSS:3.0/AV:N/AC:L/PR:N/UI:N/S:C/C:L/I:L/A:N').with { vector ->
      explain(vector)

      assert vector instanceof Cvss3Vector
      assert vector.descriptions.size() == 8

      vector.metrics.with { metrics ->
        assert metrics[Cvss3Vector.ATTACK_VECTOR] == 'N'
        assert metrics[Cvss3Vector.ATTACK_COMPLEXITY] == 'L'
        assert metrics[Cvss3Vector.PRIVILEGES_REQUIRED] == 'N'
        assert metrics[Cvss3Vector.USER_INTERACTION] == 'N'
        assert metrics[Cvss3Vector.SCOPE] == 'C'
        assert metrics[Cvss3Vector.CONFIDENTIALITY_IMPACT] == 'L'
        assert metrics[Cvss3Vector.INTEGRITY_IMPACT] == 'L'
        assert metrics[Cvss3Vector.AVAILABILITY_IMPACT] == 'N'
      }
    }
  }
}
