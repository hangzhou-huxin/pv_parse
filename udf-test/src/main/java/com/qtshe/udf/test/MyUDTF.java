package com.qtshe.udf.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;
/**
 * 输出格式：日志编号、日志时间、参数类型、参数
 * 返回格式：日志编号、日志时间、参数名、参数值、参数类型
 * @author Administrator
 *
 */
@Resolve({"string,string,string,string,string,string->string,string,string,string,string,string,string"})
public class MyUDTF  extends UDTF{
	
	

	@Override
	public void process(Object[] args) throws UDFException {
		String logId = (String)args[0] ;
		String logTime = (String)args[1] ;
		String interfaceId = (String)args[2] ;
		String interfacePath = (String)args[3] ;
		String paramType = (String)args[4] ;
		String params = (String)args[5] ;
		try{
			if(StringUtils.isBlank(params)  || "null".equals(params)){
				return ;
			}
			
			Map<String,String> map = MyUDTFUtils.splitParams(params);
			Set<String> keys = map.keySet() ;
			for(String key : keys){
				this.forward(logId,logTime,key ,map.get(key),paramType,interfaceId,interfacePath);
			}
		}catch(Exception e){
			this.forward(logId,logTime,params,e.getMessage(),"error",interfaceId,interfacePath);
		}
		
	}

}
