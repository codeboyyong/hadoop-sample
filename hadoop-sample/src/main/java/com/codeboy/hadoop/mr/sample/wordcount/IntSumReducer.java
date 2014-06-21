package com.codeboy.hadoop.mr.sample.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IntSumReducer extends
		Reducer<Text, IntWritable, Text, IntWritable> {
	
	public static  enum COUNTERS {
		WC_COMBINER_A,
		WC_COMBINER_B,
		WC_COMBINER_C,
		WC_COMBINER_D,
		WC_COMBINER_E,
		WC_COMBINER_F,
		WC_COMBINER_G,	
		
		WC_REDUCER_A,
		WC_REDUCER_B,
		WC_REDUCER_C,
		WC_REDUCER_D,
		WC_REDUCER_E,
		WC_REDUCER_F,
		WC_REDUCER_G
		};
	
	protected String countprefix = "WC_REDUCER_";
	
	protected IntWritable result = new IntWritable();

	public IntSumReducer(){
		
	}
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		System.out.println("reduce [" + this 
				+ "]: key =" + key.toString()+", values = ");
		for (IntWritable val : values) {
			int value = val.get(); 
			sum += value;
		 
			context.getCounter(COUNTERS.valueOf(countprefix+key.toString().toUpperCase())).increment(1); 
			System.out.print(value+",");
 		}
		result.set(sum);
		context.write(key, result);
	}
}