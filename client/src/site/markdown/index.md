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
# About

Provides a simple Java *client* to communicate with [Sonatype OSS Index](https://ossindex.sonatype.org/).

## Transports

A few options for different *transports* are provided.

### HttpUrlConnectionTransport

The [HttpUrlConnectionTransport](apidocs/org/sonatype/ossindex/service/client/transport/HttpUrlConnectionTransport.html)
makes use of standard JRE `HttpUrlConnection`.

### HttpClientTransport

The [HttpClientTransport](apidocs/org/sonatype/ossindex/service/client/transport/HttpClientTransport.html) makes use
of [Apache HttpClient](https://hc.apache.org/httpcomponents-client-ga/).

## Marshalling

The default marshalling library used is [Google Gson](https://github.com/google/gson).
