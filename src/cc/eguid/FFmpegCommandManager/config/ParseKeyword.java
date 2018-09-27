package cc.eguid.FFmpegCommandManager.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: FFmpegCommandHandler
 * @description:
 * @author: fuzq
 * @create: 2018-09-18 17:03
 **/
public class ParseKeyword {
    public List<String> getKeywords(String p){
        String reg = "(?<=(?<!\\\\)\\$\\{)(.*?)(?=(?<!\\\\)\\})";
        RegExp re = new RegExp();
        List<String> list = re.find(reg, p);
        return list;
    }

    public static String renderString(String content, Map<String, String> map){
        Set<Map.Entry<String, String>> sets = map.entrySet();
        for(Map.Entry<String, String> entry : sets) {
            String regex = "\\$\\{" + entry.getKey() + "\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll(entry.getValue());
        }
        return content;
    }

    public static void main(String[] args) {
        String content = "ffmpeg -i rtsp://${user}:${passwd}@${ip}/Streaming/Channels/102 -codec:v mpeg1video -f mpegts  -codec:a mp2 -b 0 -o \"tee:http://localhost:8081/helm-bwz/${ipcId}|http://localhost:8081/helm-bwz/${ipcId}\"";
        Map<String, String> map = new HashMap<>();
        map.put("user", "admin");
        map.put("passwd", "shfb_718");
        map.put("ip", "192.168.1.199");
        map.put("ipcId", "1");
        content = renderString(content, map);
        System.out.println(content);
    }
}
