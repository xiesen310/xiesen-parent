package com.github.xiesen.consanguinity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiesen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileBeatModel {
    /**
     * 节点名称*
     */
    private String name;
    /**
     * beat 类型*
     */
    private String type = "filebeat";

    private List<FilebeatInputType> filebeatTypeInstances;

    private LogstashModel logstash;

}
