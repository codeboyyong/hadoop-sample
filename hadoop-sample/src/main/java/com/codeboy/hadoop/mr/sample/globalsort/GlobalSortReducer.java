package com.codeboy.hadoop.mr.sample.globalsort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class GlobalSortReducer extends

Reducer<LongWritable, LongWritable, LongWritable, NullWritable> {

	protected NullWritable result = NullWritable.get();

	public GlobalSortReducer() {

	}

	public void reduce(LongWritable key, Iterable<LongWritable> values,
			Context context) throws IOException, InterruptedException {
		for (LongWritable xlong : values) {
			context.write(xlong, result);

		}
	}
}