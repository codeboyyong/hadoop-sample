


## Here is some examples for HCatalog



### Command Line Sample


 
See: [Command Line Sample](CommandlineSample.md)

Reference : [https://cwiki.apache.org/confluence/display/Hive/HCatalog](https://cwiki.apache.org/confluence/display/Hive/HCatalog)

 
### MapReduce Sample 	

#####Usage:
`[hive@hadoop ~]$ hcat -e "create table groups(name string,placeholder string,id int) row format delimited fields terminated by ':' stored as textfile"`

`[hive@hadoop ~]$ mvn package`

`[hive@hadoop ~]$ hcat -e "load data local inpath '/etc/group' overwrite into table groups"`

`[hive@hadoop ~]$ source ./hcatalog-env.sh`

`[hive@hadoop ~]$ hadoop jar target/hcatalog-sample-1.0.jar com.cloudera.test.UseHCat -files $HCATJAR -libjars $LIBJARS groups groupids`

#####Reference :	

[https://github.com/cloudera/hcatalog-examples](https://github.com/cloudera/hcatalog-examples)	

[http://hortonworks.com/hadoop-tutorial/how-to-use-hcatalog-basic-pig-hive-commands](http://hortonworks.com/hadoop-tutorial/how-to-use-hcatalog-basic-pig-hive-commands)


### Pig Sample

[https://cwiki.apache.org/confluence/display/Hive/HCatalog+LoadStore](https://cwiki.apache.org/confluence/display/Hive/HCatalog+LoadStore)

### REST API SAMPLE

`https://cwiki.apache.org/confluence/display/Hive/WebHCat+Reference+AllDDL`


###Sqoop Integration

[http://docs.hortonworks.com/HDPDocuments/HDP1/HDP-1.3.1/bk_dataintegration/content/ch_using-sqoop-hcat-integration.html](http://docs.hortonworks.com/HDPDocuments/HDP1/HDP-1.3.1/bk_dataintegration/content/ch_using-sqoop-hcat-integration.html)