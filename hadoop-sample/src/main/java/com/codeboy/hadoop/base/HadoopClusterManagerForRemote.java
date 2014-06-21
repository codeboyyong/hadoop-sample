package com.codeboy.hadoop.base;


public class HadoopClusterManagerForRemote implements HadoopClusterManager {
	
 
	private SSHRemoteLoginConfig sshConfig;
	private HadoopCommandConfig commandConfig;
	private HadoopCluster hadoopCluster;


	public HadoopClusterManagerForRemote(HadoopCluster hadoopCluster,HadoopCommandConfig commandConfig,SSHRemoteLoginConfig sshConfig){
		this.commandConfig=commandConfig;
		this.sshConfig=sshConfig;
		this.hadoopCluster  = hadoopCluster;
		
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

