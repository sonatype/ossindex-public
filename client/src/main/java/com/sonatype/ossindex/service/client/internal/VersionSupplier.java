/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.ossindex.service.client.internal;

import com.google.common.base.Supplier;

/**
 * {@link Version} supplier.
 *
 * @since ???
 */
public class VersionSupplier
    implements Supplier<Version>
{
  private static volatile Version instance;

  @Override
  public Version get() {
    if (instance == null) {
      instance = new Version(VersionSupplier.class);
    }
    return instance;
  }
}
