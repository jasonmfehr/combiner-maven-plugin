//Could not instantiate object with class com.jfehr.combiner.input.ThisReaderDoesNotExist

String buildLog = new File(basedir.absolutePath + "/build.log").text

if(!buildLog.contains("combine failed: Could not instantiate object with class com.jfehr.combiner.output.ThisWriterDoesNotExist")) throw new RuntimeException("integration test build failed for an error that was different than expected")
