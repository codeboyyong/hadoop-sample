package com.codeboy.hadoop.base;


public class HadoopClusterManagerForLocal implements HadoopClusterManager {
	
	private HadoopCommandConfig commandConfig;
	private HadoopCluster hadoopCluster;

	public HadoopClusterManagerForLocal(HadoopCluster hadoopCluster,  HadoopCommandConfig   commandConfig){
		  this.hadoopCluster = hadoopCluster;
		  this.commandConfig = commandConfig;
	}

	@Override
	public boolean startDFS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startJobTracker() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startALL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopDFS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopJobTracker() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopALL() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public HadoopCluster getHadoopCluster() {
 		return this.hadoopCluster;
	}

}
