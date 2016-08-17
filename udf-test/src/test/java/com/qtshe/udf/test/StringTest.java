package com.qtshe.udf.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class StringTest {
	
	@Test
	public void test(){
		
		
		
		String req = "#requesst#{\"appKey\":[\"QTSHE_ANDROID_USER\"],\"deviceId\":[\"862183037582013~8a393018372f39df\"],\"token\":[\"2d2d8304d4ee85b089f2c954c72ca8af\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]}#requesst#"  ;
		
		int s = req.length() ;
		int l = "#requesst#".length() ;
		
		System.out.println(req.substring(l, s-l)) ;
	}
	
	
	@Test
	public void test1(){
		
		//String reqParams = "#requesst#{\"appKey\":[\"QTSHE_ANDROID_USER\"],\"deviceId\":[\"862183037582013~8a393018372f39df\"],\"token\":[\"2d2d8304d4ee85b089f2c954c72ca8af\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]}#requesst#"  ;
		//String reqParams = "{\"appKey\":[\"QTSHE_ANDROID_USER\"],\"deviceId\":[\"869154023828143~e232815b40a91df8\"],\"token\":[\"d5f7f414b226ffd15caff9bce4777611\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]}" ;
		String reqParams = "{\"appKey\":[\"QTSHE_IOS_USER\"],\"deviceId\":[\"CCB1E5A4-6EA7-4F88-AABB-420616749FE1\"],\"token\":[\"996fb29642320b2493ff8ace85c97d00\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]}" ;
		if(StringUtils.isBlank(reqParams)  || "null".equals(reqParams)){
			return ;
		}
		JSONObject params = JSON.parseObject(reqParams) ;
		System.out.println("----------------------------------------------------------------------------");
		Set<String> keys = params.keySet() ;
		for(String key : keys){
			System.out.println(key + ":" + params.get(key));
		}
		
	}
	
	
	@Test
	public void test2(){
		
		//String reqParams = "#requesst#{\"appKey\":[\"QTSHE_ANDROID_USER\"],\"deviceId\":[\"862183037582013~8a393018372f39df\"],\"token\":[\"2d2d8304d4ee85b089f2c954c72ca8af\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]}#requesst#"  ;
		//String reqParams = "{\"appKey\":[\"QTSHE_ANDROID_USER\"],\"deviceId\":[\"869154023828143~e232815b40a91df8\"],\"token\":[\"d5f7f414b226ffd15caff9bce4777611\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]}" ;
		String reqParams = "{\"t\":[\"1\"]}" ;
		if(StringUtils.isBlank(reqParams)  || "null".equals(reqParams)){
			return ;
		}
		JSONObject params = JSON.parseObject(reqParams) ;
		System.out.println("----------------------------------------------------------------------------");
		Set<String> keys = params.keySet() ;
		for(String key : keys){
			System.out.println(key + ":" + params.get(key));
		}
		
	}
	
	/**
	 * 请求参数的解析
	 */
	@Test
	public void test3(){
		
		Map<String,String> map = new HashMap<String,String>() ; 
		
		
		
		StringBuilder reqParams = new StringBuilder("\"appKey\":[\"QTSHE_ANDROID_USER\"],\"deviceId\":[\"869154023828143~e232815b40a91df8\"],\"token\":[\"d5f7f414b226ffd15caff9bce4777611\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]") ;
		 
		
		int flag = -1 ;
		int colonflag = -1 ;   //冒号标志位置
		
		StringBuilder buffer = new StringBuilder() ;
		StringBuilder key = new StringBuilder(); 
		StringBuilder value = new StringBuilder(); 
		
		while( ( flag = reqParams.indexOf("],") ) !=-1){
			//清空临时缓冲区
			buffer.delete(0, buffer.length()) ;
			//设置缓冲区内容
			buffer.append(reqParams.substring(0, flag)) ;
			//取出第一个“：号”的位置
			colonflag = reqParams.indexOf(":") ;
			if(colonflag != -1){
				key.delete(0, key.length()) ;
				value.delete(0, value.length()) ;
				
				key.append( buffer.substring(1, colonflag-1).toString()) ;
				value.append( buffer.substring(colonflag+3 ,buffer.length()-1).toString()) ;
				
				map.put(key.toString(), value.toString()) ;
			}
			reqParams.delete(0, flag + 2) ;
		}
		
		
		buffer.delete(0, buffer.length()) ;
		//设置缓冲区内容
		buffer.append(reqParams) ;
		//取出第一个“：号”的位置
		colonflag = reqParams.indexOf(":") ;
		if(colonflag != -1){
			key.delete(0, key.length()) ;
			value.delete(0, value.length()) ;
			
			key.append( buffer.substring(1, colonflag-1).toString()) ;
			value.append( buffer.substring(colonflag+3 ,buffer.length()-2).toString()) ;
			
			map.put(key.toString(), value.toString()) ;
		}
		//reqParams.delete(0, flag + 2) ;
		
		
		System.out.print(map);
		
	}
	
	
	@Test
	public void test4(){
		
		String reqParams = "{\"appKey\":[\"QTSHE_ANDROID_USER\"],\"deviceId\":[\"869154023828143~e232815b40a91df8\"],\"token\":[\"d5f7f414b226ffd15caff9bce4777611\"],\"townId\":[\"87\"],\"version\":[\"3.4.2\"]}" ;
		
		
		Map<String,String> map = MyUDTFUtils.splitParams( reqParams) ;
		
		System.out.println( map);
	}
	
	@Test
	public void test5(){
		
		String resParams = "{\"errMsg\":\"\u8BB0\u5F55\u6210\u529F\uFF01\",\"success\":true}" ;
		Map<String,String> map = MyUDTFUtils.splitParams( resParams) ;
		
		System.out.println( map);
		
	}
	
	
	/**
	 * 使用正则式提取特定参数的值 
	 */
	@Test
	public void test6(){
		
	
		
		  String regex = "^\"errMsg\":\"([^\"]*)";
			Pattern pattern = Pattern.compile(regex);
			String log = "{\"errMsg\":\"\u8BB0\u5F55\u6210\u529F\uFF01\",\"success\":true}";
			
			Matcher matcher = pattern.matcher(log);

			if (matcher.matches()) {
				for (int i = 0; i < matcher.groupCount(); i++) {
					System.out.println(matcher.group(i + 1));
				}

			} else {
				System.out.println("No match found.%n");
			}
		
	}
	
	
	/**
	 * 
	 */
	@Test
	public void test7(){
		String str = "{\"errMsg\":\"\u8BB0\u5F55\u6210\u529F\uFF01\",\"success\":true}";
		int errFlag = str.indexOf("\"errMsg\":") ;
		int successFlag = str.indexOf("\"success\":") ;
		
		StringBuilder buffer = new StringBuilder() ;
		buffer.append(str.substring(errFlag + 10) );
		int end = buffer.indexOf("\""); 
		String errMsg = buffer.substring(0, end) ;
		
		System.out.println( errMsg );
	}
	
	@Test
	public void test8(){
		String str = "{\"errMsg\":\"\u8BB0\u5F55\u6210\u529F\uFF01\",\"success\":true}";
		Map map = MyUDTFUtils.splitResponseParams(str) ;
		
		System.out.println( map );
	}
	

}
