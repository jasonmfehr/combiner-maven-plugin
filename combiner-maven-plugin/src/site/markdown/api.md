## combiner-maven-plugin API

  Writing a pipeline stage implementation is a fairly straightforward process.  Follow the steps below 
  and you will have a completed and packaged pipeline implementation ready to go!

### 1. Create an empty maven jar project.
  To make it easier, use an archetype.  Open a command window or terminal and enter the following 
  changing the groupId and artifactId to whatever you want to use.

```
mvn archetype:generate -DgroupId=com.mycompany -DartifactId=my-pipeline-stage-impl -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### 2. Add the combiner-maven-plugin-api dependency.
  In the new pom, add the following dependency.  Be sure to [check maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jasonmfehr%22%20AND%20a%3A%22combiner-maven-plugin-api%22) for the latest version.

```
<dependency>
  <groupId>com.github.jasonmfehr</groupId>
  <artifactId>combiner-maven-plugin-api</artifactId>
  <version>LATEST</version>
</dependency>
```

### 3. Implement the pipeline stage.
  Create a new class that implements one of the four pipeline stage interfaces:
  
  1. [InputSourceReader](apidocs/com/github/jasonmfehr/combiner/input/InputSourceReader.html)
  
  2. [ResourceTransformer](apidocs/com/github/jasonmfehr/combiner/transformer/ResourceTransformer.html)
  
  3. [ResourceCombiner](apidocs/com/github/jasonmfehr/combiner/combiner/ResourceCombiner.html)

  4. [OutputSourceWriter](apidocs/com/github/jasonmfehr/combiner/output/OutputSourceWriter.html)

### 4. Build and deploy the jar to you artifact repository.
  If you are just running locally, open a command window and type:
  
```
mvn clean install
```

###5. Update the pom in your project that uses the combiner-maven-plugin.
  Add the new jar you built as a dependency within the combiner-maven-plugin configuration.  You can then 
  specify the fully qualified class name (include the package) in the correct place in the plugin configuration.

```
  <plugin>
    <groupId>com.github.jasonmfehr</groupId>
    <artifactId>combiner-maven-plugin</artifactId>
    <version>LATEST</version>
    <dependencies>
      <dependency>
        <groupId>com.mycompany</groupId>
        <artifactId>my-pipeline-stage-impl</artifactId>
        <version>LATEST</version>
      </dependency>
    </dependencies>
  </plugin>
```