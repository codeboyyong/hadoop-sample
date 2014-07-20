package com.codeboy.hadoop.test.a_mr.a_wordcount.b_inmapcombiner;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

import com.codeboy.hadoop.mr.sample.wordcount.WCInMapperCombinnerMapper;
import com.codeboy.hadoop.mr.sample.wordcount.WCIntSumReducer.COUNTERS;
import com.codeboy.hadoop.test.a_mr.a_wordcount.AbstractWordCountTest;
import com.codeboy.hadoop.util.HadoopFileUtil;

/***
 * This is combiner version
 * 
 * @author codeboyyong
 * 
 */
public class WordCountTestInMapperCombiner extends AbstractWordCountTest {

	@Override
	protected Job createWCJob(Configuration jobConf, String inputPath,
			String outputPath) throws IOException {
		// even you do nothing, hadoop will sort the map output key
		Job job = super.createWCJob(jobConf, inputPath, outputPath);
		job.setMapperClass(WCInMapperCombinnerMapper.class);
		return job;

	}

	@Test
	public void testWordCount() throws Exception {

		super.runWordCountJob();
		LinkedHashMap<String, String> output = HadoopFileUtil
				.readAllContentAsLinkedMap(outputPath + "/part-r-00000",
						super.getHadoopConfiguration());
		Set<String> keys = output.keySet();
		int i = 0;
		String expectedKeys[] = new String[] { "a", "b", "c", "d", "e", "f",
				"g" };
		for (String key : keys) {
			Assert.assertEquals(expectedKeys[i++], key);
		}

	}

}
