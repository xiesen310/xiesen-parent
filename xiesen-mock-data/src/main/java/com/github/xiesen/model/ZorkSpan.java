package com.github.xiesen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author：hubinbin
 * @date：2022/6/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ZorkSpan extends ZorkLogStruct implements Serializable {
    public static final String PARENT_ID = "0000000000000000";
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS+08:00";
    @NonNull
    private ZorkSpanDimensions dimensions;
    @NonNull
    private ZorkSpanNormalFields normalFields;
    @NonNull
    private ZorkSpanMeasures measures;


    @Data
    public static class ZorkSpanDimensions implements Serializable{
        /**
         * 端点
         * 1. 如果当前span操作是http请求 那么OperationName就是请求的url
         * 2. 如果当前span操作是sql语句 那么OperationName就是sql类型
         * 3. 如果当前span操作是redis操作 那么OperationName就是redis命令
         * 4. 也可以是实际调用的方法
         */
        private String endpoint;

        // span 运行组件名称
        private String component;

        // 主机名
        private String hostname;


        // ip
        @NonNull
        private String ip;

        // 服务实例
        @NonNull
        private String serviceinstance;

        // 调用类型: remote,远程调用; local: 本服务内调用
        @NonNull
        private String calltype;


        /**
         * 操作
         * 1. 当前 span 操作是 http 请求, operationname 就是请求的 url
         * 2. 当前 span 操作是 sql 语句, operationname 就是 sql 类型
         * 3. 当前 span 操作是 redis 操作, operationname 就是 redis 命令,也可以是实际调用的方法
         */
        @NonNull
        private String operationname;

        // 系统名称: 当前系统,用于查询定位
        @NonNull
        private String appsystem;

        // 服务名称: 当前服务名称,用于查询
        @NonNull
        private String servicename;
    }

    @Data
    public static class ZorkSpanNormalFields implements Serializable{
        // 功能链 id
        @NonNull
        private String traceid;

        // 父节点 id
        @NonNull
        private String parentid;

        // 本节点 id
        @NonNull
        private String spanid;


        // 开始时间
        @NonNull
        private String starttime;

        // 结束时间
        @NonNull
        private String endtime;

        // 节点名称
        private String spanname;

        // 调用返回状态码
        private String code;

        // 跨度类型: 0: 根节点(Entry); 1: 叶子节点(Exit)
        @NonNull
        private int spantype;

        // 跨度类型 HTTP, DATABASE,RPC,USER,NORMAL
        @NonNull
        private String spanlayer;

        @NonNull
        private String remoteendpointname;
        @NonNull
        private String remoteservicename;
        @NonNull
        private String remoteserviceinstance;
        @NonNull
        private String remoteip;
        @NonNull
        private String addinformation;

    }

    @Data
    public static class ZorkSpanMeasures implements Serializable{
        @NonNull
        private int duration;
    }
}

