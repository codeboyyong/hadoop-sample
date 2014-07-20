package com.codeboy.hadoop.test.a_mr.a_wordcount;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.codeboy.hadoop.util.HadoopFileUtil;


//default is running in the minicluster
/***
 * The basic version, hadoop will auto sorting the key
 * 1) basic one :           com.codeboy.hadoop.test.mr.wordcount
 * 2) descend sort map out : com.codeboy.hadoop.test.mr.wordcount.sortcomparator
 * 3) combiner             : com.codeboy.hadoop.test.mr.wordcount.combiner
 * 4) compress             : com.codeboy.hadoop.test.mr.wordcount.compress
 * 5) composit key
 * 6) group comparator and partitionaer
 * 7)
 * 8) union
 * 9) join
 * 10) sampling  
 * 
 * @author codeboyyong
 * 
 */
public class WordCountTest extends AbstractWordCountTest {

	@Test
	public void testWordCount() throws Exception {
		//this is to print the map to let you know what happens
		super.runWordCountJobMapOnly();
		
		//here check the map out put 
		List<String> maptOutputs = HadoopFileUtil.readAllContentAsList( 
				outputPath + "/part-m-00000",super.getHadoopConfiguration());
		Assert.assertEquals(maptOutputs.size(),28);
 		
		deleteFile( outputPath);
		
		//now run the real job
		super.runWordCountJob();
		LinkedHashMap<String,String> output = HadoopFileUtil.readAllContentAsLinkedMap( 
				outputPath + "/part-r-00000",super.getHadoopConfiguration());
		Set<String> keys = output.keySet(); 
		int i = 0;
		String expectedKeys[] = new String[] {"a","b","c","d","e","f","g"};
		for(String key:keys){
			Assert.assertEquals(expectedKeys[i++],key);
		}
	}
	

}
