package com.github.xiesen.mock.test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiesen
 */
public class TestFilter {
    private static Map<String, List<String>> filterExpressionCache = new ConcurrentHashMap<>();
    private static final String REGEX_URL = ";";
    private static final String REGEX_URL1 = "=>";
    private static final String REGEX_URL2 = ",";
    public static Map<String, Integer> serverWeightMap = new HashMap<>();

    public static void main(String[] args) {
//        String filterExpression = "cpu_system_mb=>user_pct,system_pct;memory_system_mb=>used_pct,used_bytes";
//        String filterExpression = "cpu_system_mb=>user_pct,system_pct";
//        parseFilterExpression(filterExpression);
//        filterExpressionCache.forEach((k, v) -> System.out.println("k = " + k + "; v = " + v.toString()));
        String queue = "default=>1;zzg=>2;";
//        String queue = "default";
        for (int i = 0; i < 9; i++) {
            System.out.println(queueSelector(queue));
        }
    }


    private static String queueSelector(String queue) {
        boolean flag = queue.contains(REGEX_URL) || queue.contains(REGEX_URL1);
        if (!flag) {
            return queue;
        } else {
            final Map<String, Integer> serverMap = parseQueue(queue);
            // 取得Ip地址List
            Set<String> keySet = serverMap.keySet();
            Iterator<String> iterator = keySet.iterator();

            List<String> serverList = new ArrayList<String>();
            while (iterator.hasNext()) {
                String server = iterator.next();
                int weight = serverMap.get(server);
                for (int i = 0; i < weight; i++) {
                    serverList.add(server);
                }
            }
            java.util.Random random = new java.util.Random();
            int randomPos = random.nextInt(serverList.size());
            return serverList.get(randomPos);
        }
    }

    private static Map<String, Integer> parseQueue(String queue) {
        Map<String, Integer> serverMap = new HashMap<String, Integer>(10);
        String[] fes = queue.split(REGEX_URL);
        for (String fe : fes) {
            String[] feTemp = fe.split(REGEX_URL1);
            serverMap.put(feTemp[0], Integer.valueOf(feTemp[1]));
        }
        return serverMap;
    }

    private static void parseFilterExpression(String filterExpression) {
        filterExpressionCache.clear();
        if (filterExpression.indexOf(REGEX_URL) >= 0) {
            String[] fes = filterExpression.split(REGEX_URL);
            for (String fe : fes) {
                String[] feTemp = fe.split(REGEX_URL1);
                List<String> list = new ArrayList<>(Arrays.asList(feTemp[1].split(REGEX_URL2)));
                filterExpressionCache.put(feTemp[0], list);
            }
        }
    }
}
