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
public class LogstashModel {
    private List<String> hosts;
    private boolean enabled;
    private boolean loadBalance;
    private KafkaModel kafka;
}
