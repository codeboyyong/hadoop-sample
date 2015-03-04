package com.codeboy.hadoop.wordcount.progressable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

import com.codeboy.hadoop.wordcount.WCIntSumReducer;
import com.codeboy.hadoop.wordcount.WCTokenizerMapper;

public class WordCountWithProgress extends Configured implements Tool {

	private static final int interval = 500;// 0.5 seconds

	public int run(String[] args) throws Exception {
		// creating a JobConf object and assigning a job name for identification
		// purposes
		JobConf conf = new JobConf(getConf(), WordCountWithProgress.class);
		conf.setJobName("WordCount");

		// Setting configuration object with the Data Type of output Key and
		// Value
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		// mapred.jar -> see core-site.xml
		// conf.set("mapred.jar",
		// "/Users/zhaoyong/git/codeboyyong/hadoop-sample/wordcount_cli/target/wordcount_cli-1.0.jar");

		Job job = new Job(conf);

		// Providing the mapper and reducer class names
		job.setMapperClass(WCTokenizerMapper.class);
		job.setReducerClass(WCIntSumReducer.class);

		// the hdfs input and output directory to be fetched from the command
		// line

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		startProgressTimer(job);
		job.waitForCompletion(true);
		return 0;
	}

	private void startProgressTimer(final Job job) {

		Timer timer = new Timer();
		TimerTask task = new  TimerTask(){

			@Override
			public void run() {
				try {
					System.out.println("map progress ="+job.mapProgress());
					System.out.println("reduce progress = " +job.reduceProgress());
				} catch (IOException e) {
 					e.printStackTrace();
				}
			
				
				
			}
			
		};
		timer.schedule(task, interval, interval);
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(),
				new WordCountWithProgress(), args);
		System.exit(res);
	}
}
