package com.codeboy.hadoop.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;

import com.codeboy.hadoop.base.HadoopCluster;
import com.codeboy.hadoop.base.HadoopConstants;
import com.google.gson.Gson;

public class HadoopClusterUtil {
	 
	public static Configuration toHadoopConfiguration(HadoopCluster hadoopCluster) {
		final Configuration conf = new Configuration();

		conf.set("fs.default.name",toNameNodeURL(  hadoopCluster));
 		//if don't set, running in local mode, you can debug into mapper and reducer
		//but then the out put will loss
		//conf.set("mapred.job.tracker", hadoopClust er.getJobHost() +":" +hadoopCluster.getJobPort());

		if(hadoopCluster.getCustomizedPorpeties()!=null){
			for(Object  key :hadoopCluster.getCustomizedPorpeties().keySet()){
				conf.set(key.toString(), (String)hadoopCluster.getCustomizedPorpeties().getProperty(key.toString()));	
			}
		}
		return conf;
	}
	
	public static HadoopCluster readHadoopClusterFromJsonFile(String filePath) throws Exception{
		String content = FileUtil.readAllContent( filePath   );
		return new Gson().fromJson(content, HadoopCluster.class); 
	}
	public static  HadoopCluster readHadoopClusterFromJsonInputStram(InputStream jsonFileInputStream) throws Exception{
		String content = FileUtil.readFromInputStream( jsonFileInputStream   );
		return new Gson().fromJson(content, HadoopCluster.class); 
	}
	
	public static boolean writeHadoopClusterToJsonFile(String filePath,HadoopCluster hadoopCluster) throws IOException{
		String content = new Gson().toJson(hadoopCluster);
		 FileUtil.writeToFile(filePath, content, null,false );
		 return true;
	}
	
	
	
	public static void main(String[] args)  {
		HadoopCluster hadoopCluster = new HadoopCluster("localhost","hadoop","supergroups","localhost",9000,"localhost",9001); 
 		try {
			writeHadoopClusterToJsonFile("~/tst", hadoopCluster) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
 	}

	public static String toNameNodeURL(HadoopCluster hadoopCluster) { 
 		return  HadoopConstants.HDFS_PREFIX  + hadoopCluster.getHdfsHost() +
				":" + 		hadoopCluster.getHdfsPort();
	} 
}
