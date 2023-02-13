package com.github.xiesen.consanguinity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiesen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaModel {
    private String broker;
    private String topic;
}
