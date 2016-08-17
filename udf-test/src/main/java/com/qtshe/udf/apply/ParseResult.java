package com.qtshe.udf.apply;

public class ParseResult {
	
	public ParseResult(String logId, String event, String deviceId) {
		super();
		this.logId = logId;
		this.event = event;
		this.deviceId = deviceId;
	}

	private String logId ;
	
	
	private String  event ;
	
	
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

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
	

}
