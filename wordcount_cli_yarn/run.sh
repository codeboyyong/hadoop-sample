mvn clean
mvn package

mvn exec:java  -Dexec.mainClass="com.codeboy.hadoop.wordcount.WordCount"   -Dexec.args="hdfs://localhost:9000/csv/golfnew.csv /tmp/myout"

 