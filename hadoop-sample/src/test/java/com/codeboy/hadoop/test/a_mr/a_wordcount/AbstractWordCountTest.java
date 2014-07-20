package com.codeboy.hadoop.test.a_mr.a_wordcount;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.junit.After;
import org.junit.Before;

import com.codeboy.hadoop.mr.sample.wordcount.WCIntSumReducer;
import com.codeboy.hadoop.mr.sample.wordcount.WCTokenizerMapper;
import com.codeboy.hadoop.test.a_mr.BaseHadoopTest;

/***
 * The basic version, hadoop will auto sorting the key
 * @author codeboyyong
 * 
 */
public abstract class AbstractWordCountTest extends BaseHadoopTest {

	public static final String localResourcePath = "/com/codeboy/hadoop/resource/testdata/wordcount_input_a2g.txt";
	public static final String inputPath = "/tmp/wordcount.txt";
	public static final String outputPath = "/tmp/wordcount_out_"
			+ System.currentTimeMillis();

	@Before
	public void prepareData(){
		try {
			super.copyToHadoop(localResourcePath, inputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	 
	public Job runWordCountJob() throws Exception {


		Configuration hadoopConfiguration = super.getHadoopConfiguration(); 
		Job job = createWCJob(hadoopConfiguration, inputPath, outputPath);
 
		// this waits until the job completes
		job.waitForCompletion(true);

		if (job.isSuccessful()) {
			System.out.println("Job completed successfully");
			super.printOutputFile(hadoopConfiguration, outputPath  );
			Assert.assertTrue(true);
		} else {
			System.out.println("Job Failed");
			Assert.fail("Job Failed"); 
		}
		return job;
	}
	
 

	@After
	public void cleanData(){
		try {
			super.deleteFile( inputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/***
	 * base version,  it is runable
	 * @param jobConf
	 * @param inputPath
	 * @param outputPath
	 * @return
	 * @throws IOException
	 */
	protected Job createWCJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {
		Job job = new Job(jobConf);

 		
		job.setJarByClass(WCTokenizerMapper.class);

		job.setJobName("wordcount");
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(WCTokenizerMapper.class);

		job.setCombinerClass(WCIntSumReducer.class);
		job.setReducerClass(WCIntSumReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		
		return job;
	}


	public Job runWordCountJobMapOnly() throws  Exception {
		Configuration hadoopConfiguration = super.getHadoopConfiguration(); 
		Job job = createWCJob(hadoopConfiguration, inputPath, outputPath);
		
		job.setNumReduceTasks(0) ;
		
		// this waits until the job completes
		job.waitForCompletion(true);

		if (job.isSuccessful()) {
			System.out.println("Job completed successfully");
			super.printOutputFile(hadoopConfiguration, outputPath  );
			Assert.assertTrue(true);
		} else {
			System.out.println("Job Failed");
			Assert.fail();
		}
		return job;
 
		
	}


 


}
