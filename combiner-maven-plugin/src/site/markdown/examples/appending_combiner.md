## Appending Combiner

  This example shows usage of the [appending combiner](apidocs/com/github/jasonmfehr/combiner/combiner/AppendingCombiner.html) to combine resources together into a single file.  [Check maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jasonmfehr%22%20AND%20a%3A%22combiner-maven-plugin-api%22) for the latest version of the plugin.
  
  In this example, all files with a name matching file*.txt in the src/main/resources are read.  All the newlines in these files are stripped out and the are files concatenated together with two newlines following the contents of each resource (so the output file will have two newlines at the end of it).  The combination is then written out to the combined-files.txt file in the build directory (target).  See below for the contents of each file

```
<plugin>
  <groupId>com.github.jasonmfehr</groupId>
  <artifactId>combiner-maven-plugin</artifactId>
  <version>LATEST</version>
  <configuration>
    <combinations>
      <combination>
        <id>append-files</id>
        <inputResources>
          <includes>
            <include>src/main/resources/file*.txt</include>
          </includes>
        </inputResources>
        <transformers>
          <transformer>StripNewlines</transformer>
        </transformers>
        <combiner>AppendingCombiner</combiner>
        <outputDestination>combined-files.txt</outputDestination>
        <settings>
          <appendingCombinerNewlines>2</appendingCombinerNewlines>
        </settings>
      </combination>
    </combinations>
  </configuration>
</plugin>
```

### Example Files

  In these files a \n denotes a line break, not the literal characters.  Also, the order that the resources are actually combined by the plugin may not be the order reflected here.
  
#### ${basedir}/src/main/resources/file-foo.txt
```
This is the text of file foo.  Let's\n 
\n
insert some\n
\n
completely random line\n
\n
breaks.\n
\n
\n
```

#### ${basedir}/src/main/resources/file-bar.txt
```
This file is file bar.  It is all on a single line with no line break at the end
```

#### target/combined-files.txt
```
This is the text of file foo.  Let's insert some completely random line breaks.\n
\n
This file is file bar.  It is all on a single line with no line break at the end\n
\n
```