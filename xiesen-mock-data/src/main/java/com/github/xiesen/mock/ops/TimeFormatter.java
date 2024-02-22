package com.github.xiesen.mock.ops;

public class TimeFormatter {
    public static String formatMilliseconds(long milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        long days = milliseconds / (1000 * 60 * 60 * 24);
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (seconds > 0) {
            sb.append(seconds).append("s ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
//        long milliseconds = 123456789;
        long milliseconds = 10010;
        String formattedTime = formatMilliseconds(milliseconds);
        System.out.println("Formatted time: " + formattedTime);
    }
}
