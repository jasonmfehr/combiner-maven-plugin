## Pipeline Stage Implementations

  The following implementations of the four pipeline stages are available either within the combiner-maven-plugin 
  by default or from a public repository.  Implementations in the specified default package only need the class name 
  specified in the pom configuration.

### Input Stage
Default Package: ```com.github.jasonmfehr.combiner.input```

| Name | Package | Description | Settings |
|:-----|:----|:---- | ----------- |
| FileInputSourceReader | com.github.jasonmfehr.combiner.input | Reads resources from the filesystem.  The ```includes``` and ```excludes``` sections of the pom take any style expressions. | none

### Transform Stage
Default Package: ```com.github.jasonmfehr.combiner.transform```

| Name | Package | Description | Settings |
|:-----|:----|:---- | ----------- |
| EscapeDoubleQuotes | com.github.jasonmfehr.combiner.transformer | Inserts a backslash before each double quote | none
| StripNewlines | com.github.jasonmfehr.combiner.input | Removes all carriage return and newline characters. | none

### Combine Stage
Default Package: ```com.github.jasonmfehr.combiner.combiner```

| Name | Package | Description | Settings |
|:-----|:----|:---- | ----------- |
| AppendingCombiner | com.github.jasonmfehr.combiner.combiner | Concatenates each transformed resources one after the other in no particular order | The ```appendingCombinerNewlines``` setting can be used to specify the number of  newline characters that are inserted between each resource.  If this setting is not specified, zero newlines are inserted between resources.
| JSObjectCombiner | com.github.jasonmfehr.combiner.combiner | Combines each resource into a javascript object with a property name matching the resource name and a value containing the contents of the resource. | The ```jsObjectName``` setting specifies the name of the javascript object that is outputted.

### Output Stage
Default Package: ```com.github.jasonmfehr.combiner.output```

| Name | Package | Description | Settings |
|:-----|:----|:---- | ----------- |
| FileOutputSourceWriter | com.github.jasonmfehr.combiner.output | Writes the combined resource to a file on the filesystem.  The ```outputDestination``` setting is a location on the filesystem where the combined resource will be written. | none
