


## Here is some examples for HCatalog



before it we need
1) configure hive use remote metastore databse
in conf/ hive-site.xml
<configuration>

<property>
  <name>hive.metastore.uris</name>
  <value>thrift://localhost:9083</value>
</property>

<property>
  <name>javax.jdo.option.ConnectionURL</name>
  <value>jdbc:postgresql://localhost:5432/hive_metastore</value>
</property>

<property>
  <name>javax.jdo.option.ConnectionDriverName</name>
  <value>org.postgresql.Driver</value>
</property>
 
<property>
  <name>javax.jdo.option.ConnectionUserName</name>
  <value>******</value>
</property>

<property>
  <name>javax.jdo.option.ConnectionPassword</name>
  <value>******</value>
</property>
 

</configuration>

2)  start hive metastore server as a remote server :
 
 [~/hadoop-install/hive-0.13.1-cdh5.3.1/bin ] $hive --service metastore
 
3)   start hiveserver2
[~/hadoop-install/hive-0.13.1-cdh5.3.1/bin ] $./hiveserver2 

### Command Line Sample


 
See: [Command Line Sample](CommandlineSample.md)

Reference : [https://cwiki.apache.org/confluence/display/Hive/HCatalog](https://cwiki.apache.org/confluence/display/Hive/HCatalog)

 
### MapReduce Sample 	

#####Usage:
[hive@hadoop ~]$ hcat -e "create table **golf**(`outlook string,temperature int ,humidity int,wind string,play string` ) row format delimited fields terminated by ',' stored as textfile"
	
[hive@hadoop ~]$ hcat -e "load data local inpath '.src/main/resources/golf.csv' overwrite into table `golf`"



[hive@hadoop ~]$ hcat -e "create table **golf_columnfilter** (`temperature int , play string` ) row format delimited fields terminated by ',' stored as textfile"

[hive@hadoop ~]$ mvn package

[hive@hadoop ~]$ source ./hcatalog-env.sh	

[hive@hadoop ~]$ hadoop jar target/hcatalog-sample-1.0.jar com.codeboy.hcatalog.HCatalogColumnFilter -files $HCATJAR -libjars $LIBJARS **golf golf_columnfilter** `temperature,play`

[hive@hadoop ~]$mvn exec:java  -Dexec.mainClass="com.codeboy.hcatalog.HCatalogColumnFilter"   -Dexec.args="--libjars /apache2.2.0/apache-hive-0.13.1-bin/hcatalog/share/hcatalog/hive-hcatalog-core-0.13.1.jar golf golf_columnfilter temperature,play"


mvn exec:java  -Dexec.mainClass="com.codeboy.hcatalog.HCatalogColumnFilter"   -Dexec.args="--libjars /Users/zhaoyong/git/apache/hive/hcatalog/core/target/hive-hcatalog-core-0.13.1.jar,/apache2.2.0/apache-hive-0.13.1-bin/./lib/hive-exec-0.13.1.jar,/apache2.2.0/apache-hive-0.13.1-bin/./lib/hive-metastore-0.13.1.jar,/Users/zhaoyong/.m2/repository/org/apache/thrift/libfb303/0.9.0/libfb303-0.9.0.jar golf golf_columnfilter temperature,play"

#####Note:
This sample shows we did a column filter by mapreduce from 1 HCatalog tabel to another HCatalog table. These two table must be existed in HCatalog/Hive
#####Reference :	

[https://github.com/cloudera/hcatalog-examples](https://github.com/cloudera/hcatalog-examples)	

[http://hortonworks.com/hadoop-tutorial/how-to-use-hcatalog-basic-pig-hive-commands](http://hortonworks.com/hadoop-tutorial/how-to-use-hcatalog-basic-pig-hive-commands)


### Pig Sample

[https://cwiki.apache.org/confluence/display/Hive/HCatalog+LoadStore](https://cwiki.apache.org/confluence/display/Hive/HCatalog+LoadStore)

### REST API SAMPLE

`https://cwiki.apache.org/confluence/display/Hive/WebHCat+Reference+AllDDL`


###Sqoop Integration

[http://docs.hortonworks.com/HDPDocuments/HDP1/HDP-1.3.1/bk_dataintegration/content/ch_using-sqoop-hcat-integration.html](http://docs.hortonworks.com/HDPDocuments/HDP1/HDP-1.3.1/bk_dataintegration/content/ch_using-sqoop-hcat-integration.html)