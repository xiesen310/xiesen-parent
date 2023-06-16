package com.github.xiesen.mock.kcbp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author xiesen
 */
public class MockKcbpLog {
    private static final String FILE_SUFFIX = ".log";
    private static final String FILE_PREFIX = "runlog";
    private static final int FILE_SIZE_LIMIT = 500 * 1024 * 1024;


    public static void main(String[] args) {
        String filePath = "";
        int fileCount = 0;
        if (args.length == 2) {
            String lastChar = args[1].substring(args[1].length() - 1);
            if (!lastChar.equalsIgnoreCase(File.separator)) {
                filePath = args[0] + File.separator;
            } else {
                filePath = args[0];
            }
            fileCount = Integer.parseInt(args[1]);
        }
        Console.log("{} params,filePath = {}; fileCount = {}", DateUtil.formatDateTime(DateUtil.date()), filePath, fileCount);
        for (int i = 0; i < fileCount; i++) {
            final String tmpPath = filePath + getTimeDir();
            autoMakedir(tmpPath);
            String fileName = tmpPath + File.separator + FILE_PREFIX + i + FILE_SUFFIX;
            new FileWriterTask(fileName).run();
        }
        Console.log("{} mock data finish.", DateUtil.formatDateTime(DateUtil.date()));
    }

    private static void autoMakedir(String tmpPath) {
        File file = new File(tmpPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static class FileWriterTask implements Runnable {
        private final String fileName;
        private volatile long fileSize = 0;

        public FileWriterTask(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void run() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName), true))) {
                while (true) {
                    final List<String> list = mockLogTemplate();
                    for (String msg : list) {
                        writer.write(msg);
                        writer.newLine();
                        fileSize += msg.length();
                    }

                    if (fileSize >= FILE_SIZE_LIMIT) {
                        cn.hutool.core.lang.Console.log("{} {} 写入完成", DateUtil.formatDateTime(DateUtil.date()), fileName);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * [20230308-000014][   228379390][1c94-1354][   99] Req: NodeId=19101, QueueId=120, MsgId=1900010100058CFA27E0EF8E, Len=126, Buf=010
     * [20230308-000014][   228379390][1c94-1354][   99] 000000000000000001000000000000000D4F5EF068D68KCXP00  GV2gODkBbGg=
     * [20230308-000014][   228379390][1c94-1354][   99]                    00000000000          207
     * [20230308-000014][   228379390][18a0-1cbc][  100] as 11 receive a request msgid=1900010100058CFA27E0EF8E mtype=0
     * [20230308-000014][   228379390][18a0-1cbc][  100] QueueId=120 MsgId=1900010100058CFA27E0EF8E Name=
     * [20230308-000014][   228379390][18a0-1cbc][   98] Ans: NodeId=19101, QueueId=120, MsgId=1900010100058CFA27E0EF8E, Len=153, Buf=013
     * [20230308-000014][   228379390][18a0-1cbc][   98] 000011000770000003191011910119101D4F5EF068D68KCXP00  GV2gODkBbGg=64075F8E0191010
     * [20230308-000014][   228379390][18a0-1cbc][   98] 00011000188        00027000000          207_CA=2.3&_ERRNO=0&_ERRMSG=OK
     *
     * @return list
     */
    private static List<String> mockLogTemplate() {
        List<String> list = new ArrayList<>();
        final String reqTime = currentTime();

        final String reqNs = getNano();

        final String msgId = randomMsgId();
        final String reqThreadId = randomThreadId();
        final String ansThreadId = randomThreadId();

        /// 请求
        list.add(String.format("[%s][   %s][%s][   99] Req: NodeId=19101, QueueId=120, MsgId=%s, Len=126, Buf=010", reqTime, reqNs, reqThreadId, msgId));
        list.add(String.format("[%s][   %s][%s][   99] 000000000000000001000000000000000D4F5EF068D68KCXP00  GV2gODkBbGg=               ", reqTime, reqNs, reqThreadId));
        list.add(String.format("[%s][   %s][%s][   99] 00000000000207 MsgId=%s", reqTime, reqNs, reqThreadId, msgId));

        final String ansTime = currentTime();
        final String ansNs = getNano();
        list.add(String.format("[%s][   %s][%s][  100] as 11 receive a request msgid=%s mtype=0", ansTime, ansNs, ansThreadId, msgId));
        list.add(String.format("[%s][   %s][%s][  100] QueueId=120 MsgId=%s Name=", ansTime, ansNs, ansThreadId, msgId));

        /// 响应
        list.add(String.format("[%s][   %s][%s][   98] Ans: NodeId=19101, QueueId=120, MsgId=%s, Len=153, Buf=013", ansTime, ansNs, ansThreadId, msgId));
        list.add(String.format("[%s][   %s][%s][   98] 000011000770000003191011910119101D4F5EF068D68KCXP00  GV2gODkBbGg=64075F8E0191010", ansTime, ansNs, ansThreadId));
        list.add(String.format("[%s][   %s][%s][   98] 007_CA=2.3MSG=OK MsgId=%s", ansTime, ansNs, ansThreadId, msgId));
        return list;
    }

    private static String randomThreadId() {
        Random random = new Random();
        int num = random.nextInt(900) + 100;
        int num2 = random.nextInt(9000) + 1000;
        return String.format("t%d-%d", num, num2);
    }

    private static String getTimeDir() {
        return DateUtil.format(DateUtil.date(), "yyyyMMdd");
    }

    private static String randomMsgId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 24);
    }

    private static String currentTime() {
        return DateUtil.format(DateUtil.date(), "yyyyMMdd-HHmmss");
    }

    private static String getNano() {
        Instant instant = Instant.now();
        long ns = instant.getNano();
        return String.valueOf(ns);
    }
}
