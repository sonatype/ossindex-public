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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.ossindex.service.api.cvss.CvssMetric.UNKNOWN_GROUP;

/**
 * CVSS vector.
 *
 * @since ???
 */
public abstract class CvssVector
{
  private static final Logger log = LoggerFactory.getLogger(CvssVector.class);

  private final String version;

  private final String value;

  private final Map<String, String> metrics;

  public CvssVector(final String version, final String value) {
    this.version = checkNotNull(version);
    this.value = checkNotNull(value);
    this.metrics = Collections.unmodifiableMap(parse(value));
  }

  public String getVersion() {
    return version;
  }

  public String getValue() {
    return value;
  }

  public Map<String, String> getMetrics() {
    return metrics;
  }

  private static final Splitter SLASH_SPLITTER = Splitter.on('/').trimResults();

  private static final Splitter COLON_SPLITTER = Splitter.on(':').trimResults();

  private Map<String, String> parse(final String value) {
    Map<String, String> result = new LinkedHashMap<>();

    for (String metric : SLASH_SPLITTER.split(value)) {
      List<String> parts = COLON_SPLITTER.splitToList(metric);
      result.put(parts.get(0), parts.get(1));
    }
    return result;
  }

  public List<MetricDescription> getDescriptions() {
    List<MetricDescription> result = new LinkedList<>();
    for (Map.Entry<String, String> entry : metrics.entrySet()) {
      String metric = entry.getKey();
      result.add(describe(metric, entry.getValue()));
    }
    return result;
  }

  @Nullable
  protected abstract CvssMetric findMetric(final String code);

  protected MetricDescription describe(final String metricCode, final String metricValue) {
    String group = UNKNOWN_GROUP;
    String title = metricCode;
    String meaning = metricValue;

    CvssMetric metric = findMetric(metricCode);
    if (metric != null) {
      group = metric.getGroup();
      title = metric.getTitle();
      meaning = metric.getMeaning(metricValue);

      // cope with undefined meanings
      if (meaning == null) {
        meaning = metricValue;
      }
    }

    if (metricCode.equals(title) || metricValue.equals(meaning)) {
      log.warn("Did not translate; metric: {} -> {}; meaning: {} -> {}", metricCode, title, metricValue, meaning);
    }

    return new MetricDescription(metricCode, group, title, metricValue, meaning);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("version", version)
        .add("value", value)
        .add("metrics", metrics)
        .toString();
  }

  protected static Map<String, CvssMetric> createLookup(final CvssMetric[] metrics) {
    Map<String, CvssMetric> lookup = new HashMap<>(metrics.length);
    for (CvssMetric metric : metrics) {
      CvssMetric prev = lookup.put(metric.getCode(), metric);
      if (prev != null) {
        log.warn("Duplicate metric code: {}", metric.getCode());
      }
    }
    return Collections.unmodifiableMap(lookup);
  }

  //
  // MetricDescription
  //

  public static class MetricDescription
  {
    private final String code;

    private final String group;

    private final String title;

    private final String valueCode;

    private final String value;

    public MetricDescription(final String code,
                             final String group,
                             final String title,
                             final String valueCode,
                             final String value)
    {
      this.code = code;
      this.group = group;
      this.title = title;
      this.valueCode = valueCode;
      this.value = value;
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

    public String getDisplayTitle() {
      if (title.equals(code)) {
        return title;
      }
      return String.format("%s (%s)", title, code);
    }

    public String getValueCode() {
      return valueCode;
    }

    public String getValue() {
      return value;
    }

    public String getDisplayValue() {
      if (value.equals(valueCode)) {
        return value;
      }
      return String.format("%s (%s)", value, valueCode);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("code", code)
          .add("group", group)
          .add("title", title)
          .add("valueCode", valueCode)
          .add("value", value)
          .toString();
    }
  }
}
