package com.qtshe.udf.test;

import com.aliyun.odps.udf.UDF;
import com.qtshe.udf.apply.ApplyParseUtil;
import com.qtshe.udf.apply.NodeInfo;

@Deprecated
public class ApplySourceUDF extends UDF{
	//------------------------事件类型--------------------------------
	private final String APPLY_EVENT_KEY = "A0" ;  //报名
	
	private final String LABEL_EVENT_KEY = "A1" ;  //标签
	
	private final String SEARCH_EVENT_KEY = "A2" ;  //搜索
	
	//------------------------报名类型-------------------------------
	private final String SEARCH_APPLY = "search" ;   //搜索报名
	
	private final String  INTENT_APPLY = "intent" ;		//意向报名
	
	private final String  SLIDER_APPLY = "slider" ;			//轮播图报名
	
	private final String  STARTUP_APPLY = "startup" ;			//启动页报名
	
	private final String  PUSH_APPLY = "push" ;				//推送报名
	
	private final String  FIND_APPLY = "find" ;					//找兼职报名
	
	private final String OTHER_APPLY = "other" ;			//其他报名
	
	
	
	
	public String  evaluate(String path) {
        if (path == null || path.trim() == "") { return null ; }
        //提取报名成功事件
        int location = path.indexOf( APPLY_EVENT_KEY) ;
        //
        if(location == -1  ){ 
        	return null ;
        }
        if(!ApplyParseUtil.validateEventLocation(location)){
        	return OTHER_APPLY ;  //返回报名来源为“其他”
        }
        
        path = path.substring(0, location-1) ;
        String[] nodes = path.split("-") ;
        
        //接口11即兼职详情接口调用标志
        Boolean partJobViewFlag = false ;
        
        int len = nodes.length ;
        //由于报名事件所在的节点不需参与分析，所以从len-2开始分析
        for(int i=len-2 ; i>-1 ; i--){
        	String nodeStr = nodes[i]; 
        	//提取接口编号和事件列表
        	NodeInfo nodeInfo = ApplyParseUtil.parseNode(nodeStr) ;
        	int interfaceId = nodeInfo.getInterfaceId() ;
        	String events = nodeInfo.getEvents() ;
        	
        	
        	if(interfaceId == 11){
        		partJobViewFlag = true; 
        		continue ;
        	}	
        	if( !partJobViewFlag )
        		continue ;
        	
        	switch(interfaceId){
        		case 20:
        			//返回报名来源为意向报名
                	return INTENT_APPLY ;
        		case 120:
        			//设置为轮播图报名
            		return SLIDER_APPLY ;
        		case 56:
        			//设置启动页报名
            		return STARTUP_APPLY ;
        		case 42:
        			//设置为推送报名
            		return PUSH_APPLY ;
        		case 9:
        			if( i-1 <0){
        				return FIND_APPLY ;
        			}
        			NodeInfo next = ApplyParseUtil.parseNode(nodes[i-1]) ;
        			if( next.getInterfaceId() == 93	){
        				return SEARCH_APPLY ;
        			}
        			if( events != null  && ((events.indexOf(LABEL_EVENT_KEY) != -1) || (events.indexOf(SEARCH_EVENT_KEY) != -1))){
        				return SEARCH_APPLY ;
        			}
        			return FIND_APPLY ;
        		case 93:
        			return SEARCH_APPLY ;
        	}
        	
        }
        return OTHER_APPLY; 
    }
	

}
