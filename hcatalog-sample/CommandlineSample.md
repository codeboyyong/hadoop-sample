


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
#####[hive@hadoop ~]$ hcat -e "create table groups(name string,placeholder string,id int) row format delimited fields terminated by ':' stored as textfile"

`OK`
####Show tables
#####[hive@hadoop ~]$ hcat -e "show tables"; 

`OK`

groups	
credit	

`Time taken: 0.894 seconds`

####Select from table 
#####[hive@hadoop ~]$ hcat -e "select * from default.credit";

`FAILED: SemanticException Operation not supported.`


####Show table details
#####[hive@hadoop ~]$ hcat -e "DESCRIBE  default.credit"; or hcat -e "desc credit";

`OK`

id                  	int                 	                    
times90dayslate     	int                 	                    
revolving_util      	double              	                    
debt_ratio          	double              	                    
credit_lines        	int                 	                    
monthly_income      	double              	                    
times30dayslate_2years	int                 	                    
srsdlqncy           	int                 	                    

`Time taken: 1.261 seconds`

load data to table 
load data local inpath '/tmp/input.txt' into table test_sq;
create table with data

 
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