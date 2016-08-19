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
		String eventAndLogid = nodeStr.substring(l);
		//String[] str = nodeStr.split(".");
		
		String[] eventLogidPair = eventAndLogid.split("_"); 
		String events = eventLogidPair[0] ;
		String logid = eventLogidPair[1]  ;
		
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

	private static ParseResult parsePath(String deviceId ,String path) {

		String logId = null ;
		String[] nodes = path.split("-");

		// 接口11即兼职详情接口调用标志
		Boolean partJobViewFlag = false;

		int len = nodes.length;
		// 由于报名事件所在的节点不需参与分析，所以从len-2开始分析
		for (int i = len - 2; i > -1; i--) {
			String nodeStr = nodes[i];
			// 提取接口编号和事件列表
			NodeInfo nodeInfo =  parseNode(nodeStr);
			int interfaceId = nodeInfo.getInterfaceId();
			String events = nodeInfo.getEvents();
			logId = nodeInfo.getLogId() ;
			
			if(!StringUtils.isBlank( events )){
				
			}

			if (interfaceId == 11) {
				partJobViewFlag = true;
				continue;
			}
			if (!partJobViewFlag)
				continue;

			switch (interfaceId) {
			case 20:
				// 返回报名来源为意向报名
				return getParseResult( logId , deviceId ,INTENT_APPLY );
			case 120:
				// 设置为轮播图报名
				return getParseResult( logId , deviceId ,SLIDER_APPLY );
			case 56:
				// 设置启动页报名
				return getParseResult( logId , deviceId ,STARTUP_APPLY );
			case 42:
				// 设置为推送报名
				return getParseResult( logId , deviceId ,PUSH_APPLY );
			case 125:
				// 首页推荐报名
				return getParseResult( logId , deviceId ,HOME_PAGE_APPLY ) ;
			case 9:
				if (i - 1 < 0) {
					return getParseResult( logId , deviceId ,FIND_APPLY );
				}
				NodeInfo next =  parseNode(nodes[i - 1]);
				//先看事件
				if (events != null){
					if(events.indexOf(LABEL_EVENT_KEY) != -1){
						return getParseResult( logId , deviceId ,LABEL_APPLY ) ;
					}else if(events.indexOf(SEARCH_EVENT_KEY) != -1){
						return getParseResult( logId , deviceId ,SEARCH_APPLY );
					}
					
				}
				if (next.getInterfaceId() == 93) {
					return getParseResult( logId , deviceId ,SEARCH_APPLY );
				}
				return getParseResult( logId , deviceId ,FIND_APPLY );
			case 93:
				return getParseResult( logId , deviceId ,SEARCH_APPLY ) ;
			}

		}
		return getParseResult( logId , deviceId ,OTHER_APPLY); 

	}
	
	/*private static void add(String event, Map<String,Long> map){
		Long i = map.get(event)  ;
		if(i == null){
			map.put(event, 1l) ;
		}else{
			map.put(event, ++i) ;
		}
	}*/
	
	
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
	
	
	/*public static Map<String,Long> parsePathToMap(String deviceId,String path){
		Map<String,Long> map  = new HashMap<String,Long>() ;
		 
		 
		 

		 if (path == null || path.trim() == "") {
				return  null;
		 }
		 
		 while( path.indexOf(ApplyParseUtil.APPLY_EVENT_KEY) != -1){
			 
			 	// 提取报名成功事件
				int location = path.indexOf(ApplyParseUtil.APPLY_EVENT_KEY);
				
				if (!ApplyParseUtil.validateEventLocation(location)) {
					add( ApplyParseUtil.OTHER_APPLY,map); // 添加报名来源为“其他”
					
					path = getRemainingPath(location,path) ;
					
					continue ;
				}
				
				String event = ApplyParseUtil.parsePath(path.substring(0, location - 1)) ; 
				if(event != null){
					add(event,map) ;
				}
				//截取掉多余的内容
				path =  getRemainingPath(location ,path) ;
				location = path.indexOf("-",location) ;
				//针对A0出现在最后的情况
				if( location == -1){
					break ;
				}
				path = path.substring(location + 1) ; 
		 }
		 return map ;
	}*/
	
	
	
	public static List<ParseResult> parsePathToList( String deviceId,String path){
		//Map<String,Long> map  = new HashMap<String,Long>() ;
		 
		 List<ParseResult> list = new ArrayList<ParseResult>(); 
		 

		 if (path == null || path.trim() == "") {
				return  null;
		 }
		 
		 while( path.indexOf(ApplyParseUtil.APPLY_EVENT_KEY) != -1){
			 
			 	// 提取报名成功事件
				int location = path.indexOf(ApplyParseUtil.APPLY_EVENT_KEY);
				
				/*if (!ApplyParseUtil.validateEventLocation(location)) {
					//add( ApplyParseUtil.OTHER_APPLY,map); // 添加报名来源为“其他”
					list.add(getParseResult( deviceId ,ApplyParseUtil.OTHER_APPLY)) ;
					
					path = getRemainingPath(location,path) ;
					
					continue ;
				}*/
				
				ParseResult result = ApplyParseUtil.parsePath(deviceId ,path.substring(0, location - 1)) ; 
				if(result.getEventType() != null){
					//add(event,map) ;
					list.add( result) ;
				}
				//截取掉多余的内容
				path =  getRemainingPath(location ,path) ;
				/*location = path.indexOf("-",location) ;
				//针对A0出现在最后的情况
				if( location == -1){
					break ;
				}
				path = path.substring(location + 1) ; */
		 }
		 return list ;
	}
	

}
