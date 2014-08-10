mvn clean
mvn package
#First we copy the things from hadoop jar command
echo mvn exec:java  -Dexec.mainClass="com.codeboy.hadoop.tools.MRJarRunner" -Dexec.classpathScope="compile"  -Dexec.args="./target/wordcount_cli-1.0.jar  com.codeboy.hadoop.wordcount.WordCount hdfs://localhost:9000/csv/golfnew.csv /tmp/myout"
mvn exec:java  -Dexec.mainClass="com.codeboy.hadoop.tools.MRJarRunner" -Dexec.classpathScope="compile"  -Dexec.args="./target/wordcount_cli-1.0.jar  com.codeboy.hadoop.wordcount.WordCount hdfs://localhost:9000/csv/golfnew.csv /tmp/myout"
 
#second we can do better!since we have the jars in the core-site.xml

mvn exec:java  -Dexec.mainClass="com.codeboy.hadoop.wordcount.WordCount"   -Dexec.args="hdfs://localhost:9000/csv/golfnew.csv /tmp/myout"

 