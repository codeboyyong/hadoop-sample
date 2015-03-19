package com.codeboy.hcatalog;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hive.hcatalog.data.DefaultHCatRecord;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.schema.HCatSchema;
import org.apache.hive.hcatalog.mapreduce.HCatInputFormat;
import org.apache.hive.hcatalog.mapreduce.HCatOutputFormat;
import org.apache.hive.hcatalog.mapreduce.OutputJobInfo;

/**
 * 
 * @author zhaoyong
 *
 */
public class HCatalogColumnFilter extends Configured implements Tool {

    private static final String COLUMN_INDEX = "COLUMN_INDEX";

	public static class Map extends Mapper<WritableComparable, HCatRecord, HCatRecord, NullWritable> {
        String groupname;
		private int[] columnNameIndex; 
		StringBuilder sb = new StringBuilder();
		Text output= new Text();
        
		@Override
      protected void map( WritableComparable key,
                          HCatRecord value,
                          org.apache.hadoop.mapreduce.Mapper<WritableComparable, HCatRecord,
                          HCatRecord, NullWritable>.Context context)
            throws IOException, InterruptedException {
			   HCatRecord record = new DefaultHCatRecord(columnNameIndex.length);
        	 
        		for(int i :columnNameIndex){ 
        			record.set(i, value.get(i));
        		}
 
            context.write( record, NullWritable.get());
        }

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
 			super.setup(context);
			String[] strIndex = context.getConfiguration().get(COLUMN_INDEX).split(",") ;
			this.columnNameIndex= new int[strIndex.length] ;
			for (int i = 0; i < strIndex.length; i++) {
				columnNameIndex[i] = Integer.valueOf(strIndex[i])  ;
			} 
		}
        
        
    }

 

    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        args = new GenericOptionsParser(conf, args).getRemainingArgs();

        // Get the input and output table names as arguments
        String inputTableName = args[0];
        String outputTableName = args[1];
        String columnNames = args[2];
         
        // Assume the default database
        String dbName = null;

        Job job =   Job.getInstance( conf, "HCatalogColumnFilter");
        
        //dbName = SchemaName
        HCatInputFormat.setInput(job, dbName, inputTableName);
       
        HCatSchema inputSchema = HCatInputFormat.getTableSchema(job.getConfiguration()) ; 
        System.err.println("INFO: input schema is :" +inputSchema);
        
        conf.set(COLUMN_INDEX ,  getColumnIndex(columnNames,inputSchema)); 

        inputSchema.getFieldNames();
        job.setJarByClass(HCatalogColumnFilter.class);
        job.setMapperClass(Map.class);
 
        // An HCatalog record as input
        job.setInputFormatClass(HCatInputFormat.class);

        // Ignore the key for the reducer output; emitting an HCatalog record as value
        job.setOutputKeyClass(DefaultHCatRecord.class);
        job.setOutputValueClass(NullWritable.class);
        
     // An HCatalog record as output
        job.setOutputFormatClass(HCatOutputFormat.class);

        HCatOutputFormat.setOutput(job, OutputJobInfo.create(dbName, outputTableName, null));
        
        /**will auto connect to HCatalog to get the table column information as the output schema information
        this will work because the output table is alreasy exists!!! */
        
        HCatSchema outputSchema = HCatOutputFormat.getTableSchema(job.getConfiguration());
         System.err.println("INFO: output schema explicitly set for writing:" + outputSchema);
        
        HCatOutputFormat.setSchema(job, outputSchema);
        
        return (job.waitForCompletion(true) ? 0 : 1);
    }

    private String getColumnIndex(String columnNames, HCatSchema inputSchema) {
    		String[] names = columnNames.split(",")  ;
    		List<Integer> resultLists = new ArrayList();
    		for(String colName:names){
    			resultLists.add(inputSchema.getFieldNames().indexOf(colName)) ;
    		}
    		Collections.sort(resultLists); 
		return resultLists.toString().replace( " ", "" ).replace("[", "").replace("]", "");  
	}

	public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new HCatalogColumnFilter(), args);
        System.exit(exitCode);
    }
}