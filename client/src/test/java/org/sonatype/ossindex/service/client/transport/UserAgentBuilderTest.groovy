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
package org.sonatype.ossindex.service.client.transport

import org.sonatype.goodies.testsupport.TestSupport
import org.sonatype.ossindex.service.client.transport.UserAgentBuilder.Product

import org.junit.Test

import static org.junit.Assert.fail

/**
 * Tests for {@link UserAgentBuilder}.
 */
class UserAgentBuilderTest
    extends TestSupport
{
  @Test
  void 'at least one product required'() {
    try {
      new UserAgentBuilder().build()
      fail()
    }
    catch (IllegalStateException e) {
      log e // expected
    }
  }

  @Test
  void 'product requires name'() {
    try {
      new UserAgentBuilder().product(
          new Product(null, '1')
      ).build()
      fail()
    }
    catch (NullPointerException e) {
      log e // expected
    }
  }

  @Test
  void 'simple product name'() {
    new UserAgentBuilder().product(
        new Product('foo')
    ).build().with {
      log it
      assert it == 'foo'
    }
  }

  @Test
  void 'simple product name and version'() {
    new UserAgentBuilder().product(
        new Product('foo', '1')
    ).build().with {
      log it
      assert it == 'foo/1'
    }
  }

  @Test
  void 'simple product name and version and comment'() {
    new UserAgentBuilder().product(
        new Product('foo', '1').comment('a', 'b', 'c')
    ).build().with {
      log it
      assert it == 'foo/1 (a; b; c)'
    }
  }

  @Test
  void 'complex comment'() {
    new UserAgentBuilder().product(
        new Product('foo', '1')
            .comment('a', 'b', 'c'))
        .build().with {
      log it
      assert it == 'foo/1 (a; b; c)'
    }
  }

  @Test
  void 'complex products'() {
    new UserAgentBuilder()
        .product(new Product('foo', '1').comment('a', 'b', 'c'))
        .product(new Product('bar', '2').comment('1', '2', '3'))
        .build().with {
      log it
      assert it == 'foo/1 (a; b; c) bar/2 (1; 2; 3)'
    }
  }
}
