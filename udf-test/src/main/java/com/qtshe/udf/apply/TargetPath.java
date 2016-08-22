package com.qtshe.udf.apply;

public class TargetPath {
	
	private String preParsePath ;
	
	
	private NodeInfo nodeInfo ;
	
	
	private String remainingPath ;


	public String getPreParsePath() {
		return preParsePath;
	}


	public void setPreParsePath(String preParsePath) {
		this.preParsePath = preParsePath;
	}


	public NodeInfo getNodeInfo() {
		return nodeInfo;
	}


	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}


	public String getRemainingPath() {
		return remainingPath;
	}


	public void setRemainingPath(String remainingPath) {
		this.remainingPath = remainingPath;
	}


	@Override
	public String toString() {
		return "TargetPath [preParsePath=" + preParsePath + ", nodeInfo=" + nodeInfo + ", remainingPath="
				+ remainingPath + "]";
	}
	
	
	
	

}
