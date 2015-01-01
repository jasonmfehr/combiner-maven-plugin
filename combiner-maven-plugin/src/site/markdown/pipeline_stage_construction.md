## Pipeline Stage Construction

#### Introduction
The four stages of the plugin's pipeline are implemented with Java classes that implement specific interfaces for each stage.  These four Java interfaces are listed below and can be found in the combiner-maven-plugin-api dependency in maven central.

#### How Objects Are Constructed For Each Stage
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