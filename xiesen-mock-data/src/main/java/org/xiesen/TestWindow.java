package org.xiesen;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Validator;
import org.xiesen.windows.Time;
import org.xiesen.windows.TimeWindow;

import java.util.Date;

public class TestWindow {
    public static final long offset = 0L;
    public static final long size = 60000L;
    public static final String format = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {
        /// 2024-03-29 18:13:20
        long ts = 1711707200000L;
        boolean expired = isExpired(ts, Time.seconds(60));
        Console.log("是否在区间内: {}", expired);
    }

    /**
     * 判断时间是否在当前时间窗口内
     *
     * @param ts   时间
     * @param time time
     * @return boolean
     */
    public static boolean isExpired(long ts, Time time) {
        TimeWindow timeWindow = getProcessingTimeWindows(time);
        return Validator.isBetween(ts, timeWindow.getStart(), timeWindow.getEnd());
    }

    /**
     * 获取当前时间窗口
     *
     * @param time time
     * @return TimeWindow
     */
    public static TimeWindow getProcessingTimeWindows(Time time) {
        long size = time.toMilliseconds();
        long offset = 0;
        if (Math.abs(offset) >= size) {
            throw new IllegalArgumentException("parameters must satisfy abs(offset) < size");
        }

        final long now = System.currentTimeMillis();
        long start = getWindowStartWithOffset(now, offset, size);
        long end = start + size;
        Console.log("[{},{})", DateUtil.format(new Date(start), format), DateUtil.format(new Date(end), format));
        return new TimeWindow(start, end);
    }

    /**
     * 获取窗口的开始时间
     *
     * @param timestamp  时间戳
     * @param offset     offset
     * @param windowSize 窗口大小,毫秒值
     * @return long
     */
    public static long getWindowStartWithOffset(long timestamp, long offset, long windowSize) {
        return timestamp - (timestamp - offset + windowSize) % windowSize;
    }
}
