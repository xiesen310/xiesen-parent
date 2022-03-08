package com.github.xiesen.mock.test;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author xiesen
 * @title: AlarmMaintenancePeriod
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/2/25 9:55
 */
@Data
public class  AlarmMaintenancePeriod implements Serializable {

    private Integer id;

    /**
     * 任务名称,同一系统下不可重复
     */
    private String taskName;

    /**
     * 系统Id
     */
    private Integer bizId;

    /**
     * 系统简称
     */
    private String bizCode;

    /**
     * 系统名称
     */
    private String bizName;

    /**
     * 开始时间有两种格式
     * 格式1：YYYY-MM-DD hh:mm:ss;
     * 格式2：hh:mm;
     * 根据维护期类型的不同，存储格式不同
     */
    private String startDateTime;

    /**
     * 结束时间有两种格式
     * 格式1：YYYY-MM-DD hh:mm:ss;
     * 格式2：hh:mm;
     * 根据维护期类型的不同，存储格式不同
     */
    private String endDateTime;

    /**
     * 规则状态分为四种，0 未发布，1 已发布，2 维护
     * 中；3 维护中止；维护中状态必须在已发布状态的
     * 基础上进行动态更新，此更新由后台自行操作, 维护
     * 中止护必须在维护中状态的基础上进行变更
     */
    private Integer status;

    /**
     * 维护期类型
     * 0 开始时间与结束时间是格式1，
     * 1 开始时间与结束时间是格式2的，
     * 2 使用星期
     */
    private Integer frequencyType;

    /**
     * 使用星期
     * 0 星期天; 1 星期一；2 星期二；3 星期三；4 星期
     * 四；5 星期五；6 星期六；
     * 例如："1,2,3,4"
     */
    private String usedWeek;

    /**
     * 主机列表
     * 例如：[{appSys:"dev_test",hosts:[{hostname:"localhost.localdomain",ip:"192.168.50.108",moduleId:56,setId:11},{hostname:"zorkdata-66",ip:"192.168.2.66",moduleId:56,setId:11}]}]
     */
    private String hostList;

    /**
     * 屏蔽的告警规则列表，格式1，2，3，4，5
     */
    private String alarmRule;

    private String comment;

    /**
     * 0 不发送告警； 1 发送告警
     */
    private Integer sendAlarm;

    /**
     * 告警方式
     */
    private String alarmWay;

    /**
     * 告警接收人
     */
    private String revUsers;

    private String createUser;

    private String createTime;

    private String updateUser;

    private String updateTime;


    private List<String> alarmRuleList;

    private Map<String, List<String>> hostnameMap;

    private Map<String, List<String>> ipMap;
}