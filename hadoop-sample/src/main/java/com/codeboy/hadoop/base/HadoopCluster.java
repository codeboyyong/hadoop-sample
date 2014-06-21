package com.codeboy.hadoop.base;


public class HadoopCluster     {
	
	public enum ClusterType{
		LOCAL,
		MINI,
		REMOTE
	};

	private String clusterName = null;
	private String userName = null;
	private String groupName = null;
	private int hdfsPort;
	private String jobHost;
	private int jobPort;
	private String hdfsHost;
	private SSHRemoteLoginConfig sshConfig;
	private ClusterType type;

	public HadoopCluster(String connName, String userName,
			String groupName, String hdfsHostName, int hdfsPort,
			String jobHostName, int jobPort) {

		this.clusterName = connName;
		this.groupName = groupName;
		this.userName = userName;
		this.hdfsHost = hdfsHostName;
		this.hdfsPort = hdfsPort;
		this.jobHost = jobHostName;
		this.jobPort = jobPort;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#setConnName(java.lang.String)
	 */
	
	public void setConnName(String connName) {
		this.clusterName = connName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#getConnName()
	 */
	
	public String getConnName() {
		return clusterName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#getGroupName()
	 */
	
	public String getGroupName() {
		return groupName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#setGroupName(java.lang.String)
	 */
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#getHdfsHostName()
	 */
	
	public String getHdfsHost() {
		return hdfsHost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#setHdfsHostName(java.lang.String)
	 */
	
	public void setHdfsHost(String hdfsHostName) {
		this.hdfsHost = hdfsHostName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#getHdfsPort()
	 */
	
	public int getHdfsPort() {
		return hdfsPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#setHdfsPort(int)
	 */
	
	public void setHdfsPort(int hdfsPort) {
		this.hdfsPort = hdfsPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#getJobHostName()
	 */
	
	public String getJobHost() {
		return jobHost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#setJobHostName(java.lang.String)
	 */
	
	public void setJobHost(String jobHostName) {
		this.jobHost = jobHostName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#getJobPort()
	 */
	
	public int getJobPort() {
		return jobPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#setJobPort(int)
	 */
	
	public void setJobPort(int jobPort) {
		this.jobPort = jobPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#getUserName()
	 */
	
	public String getUserName() {
		return userName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeboy.hadoop.base.ifc.Hc#setUserName(java.lang.String)
	 */
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public HadoopCluster() {

	}

	
	public String getHDFSUrl() {
		return HadoopConstants.HDFS_PREFIX + getHdfsHost() + ":"
				+ getHdfsPort();

	}

	
	public void setType(ClusterType type) {
		this.type = type;
		
	}

	
	public ClusterType getType() {
 		return this.type;
	}

	
	public void setSSHRemoteLoginConfig(SSHRemoteLoginConfig sshConfig) {
		this.sshConfig =  sshConfig ;
		
	}

	
	public SSHRemoteLoginConfig getSSHRemoteLoginConfig() {
 		return this.sshConfig;
	}

}
