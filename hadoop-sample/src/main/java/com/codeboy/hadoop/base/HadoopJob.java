package com.codeboy.hadoop.base;

import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.io.Writable;

public class HadoopJob     {

	private String jobName;
	
	private Class mapperClass;
	private Class reducerClass;
	private Class combinerClass;

	//default =1, so if you want to 0 you need manually set it to 0
	private int numReduceTasks;

	
	private Class <Writable> outputKeyClass;	
	private Class<Writable> outputValueClass;
	
	//can have multiple input
	private List<String> inputPaths;
	//but only one output
	private String outputPath;
	
	//default test
	private Class<Writable> mapOutputKeyClass;
	private Class<Writable> mapOutputValueClass;
	
	//golbale reucer's input will be sorted by this
	private Class<Comparator<Writable>> reducerKeyComparatorClass;
	
	private Class<Comparator<Writable>> reducerValueComparatorClass;
	
	//partition
	private Class<Comparator<Writable>> partitionerClass;
	
	private Class inputFormatClass;
	private Class outputFormatClass;
	public Class <Writable> getOutputKeyClass() {
		return outputKeyClass;
	}
	public void setOutputKeyClass(Class <Writable> outputKeyClass) {
		this.outputKeyClass = outputKeyClass;
	}

	 

}
