package org.xiesen;

import cn.hutool.core.date.DateUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiesen
 */
public class TestTkernel {
    public static void main(String[] args) {
        String input1 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: IsActive 1";
        String input2 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: tseriesLen 56822487";
        String input3 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: MDBBlock 47099";
        String input4 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: MemoryDatabase 2377";
        String input5 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: MemoryDatabaseUsage 7.74%";
        String input6 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: MDBBlock 28950";
        String input7 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: MDBBlockUsage 22.09%";
        String input8 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: OrderSize 19925";
        String input9 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: TradeSize 19360";
        String input10 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: BrokerSize 1";
        String input11 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: InvestorSize 119";
        String input12 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: TradingCodeSize 675";
        String input13 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: InvestorPositionSize 2375";
        String input14 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: UserSessionSize 92";
        String input15 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: HandleInput 5673";
        String input16 = "Oct 11 16:30:40 HTZJ-ctp01-tkernel01 tkernel 1[4900]: HandleInputTotal 20322840";

        String pattern = "(\\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2})\\s(\\w+-\\w+-\\w+)\\s(\\w+)\\s(\\d)\\[(\\d+)\\]:\\s(IsActive|tseriesLen|tresultLen|MemoryDatabase|MemoryDatabaseUsage|MDBBlock|MDBBlockUsage|OrderSize|TradeSize|BrokerSize|InvestorSize|TradingCodeSize|InvestorPositionSize|UserSessionSize|HandleInput|HandleInputTotal) (\\d+\\.?\\d*)";

        Pattern logPattern = Pattern.compile(pattern);
        System.out.println("================ input1 ================");
        dataMatcher(input1, logPattern);
        System.out.println("================ input2 ================");
        dataMatcher(input2, logPattern);
        System.out.println("================ input3 ================");
        dataMatcher(input3, logPattern);
        System.out.println("================ input4 ================");
        dataMatcher(input4, logPattern);
        System.out.println("================ input5 ================");
        dataMatcher(input5, logPattern);
        System.out.println("================ input6 ================");
        dataMatcher(input6, logPattern);
        System.out.println("================ input7 ================");
        dataMatcher(input7, logPattern);

        System.out.println("================ input8 ================");
        dataMatcher(input8, logPattern);
        System.out.println("================ input9 ================");
        dataMatcher(input9, logPattern);
        System.out.println("================ input10 ================");
        dataMatcher(input10, logPattern);
        System.out.println("================ input11 ================");
        dataMatcher(input11, logPattern);
        System.out.println("================ input12 ================");
        dataMatcher(input12, logPattern);
        System.out.println("================ input13 ================");
        dataMatcher(input13, logPattern);
        System.out.println("================ input14 ================");
        dataMatcher(input14, logPattern);
        System.out.println("================ input15 ================");
        dataMatcher(input15, logPattern);
        System.out.println("================ input16 ================");
        dataMatcher(input16, logPattern);
    }

    private static long tsParse(String message) {
        java.util.Date date = DateUtil.parse(message, "MMM dd HH:mm:ss");

        return date.getTime();
    }

    private static void dataMatcher(String input, Pattern logPattern) {
        Matcher matcher = logPattern.matcher(input);

        if (matcher.find()) {
            String timestamp = matcher.group(1);
            String server = matcher.group(2);
            String process = matcher.group(3);
            String processId = matcher.group(4);
            String logId = matcher.group(5);
            String keyword = matcher.group(6);
            String value = matcher.group(7);

            System.out.println("时间: " + timestamp);
            System.out.println("时间戳: " + tsParse(timestamp));
            System.out.println("机器名(主机名称): " + server);
            System.out.println("组件名称: " + process);
            System.out.println("组件编号: " + processId);
            System.out.println("组件pid: " + logId);
            System.out.println("指标名称: " + keyword);
            System.out.println("指标值: " + value);
        } else {
            System.out.println("未匹配上: " + input);
        }
    }

}
