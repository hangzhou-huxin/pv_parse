package com.qtshe.udf.test;

import org.apache.commons.lang.StringUtils;

import com.aliyun.odps.udf.UDF;

public class TestNullUDF extends  UDF{
	
	
	public String evaluate(String s) {
       /* if (s == null) { 
        	return "isNull"; 
        }*/
		
		if(StringUtils.isBlank(s)){
			return "isBlank" ;
		}
        return s.toLowerCase();
    }

}
