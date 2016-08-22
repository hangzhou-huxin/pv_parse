package com.qtshe.udf.apply;

public class NodeInfo {
	
	@Override
	public String toString() {
		return "NodeInfo [interfaceId=" + interfaceId + ", events=" + events + ", logId=" + logId + "]";
	}


	private Integer interfaceId ;
	
	
	private String  events ;
	
	
	private String  logId ;


	public Integer getInterfaceId() {
		return interfaceId;
	}


	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}


	public String getEvents() {
		return events;
	}


	public void setEvents(String events) {
		this.events = events;
	}


	public String getLogId() {
		return logId;
	}


	public void setLogId(String logId) {
		this.logId = logId;
	}


	
	
	
	

}
