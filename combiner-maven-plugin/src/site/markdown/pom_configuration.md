## POM Configuration

### Introduction
  The [combine](combine-mojo.html "combine goal") goal takes a single configuration element.  
  The `combination` element defines a complete set of pipeline stage implementations that, taken 
  together, will result in the combining of a set of inputs into a single output.  How this combining 
  is achieved is determined by what pipeline stage implementations are selected and what parameters 
  are specified.  Let's break it down section by section.  There is a complete sample pom at the end.

### Input Stage

  The input stage is where all the resources are read.

##### inputSourceReader
  The `inputSourceReader` element defines what input stage implementation will be used.  
  See the [pipeline stage construction](pipeline_stage_construction.html "pipeline stage construction") page 
  for details on how the value specified in this parameter translates into a Java object used to read input 
  resources.

##### inputResources
  The `inputResources` section defines a list of resources that will be pulled in and combined.  The 
  generic term "resources" is used instead of "file" because the plugin is intended to pull data from 
  any source, not just filesystem files.  That being said, the only input stage implementation that 
  currently exists is a file reader.  However, should it be necessary to pull from another source, 
  say a socket or RSS feed, then it is trivial to write and wire up a new input stage implementation.  
  Well, the actual work of getting data from the socket or RSS feed will not be trivial, but the rest 
  will.  See the [combiner-maven-plugin api](../combiner-maven-plugin-api/index.html "plugin api") 
  documentation for information on writing a custom input stage implementation.  See the 
  [pipeline stage construction](pipeline_stage_construction.html "pipeline stage construction") page 
  for details on how to wire in the custom implementation.
  
  In the case of the `FileInputSourceReader`, both the `includes` and `excludes` sections take ant style expressions 
  to define the files on the filesystem that will be combined together
  
##### POM Configuration
```
<inputSourceReader>InputReader</inputSourceReader>
<inputResources>
  <includes>
    <include>src/main/resources/file*.txt</include>
  </includes>
  <excludes>
    <exclude>src/main/resources/file-exclude-me.txt</exclude>
  </excludes>
</inputResources>
```

### Transform Stage

  The transform stage is an optional stage where the resources that were read in can be manipulated 
  in preparation for being combined.  For example, it may be necessary to remove all newlines or to 
  apply escaping.
  
##### transformers
  The `transformers` section is a list where all the transformer stage implementations are listed.  
  This section is the only available configuration for the transformer stage.  See the 
  [combiner-maven-plugin api](../combiner-maven-plugin-api/index.html "plugin api") documentation for 
  information on writing a custom transformer stage implementation.  See the 
  [pipeline stage construction (pipeline_stage_construction.html "pipeline stage construction") page 
  for details on how to wire in the custom implementation. 
  
##### POM Configuration
```
<transformers>
  <transformer>Transformer1</transformer>
  <transformer>Transformer2</transformer>
</transformers>
```

### Combine Stage

  The combine stage is when all the resources are manipulated into a single resource.  For example, 
  the appending combiner simply appends each resource together one after the other.  See the 
  [combiner-maven-plugin api](../combiner-maven-plugin-api/index.html "plugin api") documentation for 
  information on writing a custom combiner stage implementation.  See the 
  [pipeline stage construction (pipeline_stage_construction.html "pipeline stage construction") page 
  for details on how to wire in the custom implementation. 

##### combiner
  The `combiner` element is a single entry listing the combiner implementation to use.  
  
##### POM Configuration
```
<combiner>Combiner</combiner>
```

### Output Stage

  The output stage is where the single combined resource is written.  The destination resource 
  can be whatever type of resource the output stage implementation uses.  Typically this will be 
  a file on the filesystem but could also be a ssh or ftp location.

##### outputSourceWriter
  The `outputSourceWriter` element defines what output stage implementation will be used.  
  See the [pipeline stage construction](pipeline_stage_construction.html "pipeline stage construction") page 
  for details on how the value specified in this parameter translates into a Java object used to write output 
  resources.

##### outputDestination
  The `outputDestination` element defines the location where the combined resource will be written.  The value 
  of this element depends on what output stage implementation was selected in the `outputSourceWriter` element.
  
##### POM Configuration
```
<outputSourceWriter>OutputWriter</outputSourceWriter>
<outputDestination>output-file.txt</outputDestination>
```

### Other Settings

  The pom configuration for the four stages is very minimal.  There may be other configuration necessary for 
  the pipeline stage implementations.  For example, if resources are written to a destination via ftp, it 
  may be necessary to provide a username and password.  The way to pass additional configuration information 
  is via the `settings` section of the pom configuration.

##### POM Configuration
```
 <settings>
  <key1>value1</key1>
  <key2>value2</key2>
</settings>
```
  
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