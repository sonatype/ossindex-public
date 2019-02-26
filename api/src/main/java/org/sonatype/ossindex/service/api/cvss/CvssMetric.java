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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * CVSS metric.
 *
 * @since ???
 */
public class CvssMetric
{
  private static final Logger log = LoggerFactory.getLogger(CvssMetric.class);

  public static final String UNKNOWN_GROUP = "unknown";

  public static final String BASE_GROUP = "Base";

  public static final String TEMPORAL_GROUP = "Temporal";

  public static final String ENVIRONMENTAL_GROUP = "Environmental";

  private final String code;

  private final String group;

  private final String title;

  private final Map<String, String> meanings;

  public CvssMetric(final String code, final String group, final String title, final Map<String, String> meanings) {
    this.code = checkNotNull(code);
    this.group = checkNotNull(group);
    this.title = checkNotNull(title);
    this.meanings = Collections.unmodifiableMap(checkNotNull(meanings));
  }

  public String getCode() {
    return code;
  }

  public String getGroup() {
    return group;
  }

  public String getTitle() {
    return title;
  }

  public Map<String, String> getMeanings() {
    return meanings;
  }

  @Nullable
  public String getMeaning(final String value) {
    return meanings.get(value);
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("code", code)
        .add("group", group)
        .add("title", title)
        .toString();
  }

  //
  // Builder
  //

  public static class Builder
  {
    private String code;

    private String group;

    private String title;

    private Map<String, String> meanings = new HashMap<>();

    public Builder from(final CvssMetric metric) {
      this.code = metric.code;
      this.group = metric.group;
      this.title = metric.title;
      this.meanings.putAll(metric.meanings);
      return this;
    }

    public Builder code(final String code) {
      this.code = code;
      return this;
    }

    public Builder group(final String group) {
      this.group = group;
      return this;
    }

    public Builder title(final String title) {
      this.title = title;
      return this;
    }

    public Builder meaning(final String code, final String value) {
      String prev = meanings.put(code, value);
      if (prev != null) {
        log.warn("Replacing metric meaning code: {} with: {}; was: {}", code, value, prev);
      }
      return this;
    }

    public CvssMetric build() {
      return new CvssMetric(code, group, title, meanings);
    }
  }
}
