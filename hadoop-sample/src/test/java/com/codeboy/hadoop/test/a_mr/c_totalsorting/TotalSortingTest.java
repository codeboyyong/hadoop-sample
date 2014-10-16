package com.codeboy.hadoop.test.a_mr.c_totalsorting;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.examples.terasort.TeraInputFormat;
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
import com.codeboy.hadoop.pseudocluster.test.LocalWordCountTest_PseudoCluser;
import com.codeboy.hadoop.test.a_mr.BaseHadoopTest;
import com.codeboy.hadoop.test.a_mr.TestClusterManager;
import com.codeboy.hadoop.util.HadoopClusterUtil;
import com.codeboy.hadoop.util.HadoopFileUtil;

/***
According to this :
http://mail-archives.apache.org/mod_mbox/hadoop-common-user/201201.mbox/%3CCAPDj3WmTyd5LzvhT_971X60EYMahKFg_TAXFSqeeYF3Luf4Evg@mail.gmail.com%3E
we can not test total sorting in mini cluster,, let's try in local cluster mode
 * 
 * @author codeboyyong
 * 
 */
public class TotalSortingTest extends BaseHadoopTest {

	public static final String localResourcePath = "/com/codeboy/hadoop/resource/testdata/1million.txt";
	public static final String inputPath = "/tmp/global_sorting_input";
	public static final String outputPath = "/tmp/global_sorting_output" + System.currentTimeMillis();
	private static final String PARTITION_FILENAME = "_partition.lst";
 
 
	@Before
	@Override
	public void setUpBefore() {
		//do nothing since this special test need the local custer is started			
		//please run ./start-hadoop.sh 1.2.1 first	
	}
 
	public void checkInputBlocks(HadoopCluster hadoopCluster ) {
		try {

			FileStatus fileStatus=HadoopFileUtil.getHDFSFileInfo(hadoopCluster, inputPath);
			Assert.assertEquals( fileStatus.getBlockSize(),512000);
			
		 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	@Test
	public void testTotalSorting() throws Exception {
		
		
		InputStream jsonFileInputStream = LocalWordCountTest_PseudoCluser.class
				.getResourceAsStream("/com/codeboy/hadoop/resource/cluster/conf/zhaoyong/TestCluster.json");
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
		
		
	    InputSampler.Sampler<Text, NullWritable> sampler =
	      new InputSampler.RandomSampler<Text, NullWritable>(0.1, 1000, 4);
	    
	    InputSampler.writePartitionFile(job, sampler);
	    System.out.println("partitionFile from TotalOrderPartitioner="  + TotalOrderPartitioner.getPartitionFile(conf));
	    // Add to DistributedCache
  	    System.out.println("partitionFile=" + partitionFile);
 	    HadoopFileUtil.printSequenceFileContent(hadoopCluster,     partitionFile.toString(), conf,new Text(),  NullWritable.get()); 
	    
 	    
	     job.waitForCompletion(true)  ;
	     HadoopFileUtil.printReducerOutput( outputPath, conf); 

	}

	private  static String findCodeBoyHadoopToyJar() {
		return "/Users/zhaoyong/hadoop-sample-1.0.jar";
	}
	private    Job createJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {
 		Job job = new Job(jobConf);

 		//mini cluster!!!
 		jobConf.set("mapred.jar", findCodeBoyHadoopToyJar());

		job.setJobName("wordcount");

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
