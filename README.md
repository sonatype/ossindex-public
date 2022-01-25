<!--

    Copyright (c) 2018-present Sonatype, Inc. All rights reserved.

    This program is licensed to you under the Apache License Version 2.0,
    and you may not use this file except in compliance with the Apache License Version 2.0.
    You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.

    Unless required by applicable law or agreed to in writing,
    software distributed under the Apache License Version 2.0 is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.

-->
# Sonatype OSS Index - Public

![license](https://img.shields.io/github/license/sonatype/ossindex-public.svg)

![maven-central](https://img.shields.io/maven-central/v/org.sonatype.ossindex/ossindex-service.svg)

Provides API and clients for [Sonatype OSS Index](https://ossindex.sonatype.org/).

See [Javadocs](https://sonatype.github.io/ossindex-public/) for API and client reference.

## Using the client

To add a dependency on the OSS Index client, use the following:

```xml
<dependency>
  <groupId>org.sonatype.ossindex</groupId>
  <artifactId>ossindex-service-client</artifactId>
  <version>1.8.1</version>
</dependency>
```

Two options for transports are provided:

* [HttpUrlConnectionTransport](https://sonatype.github.io/ossindex-public/org/sonatype/ossindex/service/client/transport/HttpUrlConnectionTransport.html) makes use of standard JRE `HttpUrlConnection`
* [HttpClientTransport](https://sonatype.github.io/ossindex-public/org/sonatype/ossindex/service/client/transport/HttpClientTransport.html) makes use of [Apache HttpClient](https://hc.apache.org/httpcomponents-client-ga/)

## Building

### Requirements

* [Apache Maven](https://maven.apache.org/) 3.3+ (prefer to use included `mvnw`)
* JDK 7+ (10 is **NOT** supported)

### Build

    ./mvnw clean install

### Publish the docs

Checkout the release tag first, then:

1. To do a dry run:

        ./mvnw clean javadoc:aggregate scm-publish:publish-scm -Pdocs -Dscmpublish.dryRun=true

1. To publish:

        ./mvnw clean javadoc:aggregate scm-publish:publish-scm -Pdocs
