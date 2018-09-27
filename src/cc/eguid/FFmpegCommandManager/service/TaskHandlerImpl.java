package cc.eguid.FFmpegCommandManager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cc.eguid.FFmpegCommandManager.FFmpegManager;
import cc.eguid.FFmpegCommandManager.entity.TaskEntity;
import org.apache.log4j.Logger;

/**
 * 任务处理实现
 *
 * @author eguid
 * @version 2016年10月29日
 * @since jdk1.7
 */
public class TaskHandlerImpl implements TaskHandler {
    public static final Logger logger = Logger.getLogger(OutHandler.class);

    private Runtime runtime = null;

    private OutHandlerMethod ohm = null;

    public TaskHandlerImpl(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    @Override
    public TaskEntity process(String id, String command) {
        Process process = null;
        OutHandler outHandler = null;
        TaskEntity tasker = null;
        try {
            if (runtime == null) {
                runtime = Runtime.getRuntime();
            }
            if (FFmpegManager.config.isDebug())
            // 执行本地命令获取任务主进程
            {
                process = runtime.exec(command);
            }

            outHandler = new OutHandler(process.getErrorStream(), id, this.ohm, command);
            outHandler.start();
            tasker = new TaskEntity(id, process, outHandler);
        } catch (IOException e) {
            if (FFmpegManager.config.isDebug()) {
                logger.error("【执行命令失败！正在停止进程和输出线程...】");
            }
            stop(outHandler);
            stop(process);
            // 出现异常说明开启失败，返回null
            return null;
        }
        return tasker;
    }

    @Override
    public boolean stop(Process process) {
        if (process != null) {
            process.destroy();
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean stop(Thread outHandler) {
        if (outHandler != null && outHandler.isAlive()) {
            outHandler.stop();
            outHandler.destroy();
            return true;
        }
        return false;
    }

    @Override
    public boolean stop(Process process, Thread thread) {
        boolean ret;
        ret = stop(thread);
        ret = stop(process);
        return ret;
    }
}
