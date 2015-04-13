package com.codeboy.hadoop.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class WordCount extends Configured implements Tool{
      public int run(String[] args) throws Exception
      {
            //creating a JobConf object and assigning a job name for identification purposes
            JobConf conf = new JobConf(getConf(), WordCount.class);
            conf.setJobName("WordCount");

            //Setting configuration object with the Data Type of output Key and Value
            conf.setOutputKeyClass(Text.class);
            conf.setOutputValueClass(IntWritable.class);
            
            //mapred.jar -> see core-site.xml
          //  conf.set("mapred.jar", "/Users/zhaoyong/git/codeboyyong/hadoop-sample/wordcount_cli/target/wordcount_cli-1.0.jar");
    		
          
           Job job = new Job(conf);

            //Providing the mapper and reducer class names
    	  job.setMapperClass(WCTokenizerMapper.class);
          job.setReducerClass(WCIntSumReducer.class);

            //the hdfs input and output directory to be fetched from the command line

        	FileInputFormat.setInputPaths(job, new Path(args[0]));
    		FileOutputFormat.setOutputPath(job, new Path(args[1]));
            
            job.waitForCompletion(true);
            return 0;
      }
     
      public static void main(String[] args) throws Exception
    {
         final Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "kerberos");
        UserGroupInformation.setConfiguration(conf);
        
        //do kinit programmatically
        UserGroupInformation.loginUserFromKeytab("hdfs/abc.com@ABC", "/home/zhaoyong/keytab/hdfs.keytab");
        
        //use hdfs user pretend to be the “user_abc"
        UserGroupInformation ugi = UserGroupInformation.createProxyUser(“user_abc",UserGroupInformation.getLoginUser());
       //UGI do as method still works now.
        ugi.doAs(new PrivilegedExceptionAction<Void>() {
            @Override
            public Void run() throws Exception {
                int res = ToolRunner.run(new Configuration(), new WordCount(),args);//submit hadoop job
                return null;
            }
        });
                                                                        }
                                                                        
     
            System.exit(0);
      }
}
