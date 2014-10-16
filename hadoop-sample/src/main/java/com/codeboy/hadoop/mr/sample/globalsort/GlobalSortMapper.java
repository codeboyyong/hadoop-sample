package com.codeboy.hadoop.mr.sample.globalsort;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/***
 * 1million.txt, each line is a int
 * @author codeboyyong
 * 
 */
public class GlobalSortMapper extends Mapper<Object, Text, Text, Text> {

	private final static Text outKey = new Text();
	private Text outValue = new Text("");

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		outKey.set( value.toString());

		context.write(outKey, outValue);

	}
}