package com.codeboy.hadoop.mr.sample.wordcount;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/***
 * take a simple test and split by token, use a hashmap to do the inmapper combiner to eliminate duplicated outputs
 * @author codeboyyong
 *
 */
public class WCInMapperCombinnerMapper extends Mapper<Object, Text, Text, IntWritable> {

	private final   IntWritable intNUmber = new IntWritable(0);
	private Map<String,Integer> inMapperComibnerMap =  null;
	private final   Text word = new Text();

	@Override
	public void setup(Context context) throws IOException, InterruptedException  {
		super.setup(context);
 		 inMapperComibnerMap = new HashMap<String,Integer>();
	}

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			String word = itr.nextToken();
			if(inMapperComibnerMap.containsKey(word)==false){
				inMapperComibnerMap.put(word,1);
			}else{
				inMapperComibnerMap.put(word,1+inMapperComibnerMap.get(word));
			}
 		}
	}
	
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException{
		for(String key:inMapperComibnerMap.keySet() ){
			word.set(key);
			intNUmber.set(inMapperComibnerMap.get(key));  
			context.write(word, intNUmber);
		}
		super.cleanup(context);
	}
}