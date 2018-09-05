package cc.eguid.FFmpegCommandManager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import cc.eguid.FFmpegCommandManager.FFmpegManager;
import cc.eguid.FFmpegCommandManager.dao.TaskDao;
import cc.eguid.FFmpegCommandManager.entity.TaskEntity;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;


/**
 * 任务消息输出处理器
 *
 * @author eguid
 * @version 2017年10月13日
 * @since jdk1.7
 */
public class OutHandler extends Thread {
    public static final Logger logger = Logger.getLogger(OutHandler.class);


    private Runtime runtime = null;

    /**
     * 任务执行处理器
     */
    private TaskHandler taskHandler = null;

    /**
     * 任务持久化器
     */
    private TaskDao taskDao = null;

    /**
     * 控制状态
     */
    private volatile boolean desstatus = true;

    /**
     * 读取输出流
     */
    private BufferedReader br = null;

    /**
     * 输出类型
     */
    private String type = null;

    /**
     * 重新启动命令
     */
    public static String command = null;

    /**
     * 消息处理方法
     */
    private OutHandlerMethod ohm;

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public void setDesStatus(boolean desStatus) {
        this.desstatus = desStatus;
    }

    public OutHandler(InputStream is, String type, OutHandlerMethod ohm, String command) {
        br = new BufferedReader(new InputStreamReader(is));
        this.type = type;
        this.ohm = ohm;
        OutHandler.command = command;
        FFmpegManager.ffmpegStatus.put(type, "0");
    }

    /**
     * 重写线程销毁方法，安全的关闭线程
     */
    @Override
    public void destroy() {
        setDesStatus(false);
    }

    /**
     * 执行输出线程
     */
    @Override
    public void run() {
        String msg = null;
        OutHandler outHandler = null;
        TaskEntity tasker = null;
        Process process = null;
        try {
            if (FFmpegManager.config.isDebug()) {
                logger.debug("========【开始推流】========");
                while (desstatus) {
                    if ((msg = br.readLine()) != null) {
                        if (msg.contains("Server error: Already publishing")) {
                            //重复推流
                            logger.warn("=======【重复推流】=========");
                            desstatus = false;
                            if (this.isAlive()) { //结束进程
                                destroy();
                            }
                        }
                        FFmpegManager.ffmpegStatus.put(type, "0");
                        logger.info("【当前任务状态----" +  FFmpegManager.ffmpegStatus.get(type) + "】");
                        logger.info("【任务流信息------" + msg + " 】");
                        ohm.parse(type, msg);
                    } else {//重新启动线程
                        //重新启动线程需要判断当前命令的状态
                        if (FFmpegManager.ffmpegStatus.get(type) != null) {
                            if (FFmpegManager.ffmpegStatus.get(type).equals("1")) {//接口停止 不重新启动
                                desstatus = false;
                            }
                            else {
                                logger.info("【执行命令----" +  OutHandler.command + "】");
                                if (runtime == null) {
                                    runtime = Runtime.getRuntime();
                                }
                                process = runtime.exec(command);// 执行本地命令获取任务主进程
                                outHandler = new OutHandler(process.getErrorStream(), type, this.ohm, command);
                                outHandler.start();
                                tasker = new TaskEntity(type, process, outHandler);
                                logger.info("【重新启动一个新的线程去执行命令】");

                                desstatus = false;
                            }
                        } else {
                            logger.error("--------【未初始化】---------");
                        }
                    }
                }
            } else {
                Thread.yield();
            }
        } catch (IOException e) {
            logger.error("【发生内部异常错误，自动关闭[" + this.getId() + "]线程】");
            destroy();
        } finally {
            if (this.isAlive()) {
                destroy();
            }
        }
    }

    /**
     * @Description: TODO 捕获推流错误信息
     * @Param: [inputStream]
     * @return: java.lang.String
     * @Author: fuzq
     * @Date: 2018/9/4 9:40
     **/
    public String readInputStream(InputStream inputStream) throws IOException {

        BufferedReader br = null;
        String msg = null;
        br = new BufferedReader(new InputStreamReader(inputStream));
        while ((msg = br.readLine()) != null) {
            //捕获推流错误信息  重复推流应该禁止执行命令
            System.out.println(msg);
            if (msg.contains("failed to connect socket")) {//重复推流命令
                return "重复推流";
            }
        }
        return "推流无异常";
    }
}