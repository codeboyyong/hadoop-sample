package com.codeboy.hadoop.test.a_mr.a_wordcount;

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
import org.junit.Test;

import com.codeboy.hadoop.base.HadoopCluster;
import com.codeboy.hadoop.mr.sample.wordcount.IntSumReducer;
import com.codeboy.hadoop.mr.sample.wordcount.TokenizerMapper;
import com.codeboy.hadoop.util.HadoopClusterUtil;
import com.codeboy.hadoop.util.HadoopFileUtil;

/***
 * need user create the jar file and run the jar file in main
 * @author codeboyyong
 *
 */
public class LocalWordCountTestOrigin_PseudoCluser {

	@Test
	public void testWordCount() throws Exception {

		InputStream jsonFileInputStream = LocalWordCountTestOrigin_PseudoCluser.class
				.getResourceAsStream("/com/codeboy/hadoop/resource/cluster/localhosthadoop.txt");
		HadoopCluster hadoopCluster = HadoopClusterUtil
				.readHadoopClusterFromJsonInputStram(jsonFileInputStream);

		InputStream wordCountInputStream = LocalWordCountTestOrigin_PseudoCluser.class
				.getResourceAsStream("/com/codeboy/hadoop/resource/testdata/wordcount.txt");

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


	private static Job createWCJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {
		jobConf.set("mapred.jar", findCodeBoyHadoopToyJar());
		Job job = new Job(jobConf);

		job.setJarByClass(TokenizerMapper.class);

		job.setJobName("wordcount");

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(TokenizerMapper.class);

		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		return job;
	}

	private static String findCodeBoyHadoopToyJar() {
		return "/Users/zhaoyong/git/hadoop/CodeBoyHadoopToy/CodeBoyHadoopToy.jar";
	}

}
