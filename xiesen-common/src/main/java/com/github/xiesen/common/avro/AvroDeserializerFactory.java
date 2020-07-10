package com.github.xiesen.common.avro;

/**
 * @author xiese
 * @Description 反序列化工厂类
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:31
 */
public class AvroDeserializerFactory {
    private static AvroDeserializer logs = null;

    private static AvroDeserializer metrics = null;

    public static void init() {
        logs = null;
        metrics = null;
    }

    /**
     * getLogsDeserializer
     *
     * @return
     */
    public static AvroDeserializer getLogsDeserializer() {
        if (logs == null) {
            logs = new AvroDeserializer(LogAvroMacroDef.metadata);
        }
        return logs;
    }

    /**
     * getLogsDeserializer
     *
     * @return
     */
    public static AvroDeserializer getMetricDeserializer() {
        if (metrics == null) {
            metrics = new AvroDeserializer(MetricAvroMacroDef.metadata);
        }
        return metrics;
    }
}
