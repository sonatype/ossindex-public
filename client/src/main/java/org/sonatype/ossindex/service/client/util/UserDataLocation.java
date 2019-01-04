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
package org.sonatype.ossindex.service.client.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Helper to return user data base directory location specific to {@link OsType}.
 *
 * @since ???
 */
public class UserDataLocation
    implements Supplier<Path>
{
  private static final Logger log = LoggerFactory.getLogger(UserDataLocation.class);

  private final String vendor;

  private final String product;

  public UserDataLocation(final String vendor, final String product) {
    this.vendor = checkNotNull(vendor);
    this.product = checkNotNull(product);
  }

  @Override
  public Path get() {
    OsType osType = osType();
    log.debug("OS type: {}", osType);

    switch (osType) {
      case MAC:
        return mac();

      case WINDOWS:
        return windows();

      case LINUX:
        return linux();

      case OTHER:
      default:
        return other();
    }
  }

  @VisibleForTesting
  protected OsType osType() {
    return OsType.detect();
  }

  @VisibleForTesting
  @Nullable
  protected String getenv(final String name) {
    return System.getenv(name);
  }

  @VisibleForTesting
  protected Path userHome() {
    return Paths.get(System.getProperty("user.home"));
  }

  /**
   * Return user-data location for Mac OS.
   */
  private Path mac() {
    return userHome().resolve(Paths.get("Library", "Application Support", vendor, product));
  }

  /**
   * Return user-data location for Linux.
   */
  private Path linux() {
    String dirName = String.format("%s-%s", vendor, product).toLowerCase(Locale.ENGLISH);
    return userHome().resolve(Paths.get(".config", dirName));
  }

  /**
   * Return fallback user-data location for others.
   */
  private Path other() {
    String dirName = String.format(".%s-%s", vendor, product).toLowerCase(Locale.ENGLISH);
    return userHome().resolve(dirName);
  }

  //
  // Windows
  //

  /**
   * Environment variable for modern (vista+) Windows user-data location base-directory.
   */
  @VisibleForTesting
  static final String LOCALAPPDATA = "LOCALAPPDATA";

  /**
   * Environment variable for legacy (<=XP) Windows user-data location base-directory.
   */
  @VisibleForTesting
  static final String APPDATA = "APPDATA";

  @VisibleForTesting
  static final String USERPROFILE = "USERPROFILE";

  @Nullable
  private Path windowsBaseDir() {
    // modern windows; vista,7+
    String dirName = getenv(LOCALAPPDATA);
    if (dirName != null) {
      return Paths.get(dirName);
    }

    // windows xp and older?
    dirName = getenv(APPDATA);
    if (dirName != null) {
      return Paths.get(dirName);
    }

    // before giving up; try what APPDATA should be; and if it exists use it
    dirName = getenv(USERPROFILE);
    if (dirName != null) {
      Path path = Paths.get(dirName, "Local Settings", "Application Data");
      if (Files.exists(path)) {
        return path;
      }
    }

    return null;
  }

  /**
   * Return user-data location for Windows.
   */
  private Path windows() {
    Path basedir = windowsBaseDir();
    if (basedir != null) {
      return basedir.resolve(Paths.get(vendor, product));
    }

    // fallback
    log.warn("Unable to determine user-data location for Windows");
    return other();
  }
}
