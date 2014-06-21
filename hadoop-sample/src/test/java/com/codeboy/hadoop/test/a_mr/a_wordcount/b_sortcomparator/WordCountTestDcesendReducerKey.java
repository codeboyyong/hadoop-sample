package com.codeboy.hadoop.test.a_mr.a_wordcount.b_sortcomparator;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.After;
import org.junit.Test;

import com.codeboy.hadoop.test.a_mr.a_wordcount.AbstractWordCountTest;
import com.codeboy.hadoop.util.HadoopFileUtil;

/***
 * The basic version, hadoop will auto sorting the key
 * @author codeboyyong
 * 
 */
public class WordCountTestDcesendReducerKey extends AbstractWordCountTest {


		@Override
		protected   Job createWCJob(Configuration jobConf, String inputPath,
				String outputPath) throws IOException {
			Job job = super.createWCJob(jobConf, inputPath, outputPath);
			
			//this is difference
 			job.setSortComparatorClass(StringDescendComparator.class); 
			return job;
		}
		
		@Test
		public void testWordCount() throws Exception {
			super.runWordCountJob();
			LinkedHashMap<String,String> output = HadoopFileUtil.readAllContentAsLinkedMap( 
					outputPath + "/part-r-00000",super.getHadoopConfiguration());
			Set<String> keys = output.keySet(); 
			int i = 0;
			String expectedKeys[] = new String[] {"g","f","e","d","c","b","a"};
			for(String key:keys){
				Assert.assertEquals(expectedKeys[i++],key);
			}
		}

}
