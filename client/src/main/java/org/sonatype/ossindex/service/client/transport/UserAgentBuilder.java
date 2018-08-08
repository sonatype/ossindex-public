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
package org.sonatype.ossindex.service.client.transport;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Joiner;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * {@code User-Agent} builder.
 *
 * @since ???
 */
public class UserAgentBuilder
{
  /**
   * User-agent product.
   *
   * Consists of a name and optional version and comment segments.
   */
  public static class Product
  {
    private final String name;

    @Nullable
    private final String version;

    private String separator = "; ";

    @Nullable
    private List<String> comments;

    public Product(final String name, @Nullable final String version) {
      this.name = checkNotNull(name);
      this.version = version;
    }

    public Product(final String name) {
      this(name, null);
    }

    public Product seperator(final String separator) {
      this.separator = checkNotNull(separator);
      return this;
    }

    public Product comment(final String... values) {
      if (comments == null) {
        comments = new LinkedList<>();
      }
      comments.addAll(Arrays.asList(values));
      return this;
    }
  }

  private final List<Product> products = new LinkedList<>();

  public UserAgentBuilder product(final Product product) {
    products.add(product);
    return this;
  }

  public String build() {
    checkState(!products.isEmpty(), "At least one product must be specified");
    StringBuilder buff = new StringBuilder();

    Iterator<Product> iter = products.iterator();
    while (iter.hasNext()) {
      Product product = iter.next();
      buff.append(product.name);

      if (product.version != null) {
        buff.append('/').append(product.version);
      }

      if (product.comments != null) {
        buff.append(' ').append('(');
        buff.append(Joiner.on(product.separator).join(product.comments));
        buff.append(')');
      }

      if (iter.hasNext()) {
        buff.append(' ');
      }
    }

    return buff.toString();
  }
}
