package org.xiesen;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class TestSet {
    public static void main(String[] args) {
        Set<String> stdSet = new HashSet<>();
        stdSet.add("cpu@192.168.1.1@200");
        stdSet.add("cpu@192.168.1.2@200");
        stdSet.add("cpu@192.168.1.3@200");
        stdSet.add("cpu@192.168.1.4@200");
        stdSet.add("cpu@192.168.1.5@200");

        Set<String> actSet = new HashSet<>();
        actSet.add("cpu@192.168.1.1@200");
        actSet.add("cpu@192.168.1.2@200");
        actSet.add("cpu@192.168.1.3@200");
        Set<String> complementStdAct = new HashSet<>(stdSet);
        complementStdAct.removeAll(actSet); // A - B
        complementStdAct.forEach(System.out::println);

        System.out.println(calculateBatchNumber(1711641600000L, 180L));
    }

    public static int calculateBatchNumber(long windowStart, long intervalSeconds) {
        Instant instant = Instant.ofEpochMilli(windowStart);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        long millisecondsSinceStartOfDay = localDateTime.toLocalTime().toSecondOfDay();
        return (int) (millisecondsSinceStartOfDay / intervalSeconds) + 1;
    }
}
