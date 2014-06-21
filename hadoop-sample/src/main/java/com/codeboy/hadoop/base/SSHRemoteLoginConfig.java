package com.codeboy.hadoop.base;

public class SSHRemoteLoginConfig {

	private String hostName;
	private String passwd;
	private String userName;

	public SSHRemoteLoginConfig(String sshHostName, String sshUserName,
			String sshPasswd) {
		this.hostName = sshHostName;
		this.userName = sshUserName;
		this.passwd = sshPasswd;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
