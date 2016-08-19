package com.qtshe.udf.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.qtshe.udf.apply.ApplyParseUtil;
import com.qtshe.udf.apply.ParseResult;

/**
 * 输入格式：device_id、path 
 * 返回格式：device_id、搜索类型、数量
 * 
 * @author Administrator
 *
 */
@Resolve({ "string,string->string,string,string" })
public class ApplySourceNumUDF extends UDTF {

	//private Map<String,Long> map = null ;
	
	

	/*@Override
	public void process(Object[] args) throws UDFException {
		
		 String deviceId = (String)args[0] ; 
		 String path = (String)args[1] ;
		 Map<String,Long> map = ApplyParseUtil.parsePathToMap(deviceId, path) ;
		 Set<String> keys = map.keySet() ;
		 for(String key : keys){
				this.forward(deviceId, key,map.get(key));
		 }

	}*/
	
	
	public void process(Object[] args) throws UDFException {
		
		// String logId = (String)args[0]	 ;
		 String deviceId = (String)args[0] ; 
		 String path = (String)args[1] ;
		 List<ParseResult>	list = ApplyParseUtil.parsePathToList( deviceId, path) ;
		 
		 for(ParseResult result : list){
			 //返回每条记录格式为：logId   deviceId  eventType   ,即每一个报名事件的相关信息
				this.forward(result.getLogId(),deviceId,result.getEventType());
		 }

	}

}
