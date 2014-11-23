# combiner-maven-plugin

[![Build Status](https://travis-ci.org/jasonmfehr/combiner-maven-plugin.svg?branch=master)](https://travis-ci.org/jasonmfehr/combiner-maven-plugin)

## Introduction
The combiner-maven-plugin is designed for those situations where, for whatever reason, the combining of resources is neccessary.  The term "resources" is used to indicate that there may be any number of input sources (i.e. disk, socket, etc).  Currently, the combiner-maven-plugin only supports resources in the form of files that can be accessed through the filesystem.  However, extending the plugin is very easy and thus creating a new input reader for non-filesystem resources would be easy (see below section on customization).

The plugin consists of a four stage pipeline:

1. Input
2. Transform
3. Combine
4. Output

### Input
In this phase of the pipeline, resources are read into Strings.  Currently, the plugin only supports reading input resources from the disk.

### Transform
This phase involves applying transformers to the individual Strings containing the data read from each resource.  Multiple transformers can be specified with each transformer running on each resources.  Some example transformers are a transformer that removes newline characters and a transformer that applies backslash escaping of double quotes.

### Combine
In this phase of the pipeline, all the transformed resource are combined into a single String.  In addition, the String containing the combined resources can be further manipulated to add or remove information.

### Output
In this phase of the pipeline, the combined String coming from the combine phase is outputted.  Currently, only outputing to the filesystem is supported.

## Usage
The combiner-maven-plugin only has one goal.  This goal is named "combine" binds by default to the process-sources phase.

Sample pom showing all available configuration options:
```
<plugin>
  <groupId>com.github.jasonmfehr</groupId>
  <artifactId>combiner-maven-plugin</artifactId>
  <version>latest</version>
  <configuration>
    <skip>false</skip>
    <encoding>UTF-8</encoding>
    <combinations>
      <combination>
        <id>append-files</id>
        <inputResources>
          <includes>
            <include>src/main/resources/file*.txt</include>
          </includes>
          <excludes>
            <exclude>src/main/resources/file-exclude-me.txt</exclude>
          </excludes>
        </inputResources>
        <inputSourceReader>InputReader</inputSourceReader>
        <transformers>
          <transformer>Transformer1</transformer>
          <transformer>Transformer2</transformer>
        </transformers>
        <combiner>Combiner</combiner>
        <outputSourceWriter>OutputWriter</outputSourceWriter>
        <outputDestination>output-file.txt</outputDestination>
        <settings>
          <key1>value1</key1>
          <key2>value2</key2>
        </settings>
      </combination>
    </combinations>
  </configuration>
  <executions>
    <execution>
      <id>combiner</id>
      <goals>
        <goal>combine</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```
