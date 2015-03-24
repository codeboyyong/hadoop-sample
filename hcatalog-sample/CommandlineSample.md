


## Here is some examples for hcatalog



### Command Line Sample

Reference : [https://cwiki.apache.org/confluence/display/Hive/HCatalog](https://cwiki.apache.org/confluence/display/Hive/HCatalog)

Note: most of hcat comamnd is same as hive command.


####Show databases/schemas (in hive schema=database)
#####[hive@hadoop ~]$ hcat -e "show databases"; or  hcat -e "show schemas";	
`OK`

default

`Time taken: 0.886 seconds`
#### Create a table
#####[hive@hadoop ~]$ ./hcat -e "create table golf(outlook string,temperature int ,humidity int,wind string,play string ) row format delimited fields terminated by ',' stored as textfile"

`OK`
 
  
credit 
####Show tables
#####[hive@hadoop ~]$ hcat -e "show tables"; 

`OK`
	
golf	

`Time taken: 0.894 seconds`

####Select from table 
#####[hive@hadoop ~]$ hcat -e "select * from default.golf";

`FAILED: SemanticException Operation not supported.`


####Show table details
#####[hive@hadoop ~]$ hcat -e "DESCRIBE  default.golf"; or hcat -e "desc golf";

`OK`

   outlook             	string              	                    
temperature         	int                 	                    
humidity            	int                 	                    
wind                	string              	                    
play                	string              	                    
Time taken: 0.089 seconds, Fetched: 5 row(s)   	                    
 

load data to table 
 
  94  hcat -e "load data local inpath '/Users/zhaoyong/git/codeboyyong/hadoop-sample/hcatalog-sample/src/main/resources/golf.csv' overwrite into table golf"


 
### MapReduce Sample 	
reference :	

[https://github.com/cloudera/hcatalog-examples](https://github.com/cloudera/hcatalog-examples)	

[http://hortonworks.com/hadoop-tutorial/how-to-use-hcatalog-basic-pig-hive-commands](http://hortonworks.com/hadoop-tutorial/how-to-use-hcatalog-basic-pig-hive-commands)


### Pig Sample

`https://cwiki.apache.org/confluence/display/Hive/HCatalog+LoadStore`

### REST API SAMPLE

`https://cwiki.apache.org/confluence/display/Hive/WebHCat+Reference+AllDDL`


###Sqoop Integration

[http://docs.hortonworks.com/HDPDocuments/HDP1/HDP-1.3.1/bk_dataintegration/content/ch_using-sqoop-hcat-integration.html](http://docs.hortonworks.com/HDPDocuments/HDP1/HDP-1.3.1/bk_dataintegration/content/ch_using-sqoop-hcat-integration.html)