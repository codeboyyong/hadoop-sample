package com.codeboy.hadoop.base;

public interface HadoopClusterManager {
	public abstract boolean startDFS() throws Exception;
	public abstract boolean startJobTracker() throws Exception;
	public abstract boolean startALL() throws Exception;
	
	public abstract boolean stopDFS() throws Exception;
	public abstract boolean stopJobTracker()throws Exception;
	public abstract boolean stopALL()throws Exception;
	
	public abstract   HadoopCluster getHadoopCluster();
	

}
