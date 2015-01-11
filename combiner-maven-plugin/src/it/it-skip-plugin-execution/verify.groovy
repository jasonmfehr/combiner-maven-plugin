String buildLog = new File(basedir.absolutePath + "/build.log").text

if(!buildLog.contains("skipping combiner-maven-plugin execution")) throw new RuntimeException("plugin execution was not skipped")