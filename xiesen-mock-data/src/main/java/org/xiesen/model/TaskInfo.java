package org.xiesen.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiesen
 * @title: TaskInfo
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/10/17 14:37
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskInfo {
    private String id;
    private String user;
    private String name;
    private String queue;
    private String state;
    private String trackingUrl;
    private String applicationType;
    private String startedTime;
    private String finishedTime;
    private String elapsedTime;
    private String allocatedMB;
    private String allocatedVCores;
    private String runningContainers;
}
