package com.qtshe.udf.apply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ApplyParseUtil {

	// ------------------------事件类型--------------------------------
	public final static String APPLY_EVENT_KEY = "A0"; // 报名

	public final static String LABEL_EVENT_KEY = "A1"; // 标签

	public final static String SEARCH_EVENT_KEY = "A2"; // 搜索

	// ------------------------报名类型-------------------------------
	public final static String SEARCH_APPLY = "search"; // 搜索报名

	public final static String INTENT_APPLY = "intent"; // 意向报名

	public final static String SLIDER_APPLY = "slider"; // 轮播图报名

	public final static String STARTUP_APPLY = "startup"; // 启动页报名

	public final static String PUSH_APPLY = "push"; // 推送报名

	public final static String FIND_APPLY = "find"; // 找兼职报名
	
	public final static String LABEL_APPLY = "label" ; //标签报名

	public final static String OTHER_APPLY = "other"; // 其他报名
	
	public final static String  HOME_PAGE_APPLY = "homepage" ;//首页推荐报名
	
	

	/**
	 * 从"接口id.事件1.事件2_日志编号"这样的串中提分别提取出接口id和事件串，以方便后续业务处理
	 * 
	 * @param nodeStr
	 * @return
	 */
	public static NodeInfo parseNode(String nodeStr) {
		NodeInfo nodeInfo = null;
		if (StringUtils.isBlank(nodeStr)) {
			return null;
		}
		int l = nodeStr.indexOf(".");
		if (l == -1) {
			nodeInfo = new NodeInfo();
			nodeInfo.setInterfaceId(Integer.parseInt(nodeStr));
			return nodeInfo;
		}

		Integer interfaceId = Integer.parseInt(nodeStr.substring(0, l));
		String eventAndLogid = nodeStr.substring(l+1);
		//String[] str = nodeStr.split(".");
		
		String events = null ;
		String logid = null ;
		if( eventAndLogid.indexOf( "_" ) != -1){
			String[] eventLogidPair = eventAndLogid.split("_"); 
			events = eventLogidPair[0] ;
			logid = eventLogidPair[1]  ;
		}else{
			events = eventAndLogid ;
		}
		
		
		nodeInfo = new NodeInfo();
		nodeInfo.setInterfaceId(interfaceId);
		nodeInfo.setEvents(events);
		nodeInfo.setLogId(logid);

		return nodeInfo;
	}
	
	
	

	public static Boolean validateEventLocation(int location) {
		// 路径太短，无法有效提取出报名类型
		if (location > 0 && location < 6) {
			return false;
		} else {
			return true;
		}
	}

	private static String parsePath( String path ) {
		
		if( StringUtils.isBlank(path)){
			return OTHER_APPLY  ;
		}

		String logId = null ;
		String[] nodes = null ;
		
		if(path.indexOf("-") != -1){
			nodes = path.split("-");
		}else{
			nodes = new String[]{ path };
		}

		// 接口11即兼职详情接口调用标志
		Boolean partJobViewFlag = false;

		int len = nodes.length;
		
		for (int i = len - 1; i > -1; i--) {
			String nodeStr = nodes[i];
			// 提取接口编号和事件列表
			NodeInfo nodeInfo =  parseNode(nodeStr);
			int interfaceId = nodeInfo.getInterfaceId();
			String events = nodeInfo.getEvents();
			logId = nodeInfo.getLogId() ;
			
			
			if (interfaceId == 11) {
				partJobViewFlag = true;
				continue;
			}
			if (!partJobViewFlag)
				continue;

			switch (interfaceId) {
			case 20:
				// 返回报名来源为意向报名
				return  INTENT_APPLY ;
			case 120:
				// 设置为轮播图报名
				return  SLIDER_APPLY  ;
			case 56:
				// 设置启动页报名
				return  STARTUP_APPLY  ;
			case 42:
				// 设置为推送报名
				return  PUSH_APPLY  ;
			case 125:
				// 首页推荐报名
				return  HOME_PAGE_APPLY   ;
			case 9:
				if (i - 1 < 0) {
					return  FIND_APPLY ;
				}
				NodeInfo next =  parseNode(nodes[i - 1]);
				//先看事件
				if (events != null){
					if(events.indexOf(LABEL_EVENT_KEY) != -1){
						return  LABEL_APPLY  ;
					}else if(events.indexOf(SEARCH_EVENT_KEY) != -1){
						return  SEARCH_APPLY ;
					}
					
				}
				if (next.getInterfaceId() == 93) {
					return  SEARCH_APPLY ;
				}
				return  FIND_APPLY ;
			case 93:
				return  SEARCH_APPLY ;
			}

		}
		return  OTHER_APPLY ; 

	}
	
	
	
	
	private static ParseResult getParseResult(String logId , String deviceId ,String eventType ){
		
		ParseResult result = new ParseResult(logId,deviceId,eventType) ;
		return result ;
	}
	
	/**
	 * 返回剩余的路径
	 * @return
	 */
	private static String  getRemainingPath(int location , String path){
		location = path.indexOf("-",location) ;
		//针对A0出现在最后的情况
		if( location == -1){
			return "" ;
		}
		
		String rest  = path.substring(location + 1) ; 
		return rest ;
	}
	
	
	
	
	
	/**
	 * 入口方法
	 * @param deviceId
	 * @param path
	 * @return
	 */
	public static List<ParseResult> parsePathToList( String deviceId,String path){
		//Map<String,Long> map  = new HashMap<String,Long>() ;
		 
		 List<ParseResult> list = new ArrayList<ParseResult>(); 
		 

		 if (path == null || path.trim() == "") {
				return  null;
		 }
		 
		 while( path.indexOf(ApplyParseUtil.APPLY_EVENT_KEY) != -1){
			 
			 	// 提取报名成功事件
				int location = path.indexOf(ApplyParseUtil.APPLY_EVENT_KEY);
				
				TargetPath targetPath = splitPath(location , path) ;
				
				String eventType = ApplyParseUtil.parsePath( targetPath.getPreParsePath() ) ; 
				if( StringUtils.isNotBlank(eventType) ){
					ParseResult result = getParseResult(targetPath.getNodeInfo().getLogId(),deviceId,eventType) ;
					list.add( result ) ;
				}
				
				//截取掉多余的内容
				path =  targetPath.getRemainingPath() ;
				
				
		 }
		 return list ;
	}
	
	/**
	 * 从path中提取出当前发生事件的节点、待分析的path、剩余的路径
	 * @param location  事件出现的位置
	 * @param path		分解前的path
	 * @return
	 */
	public static TargetPath splitPath(int location , String path){
		
		TargetPath targetPath = null ;
		
		int beginIndex = 0;
		int endIndex = 0;
		 
		for(int i=location;i>0;i--){
			char c = path.charAt(i) ;
			if(c == '-' ){
				//获得事件节点的开始位置和前一节点的结束位置
				beginIndex = i; 
				break ;
			}
		}
		
		int len = path.length() ;
		for(int j=location;j<len;j++){
			char cc = path.charAt(j) ;
			if( cc == '-'){
				//获得事件节点的结束位置和后续节点的开始位置
				endIndex = j ;
				break ;
			}
		}
		
		//如果已经是最后一个节点
		if( endIndex == 0){
			endIndex = path.length() ;
		}else{
			endIndex = endIndex+1 ;
		}
				
		
		String preParsePath = path.substring(0,beginIndex) ; 
		String remainingPath = path.substring( endIndex) ; 
		
		
		NodeInfo node = null ;
		if( beginIndex > 0){
			beginIndex = beginIndex + 1 ;
		}
		node = parseNode(path.substring(beginIndex, endIndex-1)) ;
		
		 
		
		targetPath  = new TargetPath() ;
		targetPath.setNodeInfo(node);
		targetPath.setPreParsePath(preParsePath);
		targetPath.setRemainingPath(remainingPath);
		
		return targetPath; 
	}
	

}
