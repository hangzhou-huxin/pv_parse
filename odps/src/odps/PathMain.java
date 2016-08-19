package odps;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.RunningJob;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

public class PathMain {

	public static void main(String[] args) throws OdpsException {
		String inTable = args[0] ;
		String outTable = args[1] ;
		//String mainEvent = args[2] ;
		String date = args[2] ;
		
		JobConf job = new JobConf();

	
		// TODO: specify map output types
		job.setMapOutputKeySchema(SchemaUtils.fromString("device_id:string,logtime:string"));
		job.setMapOutputValueSchema(SchemaUtils.fromString("device_id:string,logtime:string,interface_id:string,event:string,log_id:string"));

		// TODO: specify input and output tables
		System.out.println( "inTable=" + inTable);
		System.out.println("partition=" + date);
		
		
		InputUtils.addTable(TableInfo.builder().tableName(inTable).partSpec(date).build(), job);
		
		OutputUtils.addTable(TableInfo.builder().tableName(outTable).partSpec(date).build(), job);
        
		
		// TODO: specify a mapper
		job.setMapperClass( PathMapper.class);
		// TODO: specify a reducer
		job.setReducerClass(PathReduce.class);
		
		//job.setPartitionerClass(theClass);
		//job.seto
		job.setOutputKeySortColumns(new String[]{"device_id","logtime"}); 
		
		job.setPartitionColumns(new String[]{"device_id"});
		
		job.setOutputGroupingColumns(new String[]{"device_id"});
		
		

		RunningJob rj = JobClient.runJob(job);
		rj.waitForCompletion();
	}

}
