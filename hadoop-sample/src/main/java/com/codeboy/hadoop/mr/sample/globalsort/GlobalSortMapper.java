package com.codeboy.hadoop.mr.sample.globalsort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/***
 * 1million.txt, each line is a int
 * @author codeboyyong
 * 
 */
public class GlobalSortMapper extends Mapper<Object, Text, LongWritable, LongWritable> {

	private final static LongWritable outKey = new LongWritable(0);
	private LongWritable outValue = new LongWritable(0);

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		outKey.set(Long.parseLong(value.toString()));
		outValue.set(Long.parseLong(value.toString()));
		context.write(outKey, outValue);

	}
}