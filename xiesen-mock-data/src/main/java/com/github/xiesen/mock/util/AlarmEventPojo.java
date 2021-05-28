package com.github.xiesen.mock.util;

import lombok.Data;

/**
 * @author 谢森
 * @since 2021/5/25
 */
@Data
public class AlarmEventPojo {
    private String alarmContent;
    private String lastTime;
    /**
     * 事件 id
     */
    private String eventId;
    private String recvUser;
    private String alarmTime;
    private String ip;
    private String operator;
    private String alarmCount;
    private String hostname;
    private String alarmObject;
    private String eventStatus;
    private String alarmId;
    private String appSystem;
    private String eventTime;
    private String alarmLevel;
    private String alarmTitle;
    private String beginTime;
    private String alarmObjectValue;
    private String ruleId;
    private Integer dataType;

}
