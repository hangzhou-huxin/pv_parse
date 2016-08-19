package odps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.ReducerBase;

public class PathReduce extends ReducerBase {

	private Record result = null;

	@Override
	public void setup(TaskContext context) throws IOException {
		result = context.createOutputRecord();
	}

	@Override
	public void reduce(Record key, Iterator<Record> values, TaskContext context) throws IOException {

		Boolean flag = false;
		String beginTime = null;
		String endTime = null;
		String logId = null;
		// String deviceid = null ;
		String deviceid = key.getString(0);
		String interfaceId = null;
		String event = null;

		if ("-1".equals(deviceid))
			return;
		// Stack<String> stack = new Stack<String>() ;

		StringBuilder pathBuffer = new StringBuilder();

		while (values.hasNext()) {
			Record val = values.next();

			//deviceid = val.getString(1);
			interfaceId = val.getString(2);
			event = val.getString(3);
			logId = val.getString(4);

			// flag为false表示还未开始，则可以设置开始时间
			if (!flag) {
				beginTime = val.getString(0);
				endTime = val.getString(0);
				flag = true;
			} else {
				endTime = val.getString(0);
			}

			// 如果事件不能匹配，则忽略当前的这条日志记录
			if ( StringUtils.isBlank(interfaceId)) {
				continue;
			} else {
				pathBuffer.append(interfaceId);
			}

			if (StringUtils.isBlank(event)){
				pathBuffer.append("-") ;
				continue;
			}else{
				// 如果某个节点上存在事件，则该节点需要加入logId,格式如：接口编号.事件key1.事件key2_日志编号
				pathBuffer.append(".").append(event);
				// 只有主事件出现才会加入logid
				if (event.indexOf(Constant.MAIN_EVENT_KEY) != -1) {
					pathBuffer.append("_").append(logId);
				}
			}
			
			pathBuffer.append("-") ;
		}
		//如果出现这种情况表示interfaceId为空,pathBuffer中未追加任何内容
		if(pathBuffer.length() == 0){
			return ;   
		}
		result.set(0, deviceid);
		result.set(1, beginTime);
		result.set(2, endTime);
		result.set(3, pathBuffer.substring(0, (pathBuffer.length()-1) ));

		context.write(result);
	}

	@Override
	public void cleanup(TaskContext context) throws IOException {
	}

}
