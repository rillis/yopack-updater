package com;

public class Update {
	Integer versionNumber;
	String url;
	String[] filesToRemove;
	
	public Update(Integer versionNumber, String url, String[] filesToRemove) {
		this.versionNumber = versionNumber;
		this.url = url;
		this.filesToRemove = filesToRemove;
	}
}
