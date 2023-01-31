package com.github.xiesen.redis.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author xiesen
 */
@Data
@Builder
public class RedisInfo {
    private String host;
    private int port = 6379;
    private String pwd;
    private int database;

    /**
     * redis 连接超时时间,单位 ms,默认 10s*
     */
    private int timeout = 10000;

    public RedisInfo(String host, int port, String pwd, int database, int timeout) {
        this.host = host;
        this.port = port;
        this.pwd = pwd;
        this.database = database;
        this.timeout = timeout;
    }
}
