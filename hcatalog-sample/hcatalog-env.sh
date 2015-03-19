export HCAT_HOME=/usr/lib/hive-hcatalog
export HIVE_HOME=/usr/lib/hive

export HIVE_VERSION=0.13.1-cdh5.3.0

export HCATJAR=$HCAT_HOME/share/hcatalog/hcatalog-core-$HIVE_VERSION.jar
export HCATPIGJAR=$HCAT_HOME/share/hcatalog/hcatalog-pig-adapter-$HIVE_VERSION.jar

export HADOOP_CLASSPATH=$HCATJAR:$HCATPIGJAR:$HIVE_HOME/lib/hive-exec-$HIVE_VERSION.jar\
:$HIVE_HOME/lib/hive-metastore-$HIVE_VERSION.jar:$HIVE_HOME/lib/jdo-api-*.jar:$HIVE_HOME/lib/libfb303-*.jar\
:$HIVE_HOME/lib/libthrift-*.jar:$HIVE_HOME/lib/slf4j-api-*.jar:$HIVE_HOME/conf:/etc/hadoop/conf

export  LIBJARS=`echo $HADOOP_CLASSPATH | sed -e 's/:/,/g'`
export LIBJARS=$LIBJARS,$HIVE_HOME/lib/antlr-runtime-*.jar