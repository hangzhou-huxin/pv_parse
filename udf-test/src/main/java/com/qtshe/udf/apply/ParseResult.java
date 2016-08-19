package com.qtshe.udf.apply;

public class ParseResult {
	
	public ParseResult(String logId , String deviceId , String eventType) {
		super();
		this.logId = logId;
		this.eventType = eventType;
		this.deviceId = deviceId;
	}

	private String logId ;
	
	
	private String  eventType ;
	
	
	private String deviceId ;
	
	/**
	 * 报名次数
	 */
	//private Integer num ;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "ParseResult [logId=" + logId + ", eventType=" + eventType + ", deviceId=" + deviceId + "]";
	}

	
	

}
