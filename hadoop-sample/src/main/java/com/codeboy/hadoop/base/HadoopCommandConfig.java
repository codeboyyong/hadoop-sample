package com.codeboy.hadoop.base;

public class HadoopCommandConfig {
	private String startDFSCommand;
	private String startJobTraker;
	private String stopDFSCommand;
	private String stopJobTracker;
	
	
	
	public HadoopCommandConfig(String startDFSCommand,
			String stopDFSCommand,  String startJobTraker,String stopJobTracker) {
		super();
		this.startDFSCommand = startDFSCommand;
		this.startJobTraker = startJobTraker;
		this.stopDFSCommand = stopDFSCommand;
		this.stopJobTracker = stopJobTracker;
	}
	public String getStartDFSCommand() {
		return startDFSCommand;
	}
	public void setStartDFSCommand(String startDFSCommand) {
		this.startDFSCommand = startDFSCommand;
	}
	public String getStartJobTraker() {
		return startJobTraker;
	}
	public void setStartJobTraker(String startJobTraker) {
		this.startJobTraker = startJobTraker;
	}
	public String getStopDFSCommand() {
		return stopDFSCommand;
	}
	public void setStopDFSCommand(String stopDFSCommand) {
		this.stopDFSCommand = stopDFSCommand;
	}
	public String getStopJobTracker() {
		return stopJobTracker;
	}
	public void setStopJobTracker(String stopJobTracker) {
		this.stopJobTracker = stopJobTracker;
	}

}
