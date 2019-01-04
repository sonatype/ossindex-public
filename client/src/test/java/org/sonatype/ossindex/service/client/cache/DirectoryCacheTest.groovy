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
package org.sonatype.ossindex.service.client.cache

import org.sonatype.goodies.packageurl.PackageUrl
import org.sonatype.goodies.testsupport.TestSupport
import org.sonatype.ossindex.service.api.componentreport.ComponentReport
import org.sonatype.ossindex.service.api.componentreport.ComponentReportVulnerability
import org.sonatype.ossindex.service.client.marshal.GsonMarshaller

import com.google.common.io.Files
import org.joda.time.Duration
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * {@link DirectoryCache} tests.
 */
class DirectoryCacheTest
    extends TestSupport
{
  File baseDir

  DirectoryCache underTest

  @Before
  void setUp() {
    baseDir = util.createTempDir('cache-')
    underTest = new DirectoryCache(new GsonMarshaller(),
        new DirectoryCache.Configuration(
            baseDir: baseDir.toPath(),
            expireAfter: Duration.standardSeconds(1)
        )
    )
  }

  @After
  void tearDown() {
    underTest.close()
    underTest = null
    baseDir = null
  }

  private void displayDirectoryTree(final File directory) {
    for (File file : Files.fileTreeTraverser().breadthFirstTraversal(directory)) {
      log file
    }
  }

  @Test
  void 'add entry'() {
    def coordinates = PackageUrl.parse('pkg:mock/foo/bar@baz')

    def report = new ComponentReport(
        coordinates: coordinates,
        description: 'mock 1',
        reference: URI.create('http://ossindex.example.com/mock-1'),
        vulnerabilities: [
            new ComponentReportVulnerability(
                id: 'mock-1',
                title: 'Mock 1',
                description: 'mock 1',
                cvssScore: 1.0,
                cvssVector: 'CVSS:3.0/AV:N/AC:L/PR:N/UI:N/S:C/C:L/I:L/A:N',
                cwe: 'foo-123',
                cve: null,
                reference: URI.create('http://ossindex.example.com/vuln/mock-1'),
                versionRanges: [
                    '(,)'
                ]
            )
        ]
    )

    underTest.getIfPresent(coordinates).with {
      assert it == null
    }

    underTest.putAll([(coordinates): report])

    underTest.getIfPresent(coordinates).with {
      assert it != null
      assert it == report
    }

    displayDirectoryTree(baseDir)

    // FIXME: this is likely to break periodically; consider revising test
    Thread.sleep(2000)

    underTest.getIfPresent(coordinates).with {
      assert it == null
    }

    displayDirectoryTree(baseDir)
  }
}
