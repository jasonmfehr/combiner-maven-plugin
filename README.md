# combiner-maven-plugin

[![Build Status](https://travis-ci.org/jasonmfehr/combiner-maven-plugin.svg?branch=master)](https://travis-ci.org/jasonmfehr/combiner-maven-plugin)

*TODO -- insert link to site here*

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
