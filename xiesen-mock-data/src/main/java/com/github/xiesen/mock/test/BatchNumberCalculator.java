package com.github.xiesen.mock.test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author xiesen
 */
public class BatchNumberCalculator {
    public static void main(String[] args) {
        long intervalMilliseconds = 30; // 1小时

//        int batchNumber = calculateBatchNumber(intervalMilliseconds);
        int batchNumber = calculateBatchNumber(1699599030000L, intervalMilliseconds);
        System.out.println("Current batch number: " + batchNumber);
    }


    public static int calculateBatchNumber(long windowStart, long intervalMilliseconds) {
        Instant instant = Instant.ofEpochMilli(windowStart);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        long millisecondsSinceStartOfDay = localDateTime.toLocalTime().toSecondOfDay();
        return (int) (millisecondsSinceStartOfDay / intervalMilliseconds) + 1;
    }

    public static int calculateBatchNumber(long intervalMilliseconds) {
        LocalTime currentTime = LocalTime.now();
        // 16 9958 7660 000
        System.out.println(currentTime.toNanoOfDay());
        long millisecondsSinceStartOfDay = currentTime.toNanoOfDay() / 1000000;
        int batchNumber = (int) (millisecondsSinceStartOfDay / intervalMilliseconds) + 1;
        return batchNumber;
    }
}
