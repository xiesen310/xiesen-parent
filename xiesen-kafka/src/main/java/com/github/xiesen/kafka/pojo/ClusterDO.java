package com.github.xiesen.kafka.pojo;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @author 谢森
 * @since 2021/4/28
 */
@Data
public class ClusterDO implements Comparable<ClusterDO> {
    private Long id;

    private String clusterName;

    private String zookeeper;

    private String bootstrapServers;

    private String securityProperties;

    private String jmxProperties;

    private Integer status;

    private Date gmtCreate;

    private Date gmtModify;

    @Override
    public int compareTo(ClusterDO clusterDO) {
        return this.id.compareTo(clusterDO.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClusterDO clusterDO = (ClusterDO) o;
        return Objects.equals(id, clusterDO.id)
                && Objects.equals(clusterName, clusterDO.clusterName)
                && Objects.equals(zookeeper, clusterDO.zookeeper)
                && Objects.equals(bootstrapServers, clusterDO.bootstrapServers)
                && Objects.equals(securityProperties, clusterDO.securityProperties)
                && Objects.equals(jmxProperties, clusterDO.jmxProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clusterName, zookeeper, bootstrapServers, securityProperties, jmxProperties);
    }

}
