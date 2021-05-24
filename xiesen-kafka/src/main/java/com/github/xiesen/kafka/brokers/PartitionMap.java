package com.github.xiesen.kafka.brokers;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/4/28
 */
@Data
public class PartitionMap implements Serializable {
    /**
     * 版本号
     */
    private int version;

    /**
     * Map<PartitionId，副本所在的brokerId列表>
     */
    private Map<Integer, List<Integer>> partitions;
}
