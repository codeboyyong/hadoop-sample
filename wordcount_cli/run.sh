mvn clean
mvn package

echo mvn exec:java  -Dexec.mainClass="com.codeboy.hadoop.tools.MRJarRunner" -Dexec.classpathScope="compile"  -Dexec.args="./target/wordcount_cli-1.0.jar  com.codeboy.hadoop.wordcount.WordCount hdfs://localhost:9000/csv/golfnew.csv /tmp/myout"
mvn exec:java  -Dexec.mainClass="com.codeboy.hadoop.tools.MRJarRunner" -Dexec.classpathScope="compile"  -Dexec.args="./target/wordcount_cli-1.0.jar  com.codeboy.hadoop.wordcount.WordCount hdfs://localhost:9000/csv/golfnew.csv /tmp/myout"
 
 