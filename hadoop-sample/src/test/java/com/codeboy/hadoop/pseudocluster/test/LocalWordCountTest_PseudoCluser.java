package com.codeboy.hadoop.pseudocluster.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.codeboy.hadoop.base.HadoopCluster;
import com.codeboy.hadoop.mr.sample.wordcount.WCIntSumReducer;
import com.codeboy.hadoop.mr.sample.wordcount.WCTokenizerMapper;
import com.codeboy.hadoop.util.HadoopClusterUtil;
import com.codeboy.hadoop.util.HadoopFileUtil;

/***
 * need user create the jar file and run the jar file in main.
 * Make sure start hadoop local cluster before run it.
 * 
 * cd hadoop-scripts/hadoop-installtion/script/
 * 
 * ./start-hadoop.sh 1.2.1
 * 
 * @author codeboyyong
 *
 */
public class LocalWordCountTest_PseudoCluser {

 
	public   static void main( String args[]) throws Exception {

		InputStream jsonFileInputStream = LocalWordCountTest_PseudoCluser.class
				.getResourceAsStream("/com/codeboy/hadoop/resource/cluster/conf/zhaoyong/TestCluster.json");
		HadoopCluster hadoopCluster = HadoopClusterUtil
				.readHadoopClusterFromJsonInputStram(jsonFileInputStream);

		InputStream wordCountInputStream = LocalWordCountTest_PseudoCluser.class
				.getResourceAsStream("/com/codeboy/hadoop/resource/testdata/wordcount_input_a2g.txt");

		String inputPath = "/tmp/wordcount.txt";
		HadoopFileUtil.writeInputStreamIntoHDFSFile(wordCountInputStream,
				hadoopCluster, inputPath);

		String outputPath = "/tmp/wordcount" + System.currentTimeMillis();
		Job job = createWCJob(
				HadoopClusterUtil.toHadoopConfiguration(hadoopCluster),
				inputPath, outputPath);

		// this waits until the job completes
		job.waitForCompletion(true);

		if (job.isSuccessful()) {
			System.out.println("Job completed successfully");
			// outputPath
			HadoopFileUtil.printReducerOutput(outputPath, 
					HadoopClusterUtil.toHadoopConfiguration(hadoopCluster));
		} else {
			System.out.println("Job Failed");
		}

	}


	private static  Job createWCJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {
		jobConf.set("mapred.jar", findCodeBoyHadoopToyJar());
		Job job = new Job(jobConf);

		job.setJarByClass(WCTokenizerMapper.class);

		job.setJobName("wordcount");

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

	private  static String findCodeBoyHadoopToyJar() {
		return "/Users/zhaoyong/hadoop-sample-1.0.jar";
	}

}
