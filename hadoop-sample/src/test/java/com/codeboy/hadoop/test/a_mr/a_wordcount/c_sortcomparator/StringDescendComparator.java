package com.codeboy.hadoop.test.a_mr.a_wordcount.c_sortcomparator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class StringDescendComparator extends WritableComparator {

		protected StringDescendComparator() {
			//this Text.Class == mapoutput class
			super(Text.class, true);
		}
		
 	 
		@Override
		public int compare(WritableComparable w1, WritableComparable w2) {
			Text k1 = (Text)w1;
			Text k2 = (Text)w2;
			//notice: here is the descending
			return -1 * k1.compareTo(k2);
		}
	 
	 

}
