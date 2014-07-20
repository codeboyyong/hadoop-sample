package com.codeboy.hadoop.test.a_mr.c_globalsorting;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.junit.Before;
import org.junit.Test;

import com.codeboy.hadoop.base.HadoopCluster;
import com.codeboy.hadoop.mr.sample.globalsort.GlobalSortMapper;
import com.codeboy.hadoop.mr.sample.globalsort.GlobalSortReducer;
import com.codeboy.hadoop.test.a_mr.BaseHadoopTest;
import com.codeboy.hadoop.test.a_mr.TestClusterManager;
import com.codeboy.hadoop.util.HadoopClusterUtil;
import com.codeboy.hadoop.util.HadoopFileUtil;

/***
 * The basic version, hadoop will auto sorting the key
 * 
 * @author codeboyyong
 * 
 */
public class GlobalSortingTest extends BaseHadoopTest {

	public static final String localResourcePath = "/com/codeboy/hadoop/resource/testdata/1million.txt";
	public static final String inputPath = "/tmp/global_sorting_input";
	public static final String outputPath = "/tmp/global_sorting_output"
			+ System.currentTimeMillis();
	HadoopCluster hadoopCluster; 

	@Before
	@Override
	public void setUpBefore() {
		try {

			Properties overrideProperties = new Properties();
			overrideProperties.put("dfs.block.size", "512000");//512k
			super.testClusterManager = new TestClusterManager(overrideProperties);
			super.testClusterManager.startUp();

			super.copyToHadoop(localResourcePath, inputPath);
			
			hadoopCluster =super.testClusterManager.getHadoopClusterManager().getHadoopCluster();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
 
	public void checkInputBlocks() {
		try {

			FileStatus fileStatus=HadoopFileUtil.getHDFSFileInfo(hadoopCluster, inputPath);
			Assert.assertEquals( fileStatus.getBlockSize(),512000);
			
		 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

	@Test
	public void testIputSampler() throws Exception {
		Job job = createJob( HadoopClusterUtil.toHadoopConfiguration(hadoopCluster),
				inputPath, outputPath);
		
		job.setPartitionerClass(TotalOrderPartitioner.class);

	    InputSampler.Sampler<Text, NullWritable> sampler =
	      new InputSampler.RandomSampler<Text, NullWritable>(0.1, 1000, 4);
	    
	    InputSampler.writePartitionFile(job, sampler);
	    
	    // Add to DistributedCache
	    Configuration conf = job.getConfiguration();
	    String partitionFile =TotalOrderPartitioner.getPartitionFile(conf);
//	    System.out.println("partitionFile=" + partitionFile);
//	    HadoopFileUtil.printSequenceFileContent(hadoopCluster, "/user/"+System.getProperty("user.name")
//	    		+ "/"+partitionFile, conf,new Text(),  NullWritable.get()); 
	    
	    URI partitionUri = new URI(partitionFile + "#" +
	        TotalOrderPartitioner.DEFAULT_PATH);
	    DistributedCache.addCacheFile(partitionUri, conf);
	    DistributedCache.createSymlink(conf);
	    
	     job.waitForCompletion(true)  ;
	     HadoopFileUtil.printReducerOutput( outputPath, conf); 

	}
	
	private    Job createJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {
 		Job job = new Job(jobConf);

 		//mini cluster!!!
		job.setJarByClass(GlobalSortMapper.class);

		job.setJobName("wordcount");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(GlobalSortMapper.class);
		job.setReducerClass(GlobalSortReducer.class);


		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		return job;
	}

}
