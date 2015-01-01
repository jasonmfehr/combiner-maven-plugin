## Pipeline Stage Descriptions

### Input
In this phase of the pipeline, resources are read into Strings.  Currently, the plugin only supports reading input resources from the disk.

### Transform
This phase involves applying transformers to the individual Strings containing the data read from each resource.  Multiple transformers can be specified with each transformer running on each resources.  Some example transformers are a transformer that removes newline characters and a transformer that applies backslash escaping of double quotes.

### Combine
In this phase of the pipeline, all the transformed resource are combined into a single String.  In addition, the String containing the combined resources can be further manipulated to add or remove information.

### Output
In this phase of the pipeline, the combined String coming from the combine phase is outputted.  Currently, only outputing to the filesystem is supported.
