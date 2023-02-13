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
public class BeatModel {
    /**
     * beat 类型*
     */
    private String type;

    /**
     * topic 列表*
     */
    private List<String> topicName;

    /**
     * ip 实例
     */
    private List<String> ip;

    private LogstashModel logstash;

}
