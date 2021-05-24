package com.github.xiesen.kafka.brokers;

import lombok.Data;

import java.util.Set;

/**
 * 存储Topic的元信息, 元信息对应的ZK节点是/brokers/topics/${topicName}
 *
 * @author 谢森
 * @since 2021/4/28
 */
@Data
public class TopicMetadata implements Cloneable {
    /**
     * topic名称
     */
    private String topic;

    /**
     * partition所在的Broker
     */
    private PartitionMap partitionMap;

    /**
     * topic所在的broker, 由partitionMap获取得到
     */
    private Set<Integer> brokerIdSet;

    /**
     * 副本数
     */
    private int replicaNum;

    /**
     * 分区数
     */
    private int partitionNum;

    /**
     * 修改节点的时间
     */
    private long modifyTime;

    /**
     * 创建节点的时间
     */
    private long createTime;
}
