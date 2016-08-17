package com.qtshe.udf.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ApplySourceUDFTest {
	
	private ApplySourceUDF udf = null ;
	
	@Before
	public void init(){
		 udf = new ApplySourceUDF() ;
	}
	
	/**
	 * 错误的路径测试，输出null
	 */
	@Test
	public void test(){
		
		String path = "ddddddddddddddd" ;
		
		String type = udf.evaluate(path) ;
		Assert.assertNull( type);
		
	}
	
	/**
	 * ...9-11-22.A0...的测试，输出为“搜索报名”
	 */
	@Test
	public void test1(){
		String path = "4-4-11-201-93-9-11-22.A0-201-90-201-201-92-201-90" ;
		
		String type = udf.evaluate(path) ;
		
		Assert.assertEquals("search", type);
		
		//System.out.println(type);
	}
	
	
	/**
	 * ...9-22.A0...,输出为“其他报名”
	 */
	@Test
	public void test2(){
		String path = "4-4-11-201-93-9-22.A0-201-90-201-201-92-201-90" ;
		
		String type = udf.evaluate(path) ;
		
		Assert.assertEquals("other", type);
		
		//System.out.println(type);
		
	}
	
	
	/**
	 * ...9...22.A0...,输出为“其他报名”
	 */
	@Test
	public void test3(){
		String path = "4-4-11-201-93-9-22.A0-201-90-201-201-92-201-90" ;
		
		String type = udf.evaluate(path) ;
		
		Assert.assertEquals("other", type);
	}
	
	/**
	 * ...20...11.20.A0...
	 */
	@Test
	public void test4(){
		String path = "4-4-11-20-201-11-22.A0-201-90-201-201-92-201-90" ;
		
		String type = udf.evaluate(path) ;
		
		Assert.assertEquals("intent", type);
	}
	
	
	
	@Test
	public void test5(){
		String path = "201-52-88-90-92-90-92-120-201-9-10-201-11-201-9-10-9-201-11-201-10-9-9-201-11-201-11-201-11-201-201-61-201-52-88-92-90-120-201-10-9-9-201-11-22.A0-201-159-11-201-11-201-9-10-9-9-201-11-201-10-9-9-9-9-9-201-52-88-92-90-201-52-88-90-92-201-114-105-106-201-100-98-98-193-193-201-99-201-11-201-99-201-52-88-90-92-201-105-114-106-201-100-98-98-193-193-98-201-100-98-98-193-193" ;
		
		String type = udf.evaluate(path) ;
		
		System.out.println(type);
	}
}
