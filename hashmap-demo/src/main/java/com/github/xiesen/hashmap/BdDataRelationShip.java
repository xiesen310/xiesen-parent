package com.github.xiesen.hashmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 谢森
 * @since 2021/6/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BdDataRelationShip {
    private int id;
    private String rule;
    private Integer dataType;
    private String bizCode;
    private String bizName;
    private String moduleCode;
    private String moduleName;
    private String dataName;
    private String originalTopic;
    private String analysisTopic;
    private String esIndex;
    private String originalGroupId;
    private String analysisGroupId;
    private String status;
    private Integer level;
    private String timeType;
    private Integer keepOfTime;
}
