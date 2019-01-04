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
package org.sonatype.ossindex.service.client.cache;

import java.util.Map;

import javax.annotation.Nullable;

import org.sonatype.goodies.packageurl.PackageUrl;
import org.sonatype.ossindex.service.api.componentreport.ComponentReport;

/**
 * {@link PackageUrl} to {@link ComponentReport} cache.
 *
 * @since ???
 */
public interface Cache
  extends AutoCloseable
{
  /**
   * Return cached report for given coordinates or {@literal null} if not cached.
   */
  @Nullable
  ComponentReport getIfPresent(PackageUrl coordinates);

  /**
   * Add cached reports.
   */
  void putAll(Map<PackageUrl, ComponentReport> reports);
}
