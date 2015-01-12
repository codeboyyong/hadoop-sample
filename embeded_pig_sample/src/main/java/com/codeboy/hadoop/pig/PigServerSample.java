package com.codeboy.hadoop.pig;

import org.apache.hadoop.conf.Configuration;
import org.apache.pig.ExecType;
import org.apache.pig.PigServer;

public class PigServerSample {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

//		conf.set("fs.default.name", "maprfs:///maprdemo:7222");
		conf.set("fs.default.name", "maprfs:///");
		
		conf.set("mapreduce.framework.name", "yarn");
		conf.set("yarn.resourcemanager.address", "maprdemo:8032");
		conf.set("yarn.resourcemanager.scheduler.address", "maprdemo:8030");
  
		PigServer server = new PigServer(ExecType.MAPREDUCE, conf);
		server.registerQuery("A = load 'maprfs:///golfnew.csv'  USING PigStorage(',') AS (c1:chararray, c2:chararray, c3:chararray,c4:chararray,c5:chararray);");
		
		// This store will generate a job without reducer task, it will work
		//server.store("A", "/temp/result_aaaxa") ; 
		
		server.registerQuery("B = limit A 3;");
		// This store will generate a job with reducer task, it will fail
		server.store("B", "/temp/result_b3xeb") ; 
		
	}
}
