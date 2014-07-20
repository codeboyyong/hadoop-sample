package com.codeboy.hadoop.test.a_mr.a_wordcount.a_combiner;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

import com.codeboy.hadoop.mr.sample.wordcount.WCIntSumReducer.COUNTERS;
import com.codeboy.hadoop.test.a_mr.a_wordcount.AbstractWordCountTest;
import com.codeboy.hadoop.util.HadoopFileUtil;

/***
 * This is combiner version
 * @author codeboyyong
 * 
 */
public class WordCountTestCombiner extends AbstractWordCountTest {

	@Override
	protected   Job createWCJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {	 
		//even you do nothing, hadoop will sort the map output key
		Job job = super.createWCJob(jobConf, inputPath, outputPath);
		job.setCombinerClass(IntSumCombiner.class);
		return job;

	}
	
	
	@Test
	public void testWordCount() throws Exception {
		
		//this is to print the map to let you know what happens
		Job job = super.runWordCountJobMapOnly();
		
		//here check the map out put 
		List<String> maptOutputs = HadoopFileUtil.readAllContentAsList( 
				outputPath + "/part-m-00000",super.getHadoopConfiguration());
		//so there is no such a thing called "in-mapper-combiner"
		//combiner always happens after mapper and before shuffer
		//so here is not 7 but 28, but in the real reduer, there is no need to sum
		Assert.assertEquals(maptOutputs.size(),28);
 
		deleteFile( outputPath);
		//now run the real job
		job = super.runWordCountJob();
		LinkedHashMap<String,String> output = HadoopFileUtil.readAllContentAsLinkedMap( 
				outputPath + "/part-r-00000",super.getHadoopConfiguration());
		Set<String> keys = output.keySet(); 
		int i = 0;
		String expectedKeys[] = new String[] {"a","b","c","d","e","f","g"};
		for(String key:keys){
			Assert.assertEquals(expectedKeys[i++],key);
		}
		
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_COMBINER_A).getValue(),7);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_COMBINER_B).getValue(),6);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_COMBINER_C).getValue(),5);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_COMBINER_D).getValue(),4);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_COMBINER_E).getValue(),3);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_COMBINER_F).getValue(),2);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_COMBINER_G).getValue(),1);
		
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_REDUCER_A).getValue(),1);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_REDUCER_B).getValue(),1);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_REDUCER_C).getValue(),1);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_REDUCER_D).getValue(),1);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_REDUCER_E).getValue(),1);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_REDUCER_F).getValue(),1);
		Assert.assertEquals(job.getCounters().findCounter(COUNTERS.WC_REDUCER_G).getValue(),1);
	}
	

}
