package com.github.xiesen.common.avro2.avro;

/**
 * @author xiese
 * @Description 序列化工厂类
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:32
 */
@SuppressWarnings("all")
public class AvroSerializerFactory {
    private static AvroSerializer metricMetadata = null;
    private static AvroSerializer logMetadata = null;

    public static AvroSerializer getLogAvroSerializer() {
        if (logMetadata == null) {
            logMetadata = new AvroSerializer(LogAvroMacroDef.metadata);
        }
        return logMetadata;
    }


    public static AvroSerializer getMetricAvroSerializer() {
        if (metricMetadata == null) {
            metricMetadata = new AvroSerializer(MetricAvroMacroDef.metadata);
        }
        return metricMetadata;
    }
}
