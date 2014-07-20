package com.codeboy.hadoop.test.a_mr.a_wordcount.a_combiner;

import com.codeboy.hadoop.mr.sample.wordcount.WCIntSumReducer;


public class IntSumCombiner extends WCIntSumReducer {
	public IntSumCombiner(){
		countprefix = "WC_COMBINER_" ;
	}
 
}