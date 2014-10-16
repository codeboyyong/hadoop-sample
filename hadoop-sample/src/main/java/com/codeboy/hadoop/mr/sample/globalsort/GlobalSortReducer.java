package com.codeboy.hadoop.mr.sample.globalsort;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class GlobalSortReducer extends
		Reducer<Text, Text, Text, NullWritable> {
	
	protected NullWritable result = NullWritable.get() ;

	public GlobalSortReducer(){
		
	}
	
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
 
		context.write(key, result);
	}
}