package cc.eguid.FFmpegCommandManager.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cc.eguid.FFmpegCommandManager.FFmpegManager;
import cc.eguid.FFmpegCommandManager.FFmpegManagerImpl;
import cc.eguid.FFmpegCommandManager.entity.TaskEntity;

/**
 * 测试
 *
 * @author eguid
 * @version 2017年10月13日
 * @since jdk1.7
 */
public class Test {
    /**
     * 命令组装器测试
     *
     * @throws InterruptedException
     */
    public static void test1() throws InterruptedException {
        FFmpegManager manager = new FFmpegManagerImpl();
        Map<String, String> map = new HashMap<String, String>();
        map.put("appName", "test123");
        map.put("input", "rtsp://admin:shfb_718@192.168.1.198");
        map.put("output", "rtmp://127.0.0.1:1935/live/ipcId");
        map.put("output1", "rtmp://127.0.0.1:1935/hls/ipcId");
        map.put("codec", "copy");
        map.put("fmt", "flv");
        map.put("fps", "25");
        map.put("rs", "640x360");
        map.put("twoPart", "2");
        // 执行任务，id就是appName，如果执行失败返回为null
        String id = manager.start(map);
        System.out.println(id);
        // 通过id查询
        TaskEntity info = manager.query(id);
        System.out.println(info);
        // 查询全部
        Collection<TaskEntity> infoList = manager.queryAll();
        System.out.println(infoList);
        Thread.sleep(30000);
        // 停止id对应的任务
        manager.stop(id);
    }

    /**
     * 默认方式，rtsp->rtmp转流单个命令测试
     *
     * @throws InterruptedException
     */
    public static void test2() throws InterruptedException {
        FFmpegManager manager = new FFmpegManagerImpl();
        // -rtsp_transport tcp
        //测试多个任何同时执行和停止情况
        //默认方式发布任务
        manager.start("tomcat", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat");

        Thread.sleep(30000);
        // 停止全部任务
        manager.stopAll();
    }

    /**
     * 完整ffmpeg路径测试
     *
     * @throws InterruptedException
     */
    public static void test4() throws InterruptedException {
        FFmpegManager manager = new FFmpegManagerImpl();
        // -rtsp_transport tcp
        //测试多个任何同时执行和停止情况
        //默认方式发布任务
        manager.start("tomcat", "D:/TestWorkspaces/FFmpegCommandHandler/src/cc/eguid/FFmpegCommandManager/ffmpeg/ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat", true);

        Thread.sleep(30000);
        // 停止全部任务
        manager.stopAll();
    }

    /**
     * rtsp-rtmp转流多任务测试
     *
     * @throws InterruptedException
     */
    public static void test3() throws InterruptedException {
        FFmpegManager manager = new FFmpegManagerImpl();
        // -rtsp_transport tcp
        //测试多个任何同时执行和停止情况
        //false表示使用配置文件中的ffmpeg路径，true表示本条命令已经包含ffmpeg所在的完整路径
        manager.start("tomcat", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat", false);
        manager.start("tomcat1", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat1", false);
        manager.start("tomcat2", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat2", false);
        manager.start("tomcat3", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat3", false);
        manager.start("tomcat4", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat4", false);
        manager.start("tomcat5", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat5", false);
        manager.start("tomcat6", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat6", false);
        manager.start("tomcat7", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat7", false);
        manager.start("tomcat8", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat8", false);
        manager.start("tomcat9", "ffmpeg -i rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov -vcodec copy -acodec copy -f flv -y rtmp://106.14.182.20:1935/rtmp/tomcat9", false);

        Thread.sleep(300000);
        // 停止全部任务
        manager.stopAll();
    }

    public static void main(String[] args) throws InterruptedException {
        test1();
//		test2();
//		test3();
//		test4();
    }
}
