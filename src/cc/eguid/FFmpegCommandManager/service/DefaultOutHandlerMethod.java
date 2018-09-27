package cc.eguid.FFmpegCommandManager.service;


import org.apache.log4j.Logger;

import java.util.Date;

/**
 * 默认任务消息输出处理
 * @author eguid
 * @since jdk1.7
 * @version 2017年10月13日
 */
public class DefaultOutHandlerMethod implements OutHandlerMethod{
	public static final Logger logger = Logger.getLogger(DefaultOutHandlerMethod.class);

	@Override
	public void parse(String type,String msg) {
		//过滤消息
		if (msg.indexOf("[rtsp") != -1) {
			logger.debug( "【丢包信息 : 摄像头： " + type + "发生网络异常丢包，消息体：" + msg + "  】");
		}else if(msg.indexOf("frame=")!=-1){
			logger.info("【推流信息 : 摄像头： " + type + " 推流信息 " + " : " + msg + "  】");
		}
	}
}
