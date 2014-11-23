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

### Pipeline Stage Construction
The four stages of the plugin's pipeline are implemented with Java classes that implement specific interfaces for each stage.  These four Java interfaces are listed below and can be found in the combiner-maven-plugin-api dependency in maven central.

Within the configuration of each combination, there are places to specify Strings or Lists of Strings detailing which implementation classes to user for each stage of the pipeline.  The input and transformer stages both allow Lists while the combine and output stages only allow a String.  The following strategies are how the plugin attempts to locate the Java classes that match the values specified in the combination configuration:

1. The fully qualified class name is determined.  If only the classname is specified (i.e. "FileInputSourceReader", then the plugin prepends the default package (i.e. com.github.jasonmfehr.combiner.input.FileInputSourceReader).  See the table below for the default package for each stage.  If a fully qualified class was specified (i.e. com.mypackage.myinputreader.StreamInputReader), then that exact value is used as the fully qualified class name.
2. The plexus container is examined for any components that have a role matching the fully qualified class name.  If a component is found, it is checked to make sure it implements the required Java interface.  If it does, then that component is returned.  If it does not, then the plugin moves on to the next strategy.
3. The class with the fully qualified name is instantiated using a default constructor.  If the instantiation is successful, the object is checked to make sure it implements the required interface.  If it does, that object is returned.  If it does not, an exception is thrown.

| Pipeline Stage | Java Interface | Default Package |
| -------------- | -------------- | --------------- |
| Input | com.github.jasonmfehr.combiner.input.InputSourceReader | com.github.jasonmfehr.combiner.input |
| Transform | com.github.jasonmfehr.combiner.transformer.ResourceTransformer | com.github.jasonmfehr.combiner.transformer |
| Combine | com.github.jasonmfehr.combiner.combiner.ResourceCombiner | com.github.jasonmfehr.combiner.combiner |
| Output | com.github.jasonmfehr.combiner.output.OutputSourceWriter | com.github.jasonmfehr.combiner.output |

### Sample pom showing all available configuration options:
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
### Usage Details
| Name | Type | Description |
|:-----|:---- | ----------- |
| combinations | List | Defines a set of related input resources, transformers, combiner, and output destination/writer. |
| combiner | String | Java class implementation of the combiner pipeline stage used when combining all resources together.  The class specified here must implement the Java interface com.github.jasonmfehr.combiner.combiner.ResourceCombiner from the combiner-maven-plugin-api dependency.  See the above section on pipeline stage construction for information on what values can be specified in this parameter. |
