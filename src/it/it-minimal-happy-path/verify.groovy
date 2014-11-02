String actual = new File(basedir.absolutePath + "/target/actual-result.txt").text

for(int i=1; i<=3; i++){
    String fileText = new File(basedir.absolutePath + "/src/main/resources/file" + i + ".txt").text
    
    if(!actual.contains(fileText)) throw new RuntimeException("text from file" + i + " not found in actual-results.txt")
}