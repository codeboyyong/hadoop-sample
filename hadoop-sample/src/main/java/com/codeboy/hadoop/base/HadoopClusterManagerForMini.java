package com.codeboy.hadoop.base;

import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.security.UserGroupInformation;

import com.codeboy.hadoop.util.HadoopClusterUtil;

public class HadoopClusterManagerForMini implements HadoopClusterManager {

	private HadoopCluster hadoopCluster;

	private MiniDFSCluster dfsCluster = null;
	//this is only for mrv1
	private MiniMRCluster mrCluster = null;

	public HadoopClusterManagerForMini(HadoopCluster hadoopCluster) {
		this.hadoopCluster = hadoopCluster;
	}

	@Override
	public boolean startDFS() throws Exception {
		if (dfsCluster == null) {
//			dfsCluster = new MiniDFSCluster.Builder(
//					HadoopClusterUtil.toHadoopConfiguration(hadoopCluster)).build();
			dfsCluster = new  MiniDFSCluster(hadoopCluster.getHdfsPort(), 
					HadoopClusterUtil.toHadoopConfiguration(hadoopCluster) , 2, true, true, null, null);
		}
		return true;
	}

	@Override
	public boolean startJobTracker() throws Exception {
		if (mrCluster == null) {
			String namenodeURL = HadoopClusterUtil.toNameNodeURL(hadoopCluster);
			JobConf conf = new JobConf(
					HadoopClusterUtil.toHadoopConfiguration(hadoopCluster));
			UserGroupInformation ugi = UserGroupInformation
					.createRemoteUser(hadoopCluster.getUserName());
			ugi.setConfiguration(conf);
			// public MiniMRCluster(int jobTrackerPort, int taskTrackerPort,
			// int numTaskTrackers, String namenode, int numDir, String[] racks,
			// String[] hosts, UserGroupInformation ugi, JobConf conf)
			mrCluster = new MiniMRCluster(hadoopCluster.getJobPort(), 0, 1,
					namenodeURL, 1, null, null, ugi, conf);

		}
		return true;
	}

	@Override
	public boolean startALL() throws Exception {
		this.startDFS();
		this.startJobTracker();
		return true;
	}

	@Override
	public boolean stopDFS() {
		if (dfsCluster != null) {
			dfsCluster.shutdown();
			dfsCluster = null;
		}
		return false;
	}

	@Override
	public boolean stopJobTracker() {
		
		if (mrCluster != null) {
			mrCluster.shutdown();
			mrCluster = null;
		}
		return false;
	}

	@Override
	public boolean stopALL() {
		this.stopDFS(); 
		this.stopJobTracker(); 
		return false;
	}

	@Override
	public HadoopCluster getHadoopCluster() {
 		return this.hadoopCluster;
	}

}
