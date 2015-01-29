//No input resources were found for combination with id

String buildLog = new File(basedir.absolutePath + "/build.log").text

if(!buildLog.contains("No input resources were found for combination with id no-input-resources")) throw new RuntimeException("integration test build failed for an error that was different than expected")
