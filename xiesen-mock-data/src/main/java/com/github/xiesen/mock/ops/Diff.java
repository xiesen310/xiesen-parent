package com.github.xiesen.mock.ops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiesen
 */
public class Diff {
    public static void main(String[] args) {
        String flumeFileName = "D:\\tmp\\kcbp\\kcbp_logs\\bak\\flume.txt";
        String flinkFileName = "D:\\tmp\\kcbp\\kcbp_logs\\bak\\flink.txt";
        final List<String> flumeList = readResult(flumeFileName);
        final List<String> flinkList = readResult(flinkFileName);

        final List<String> list = subList(flumeList, flinkList);
        System.out.println("不同的 messId 一共 " + list.size() + " 个.");
        list.forEach(System.out::println);
    }


    private static List<String> subList(List<String> flumeList, List<String> flinkList) {
        Map<String, String> tempMap = flinkList.parallelStream().collect(Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData));
        return flumeList.parallelStream().filter(str -> {
            return !tempMap.containsKey(str);
        }).collect(Collectors.toList());
    }

    private static List<String> readResult(String fileName) {
        BufferedReader reader;
        List<String> list = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                list.add(line.trim());
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
