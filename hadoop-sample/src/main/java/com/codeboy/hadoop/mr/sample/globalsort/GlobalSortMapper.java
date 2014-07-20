package com.codeboy.hadoop.mr.sample.globalsort;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/***
 * 1million.txt, each line is a int
 * @author codeboyyong
 * 
 */
public class GlobalSortMapper extends Mapper<Object, Text, IntWritable, Text> {

	private final static IntWritable outKey = new IntWritable(0);
	private Text outValue = new Text("");

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		outKey.set(Integer.parseInt(value.toString()));

		context.write(outKey, outValue);

	}
}