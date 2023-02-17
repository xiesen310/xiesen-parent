package com.github.xiesen.consanguinity.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xiesen
 */
@Data
@Builder
public class FilebeatInputType {
    private String type;
    private List<String> paths;
    private Map<String, Object> fields;
    private String encoding;
    private String ip;
    private String topicName;
    private String broker;
}
