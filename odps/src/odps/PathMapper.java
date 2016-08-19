package odps;

import java.io.IOException;

import com.aliyun.odps.counter.Counter;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.MapperBase;

public class PathMapper extends MapperBase {

	private Record uvLogInfo;
	
	private Record key ;
	

	@Override
	public void setup(TaskContext context) throws IOException {
		uvLogInfo = context.createMapOutputValueRecord(); 
		key = context.createMapOutputKeyRecord(); 
	}

	@Override
	public void map(long recordNum, Record record, TaskContext context) throws IOException {
		
		String logId = record.getString(0) ;
		String event = record.getString(1) ;
		String logtime = record.getString(2) ;
		String deviceid = record.getString(3)    ;
		String interfaceId = record.getString(4) ;
		
		
		uvLogInfo.set(0,logtime);
		uvLogInfo.set(1,deviceid);
		uvLogInfo.set(2,interfaceId);
		uvLogInfo.set(3,event);
		uvLogInfo.set(4,logId);
		
		key.setString(0, deviceid);
		key.setString(1, logtime);
		context.write( key, uvLogInfo);

	}

	@Override
	public void cleanup(TaskContext context) throws IOException {
	}

}
