package com.codeboy.hadoop.test.a_mr.a_wordcount.a_combiner;

import com.codeboy.hadoop.mr.sample.wordcount.IntSumReducer;


public class IntSumCombiner extends IntSumReducer {
	public IntSumCombiner(){
		countprefix = "WC_COMBINER_" ;
	}
 
}