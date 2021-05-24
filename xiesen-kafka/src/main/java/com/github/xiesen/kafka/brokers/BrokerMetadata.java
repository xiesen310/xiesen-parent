package com.github.xiesen.kafka.brokers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 谢森
 * @since 2021/4/28
 */
@Data
@Slf4j
public class BrokerMetadata implements Cloneable {

    private long clusterId;

    private int brokerId;

    private List<String> endpoints;

    private String host;

    private int port;

    /*
     * ZK上对应的字段就是这个名字, 不要进行修改
     */
    private int jmx_port;

    private String version;

    private long timestamp;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException var3) {
            log.error("clone BrokerMetadata failed.", var3);
        }
        return null;
    }
}
