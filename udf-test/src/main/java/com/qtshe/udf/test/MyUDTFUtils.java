package com.qtshe.udf.test;

import java.util.HashMap;
import java.util.Map;

public class MyUDTFUtils {
	
	/**
	 * 解析响应参数
	 * @param str
	 * @return
	 */
	public static Map<String,String> splitResponseParams( String str){
		
		//String str = "{\"errMsg\":\"\u8BB0\u5F55\u6210\u529F\uFF01\",\"success\":true}";
		int errFlag = str.indexOf("\"errMsg\":") ;
		int successFlag = str.indexOf("\"success\":") ;
		
		Map<String,String> map = new HashMap<String,String>() ; 
		StringBuilder buffer = new StringBuilder() ;
		int end = 0 ;
		if( errFlag != -1){
			buffer.append(str.substring(errFlag + 10) );
			end = buffer.indexOf("\""); 
			String errMsg = buffer.substring(0, end) ;
			map.put("errMsg", errMsg)	; 
			buffer.delete(0, buffer.length()) ;
		}
		
		if( successFlag != -1){
			buffer.append(str.substring(successFlag + 10)) ;
			
			int i = buffer.indexOf("}") ;
			int j = buffer.indexOf(",") ;
			
			end = (i<0 || j<0)?Math.max(i, j):Math.min(i, j) ;
			String success = buffer.substring(0, end) ;
			map.put("success", success)	; 
			buffer.delete(0, buffer.length()) ;
		}
		
		return map ;
		
	}
	
	/**
	 * 解析请求参数
	 * @param str
	 * @return
	 */
	public static Map<String,String> splitParams( String str){
		
		
		
		Map<String,String> map = new HashMap<String,String>() ; 
		
		
		
		StringBuilder params = new StringBuilder( str.substring(1, str.length()-1)) ;
		 
		
		int flag = -1 ;
		int colonflag = -1 ;   //冒号标志位置
		
		StringBuilder buffer = new StringBuilder() ;
		StringBuilder key = new StringBuilder(); 
		StringBuilder value = new StringBuilder(); 
		
		while( ( flag = params.indexOf("],") ) !=-1){
			//清空临时缓冲区
			buffer.delete(0, buffer.length()) ;
			//设置缓冲区内容
			buffer.append(params.substring(0, flag)) ;
			//取出第一个“：号”的位置
			colonflag = params.indexOf(":") ;
			if(colonflag != -1){
				key.delete(0, key.length()) ;
				value.delete(0, value.length()) ;
				
				key.append( buffer.substring(1, colonflag-1).toString()) ;
				value.append( buffer.substring(colonflag+3 ,buffer.length()-1).toString()) ;
				
				map.put(key.toString(), value.toString()) ;
			}
			params.delete(0, flag + 2) ;
		}
		
		
		buffer.delete(0, buffer.length()) ;
		//设置缓冲区内容
		buffer.append(params) ;
		//取出第一个“：号”的位置
		colonflag = params.indexOf(":") ;
		if(colonflag != -1){
			key.delete(0, key.length()) ;
			value.delete(0, value.length()) ;
			
			key.append( buffer.substring(1, colonflag-1).toString()) ;
			value.append( buffer.substring(colonflag+3 ,buffer.length()-2).toString()) ;
			
			map.put(key.toString(), value.toString()) ;
		}
		
		params = null ;
		buffer = null ;
		key = null ;
		value = null ;
		
		
		return map ;
	}

}
