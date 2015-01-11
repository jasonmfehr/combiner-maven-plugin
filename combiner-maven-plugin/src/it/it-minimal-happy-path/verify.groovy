String actual = new File(basedir.absolutePath + "/target/actual-result.txt").text

if(actual.contains("\r|\n")) throw new RuntimeException("expected zero newline characters but found at least one")

for(int i=1; i<=3; i++){
    String fileText = new File(basedir.absolutePath + "/src/main/resources/file" + i + ".txt").text.replaceAll("\r|\n", "")
    
    if(!actual.contains(fileText)) throw new RuntimeException("text from file" + i + ".txt not found in actual-results.txt")
}