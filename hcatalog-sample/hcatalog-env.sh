#this only works for 0.13.1-cdh5.3.1


export HIVE_VERSION=0.13.1-cdh5.3.1
export HADOOP_VERSION=2.5.0-cdh5.3.1

export HIVE_HOME=/Users/zhaoyong/hadoop-install/hive-$HIVE_VERSION
export HCAT_HOME=$HIVE_HOME/hcatalog
export HADOOP_HOME=/Users/zhaoyong/hadoop-install/hadoop-$HADOOP_VERSION


export JDO_VERSION=3.0.1
export LIB303_VERSION=0.9.0
export LIB_THRIFT_VERSION=0.9.0-cdh5-2
export ANTLR_VERSION=3.4
export HCATJAR=$HCAT_HOME/share/hcatalog/hive-hcatalog-core-$HIVE_VERSION.jar
export HCATPIGJAR=$HCAT_HOME/share/hcatalog/hive-hcatalog-pig-adapter-$HIVE_VERSION.jar

export HADOOP_CLASSPATH=$HCATJAR:$HCATPIGJAR:$HIVE_HOME/lib/hive-exec-$HIVE_VERSION.jar\
:$HIVE_HOME/lib/hive-metastore-$HIVE_VERSION.jar:$HIVE_HOME/lib/jdo-api-$JDO_VERSION.jar:$HIVE_HOME/lib/libfb303-$LIB303_VERSION.jar\
:$HIVE_HOME/lib/libthrift-$LIB_THRIFT_VERSION.jar:$HIVE_HOME/conf:$HADOOP_HOME/etc/hadoop/

export  LIBJARS=`echo $HADOOP_CLASSPATH | sed -e 's/:/,/g'`
export LIBJARS=$LIBJARS,$HIVE_HOME/lib/antlr-runtime-$ANTLR_VERSION.jar


export PATH=$PATH:$HIVE_HOME/bin:$HADOOP_HOME/bin


