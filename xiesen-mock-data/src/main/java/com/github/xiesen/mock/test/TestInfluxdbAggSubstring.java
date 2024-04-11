package com.github.xiesen.mock.test;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class TestInfluxdbAggSubstring {
    public static long getMsTime(String str) {
        if (str == null) {
            return -1L;
        } else {
            str = str.trim();

            try {
                if (str.length() == 13 && str.startsWith("1")) {
                    return Long.parseLong(str);
                } else if (str.length() == 10 && str.startsWith("1")) {
                    return Long.parseLong(str) * 1000L;
                } else {
                    return str.contains(" ") ? Timestamp.valueOf(str).getTime() : (new DateTime(str)).getMillis();
                }
            } catch (Exception var2) {
                return -1L;
            }
        }
    }

    public static void main(String[] args) {
        String inputString = "This is the string to be truncated}. More text follows.";
        int endIndex = inputString.indexOf('}');

        if (endIndex != -1) {
            String outputString = inputString.substring(0, endIndex + 1); // Extract the substring before '}'
            System.out.println(outputString); // Print the result
        } else {
            System.out.println("Character '}' not found in the string.");
        }

        long msTime = getMsTime("1712815740098");
        System.out.println(msTime);
    }
}
