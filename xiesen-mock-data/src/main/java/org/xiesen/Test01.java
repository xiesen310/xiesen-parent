package org.xiesen;

import com.github.xiesen.TestLog;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author 谢森
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2021/1/22 20:08
 */
public class Test01 {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(Test01.class);
    private static BlockingQueue<Map<String, Object>> alarmRecordQueue = new LinkedBlockingQueue<>();

    public static void run() {
        log.error("Test01 error");
        log.info("Test01 info");
        log.debug("Test01 debug");
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            mockAlarmRecordQueue(alarmRecordQueue);
        }
        System.out.println("alarmRecordQueue start size is " + alarmRecordQueue.size());
        for (Map<String, Object> map : alarmRecordQueue) {
            Map<String, Object> stringObjectMap = alarmRecordQueue.poll(3, TimeUnit.SECONDS);
            stringObjectMap.forEach((k, v) -> System.out.println(k + "_" + v));
        }
        System.out.println("alarmRecordQueue end size is " + alarmRecordQueue.size());

    }

    public static void mockAlarmRecordQueue(BlockingQueue<Map<String, Object>> alarmRecordQueue) throws InterruptedException {
        alarmRecordQueue.put(putMap());
    }

    public static Map<String, Object> putMap() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", "zs");
        return map;
    }


}
