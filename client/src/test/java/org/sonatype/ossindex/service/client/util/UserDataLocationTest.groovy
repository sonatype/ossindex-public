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
package org.sonatype.ossindex.service.client.util

import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicReference

import org.sonatype.goodies.testsupport.TestSupport

import org.junit.Before
import org.junit.Test

/**
 * {@link UserDataLocation} tests.
 */
class UserDataLocationTest
    extends TestSupport
{
  AtomicReference<OsType> osType

  Map<String,String> env

  UserDataLocation underTest

  @Before
  void setUp() {
    osType = new AtomicReference()
    env = [:]
    underTest = new UserDataLocation('Foo', 'Bar') {
      @Override
      protected OsType osType() {
        return osType.get()
      }

      @Override
      protected Path userHome() {
        return Paths.get('USER_HOME')
      }

      @Override
      protected String getenv(final String name) {
        return env.get(name)
      }
    }
  }

  private static String path(final String first, String... more) {
    return Paths.get(first, more).toString()
  }

  // NOTE: this isn't quite correct but serves to test the resolution w/o platform-specific path separator complexity.

  @Test
  void 'mac location'() {
    osType.set(OsType.MAC)
    underTest.get().with {
      assert it.toString() == path('USER_HOME', 'Library', 'Application Support', 'Foo', 'Bar')
    }
  }

  @Test
  void 'windows location; modern'() {
    osType.set(OsType.WINDOWS)
    env.put(UserDataLocation.LOCALAPPDATA, 'WINDOWS_BASEDIR')
    underTest.get().with {
      assert it.toString() == path('WINDOWS_BASEDIR', 'Foo', 'Bar')
    }
  }

  @Test
  void 'windows location; legacy'() {
    osType.set(OsType.WINDOWS)
    env.put(UserDataLocation.APPDATA, 'WINDOWS_BASEDIR')
    underTest.get().with {
      assert it.toString() == path('WINDOWS_BASEDIR', 'Foo', 'Bar')
    }
  }

  @Test
  void 'windows location; fallback'() {
    osType.set(OsType.WINDOWS)
    underTest.get().with {
      assert it.toString() == path('USER_HOME','.foo-bar')
    }
  }

  @Test
  void 'linux location'() {
    osType.set(OsType.LINUX)
    underTest.get().with {
      assert it.toString() == path('USER_HOME', '.config', 'foo-bar')
    }
  }

  @Test
  void 'other location'() {
    osType.set(OsType.OTHER)
    underTest.get().with {
      assert it.toString() == path('USER_HOME','.foo-bar')
    }
  }
}
