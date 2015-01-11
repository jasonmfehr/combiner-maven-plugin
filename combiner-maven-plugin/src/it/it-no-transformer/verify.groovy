//first check that the input files were combined into the output file
String actual = new File(basedir.absolutePath + "/target/actual-result.txt").text

for(int i=1; i<=2; i++){
	String fileText = new File(basedir.absolutePath + "/src/main/resources/file" + i + ".txt").text
			
	if(!actual.contains(fileText)) throw new RuntimeException("text from file" + i + ".txt not found in actual-results.txt")
}

String buildLog = new File(basedir.absolutePath + "/build.log").text

//second check the log for the beginning and ending messages 
if(!buildLog.contains("Executing pipeline stage two - transform resources")) throw new RuntimeException("did not find transfomation stage beginning message")
if(!buildLog.contains("Completed execution of pipeline stage two - transform resources")) throw new RuntimeException("did not find transfomation stage ending message")

//third, check the log to ensure no transformers were executed
if(buildLog.contains("Executing resource transformer with class")) throw new RuntimeException("found resource transformer that was executed")
if(buildLog.contains("Executing transformer on resource")) throw new RuntimeException("found resource transformer that was executed on a resource")
if(buildLog.contains("Finished executing resource transformer with class")) throw new RuntimeException("found resource transformer that was executed")
	