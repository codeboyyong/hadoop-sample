package com.codeboy.hadoop.test.a_mr.b_advance;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

// Compares the composite key
//see 	//https://gist.github.com/geofferyzh/3489292

public class CompositeKeyComparator extends WritableComparator {

	/* s Constructor. */
	protected CompositeKeyComparator() {
		super(Text.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		Text k1 = (Text) w1;
		Text k2 = (Text) w2;

		String[] k1Items = k1.toString().split(":");
		String[] k2Items = k2.toString().split(":");

		//k1Items[0] is the column name
		String k1Base = k1Items[1];
		String k2Base = k2Items[1];

		int comp = k1Base.compareTo(k2Base);

		return comp;
	}
}