---
title: Client
subtitle: Java REST client
glyph: fas fa-truck

draft: false

menu:
  topnav:
    identifier: Client
    weight: 10

categories:
  - java
  - client
tags:
  - ossindex-service-client
---

Provides a simple Java *client* to communicate with [Sonatype OSS Index](https://ossindex.sonatype.org/).

## Dependency

To use from [Apache Maven](https://maven.apache.org) include dependency:

{{< maven-dependency module="ossindex-service-client" >}}

## Transports

A few options for different *transports* are provided.

### HttpUrlConnectionTransport

The [HttpUrlConnectionTransport](../maven/apidocs/org/sonatype/ossindex/service/client/transport/HttpUrlConnectionTransport.html)
makes use of standard JRE `HttpUrlConnection`.

### HttpClientTransport

The [HttpClientTransport](../maven/apidocs/org/sonatype/ossindex/service/client/transport/HttpClientTransport.html) makes use
of [Apache HttpClient](https://hc.apache.org/httpcomponents-client-ga/).

## Marshalling

The default marshalling library used is [Google Gson](https://github.com/google/gson).
