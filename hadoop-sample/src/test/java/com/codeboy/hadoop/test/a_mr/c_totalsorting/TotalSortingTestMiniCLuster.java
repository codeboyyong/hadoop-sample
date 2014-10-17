package com.codeboy.hadoop.test.a_mr.c_totalsorting;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile.Writer;
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
import com.codeboy.hadoop.pseudocluster.test.LocalWordCountTest_PseudoCluser;
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
public class TotalSortingTestMiniCLuster extends BaseHadoopTest {
	private static final String PARTITION_FILENAME = "_partition.lst";

	public static final String localResourcePath = "/com/codeboy/hadoop/resource/testdata/1million.txt";
	public static final String inputPath = "/tmp/global_sorting_input";
	public static final String outputPath = "/tmp/global_sorting_output"
			+ System.currentTimeMillis();
	HadoopCluster hadoopCluster; 
	int BLOCK_SIZE = 1024000;

	@Before
	@Override
	public void setUpBefore() {
		try {

			Properties overrideProperties = new Properties();
			overrideProperties.put("dfs.block.size", BLOCK_SIZE+"");//1024
			super.testClusterManager = new TestClusterManager(overrideProperties);
			super.testClusterManager.startUp();

			super.copyToHadoop(localResourcePath, inputPath);
			
			hadoopCluster =super.testClusterManager.getHadoopClusterManager().getHadoopCluster();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
 
	private void checkInputBlocks() {
		try {

			FileStatus fileStatus=HadoopFileUtil.getHDFSFileInfo(hadoopCluster, inputPath);
			Assert.assertEquals( fileStatus.getBlockSize(),BLOCK_SIZE);
			
		 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

	@Test
	public void testIputSampler() throws Exception {
		InputStream jsonFileInputStream = LocalWordCountTest_PseudoCluser.class
				.getResourceAsStream("/com/codeboy/hadoop/resource/cluster/conf/zhaoyong/TestCluster_Local.json");
		HadoopCluster hadoopCluster = HadoopClusterUtil
				.readHadoopClusterFromJsonInputStram(jsonFileInputStream);

		InputStream wordCountInputStream = LocalWordCountTest_PseudoCluser.class
				.getResourceAsStream(localResourcePath);

 		HadoopFileUtil.writeInputStreamIntoHDFSFile(wordCountInputStream,
				hadoopCluster, inputPath);

 		 
		
		Job job = createJob( HadoopClusterUtil.toHadoopConfiguration(hadoopCluster),
				inputPath, outputPath);
	    Configuration conf = job.getConfiguration();


		Path inputDir = new Path(inputPath);
		 inputDir = inputDir.makeQualified(inputDir.getFileSystem(job.getConfiguration()));
		    Path partitionFile = new Path("/tmp",  PARTITION_FILENAME);
		    TotalOrderPartitioner.setPartitionFile(conf, partitionFile) ;
		    URI partitionUri = new URI(partitionFile.toString() + "#" +  PARTITION_FILENAME);

		    DistributedCache.addCacheFile(partitionUri, conf);
		    DistributedCache.createSymlink(conf);
		    conf.setInt("dfs.replication", 1);
		
		
//	    InputSampler.Sampler<Text, NullWritable> sampler =
//	      new InputSampler.RandomSampler<Text, NullWritable>(0.1, 4, 4);
//	    
//	    InputSampler.writePartitionFile(job, sampler);
//	    System.out.println("partitionFile from TotalOrderPartitioner="  + TotalOrderPartitioner.getPartitionFile(conf));
	    // Add to DistributedCache
  	    
		    Writer writer = new SequenceFile.Writer(FileSystem.get(conf), conf,partitionFile,  LongWritable.class, NullWritable.class) ; 
		    writer.append(new LongWritable(10000), NullWritable.get());
		    writer.append(new LongWritable(18000), NullWritable.get());
		    writer.append(new LongWritable(25000), NullWritable.get());
		    writer.close();
		    
		    System.out.println("partitionFile=" + partitionFile);
 	    HadoopFileUtil.printSequenceFileContent(hadoopCluster,     partitionFile.toString(), conf,new LongWritable(),  NullWritable.get()); 
	    
	     job.setNumReduceTasks(4);
 	    
	     job.waitForCompletion(true)  ;
	     HadoopFileUtil.printReducerOutput( outputPath, conf); 

	     
	     checkInputBlocks();
	}

 
	private    Job createJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {
 		
		Job job = new Job(jobConf);

 		//mini cluster!!!
 	    job.setJarByClass(GlobalSortMapper.class);

		job.setJobName("totalSort");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setPartitionerClass(TotalOrderPartitioner.class);

		
		job.setMapperClass(GlobalSortMapper.class);
		job.setReducerClass(GlobalSortReducer.class);


		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

 		FileInputFormat.setInputPaths(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		 
		
		return job;
	}
}
