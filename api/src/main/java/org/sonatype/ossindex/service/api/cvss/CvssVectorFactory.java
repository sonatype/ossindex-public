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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link CvssVector} factory.
 *
 * @see Cvss2Vector
 * @see Cvss3Vector
 * @since 1.2.0
 */
public class CvssVectorFactory
{
  private static final Logger log = LoggerFactory.getLogger(CvssVectorFactory.class);

  public static CvssVector create(final String value) {
    checkNotNull(value);
    log.trace("Create: {}", value);
    if (value.startsWith(Cvss3Vector.PREAMBLE)) {
      return new Cvss3Vector(value);
    }
    return new Cvss2Vector(value);
  }
}
