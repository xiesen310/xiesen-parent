create table streamx_metric_cpu (
    WATERMARK FOR timestamp AS withOffset('timestamp', 6000),
    timestamp bigint,
    appprogramname varchar,
    appsystem varchar,
    hostname varchar,
    ip varchar,
    cpu_usage_rate double
) with (
    type = 'kafka11',
    zookeeperQuorum = 'zorkdata-92:2181',
    kafka.key.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer',
    sourceDataType = 'metricavro',
    kafka.value.deserializer = 'org.apache.kafka.common.serialization.ByteArrayDeserializer',
    bootstrapServers = 'zorkdata-92:9092',
    groupId = 'streamx_sql_01', parallelism = '1',
    topic = 'metric2metric', schemaString = 'appsystem,appprogramname,hostname,ip|cpu_usage_rate|',
    offsetReset = 'earliest'
);

create table streamx_metric_cpu_1m (
    timestamp TIMESTAMP,
    appprogramname varchar,
    appsystem varchar,
    hostname varchar,
    ip varchar,
    cpu_usage_1m double
) with (
    type = 'kafka11',
    zookeeperQuorum = 'zorkdata-92:2181',
    kafka.key.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer',
    kafka.value.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer',
    bootstrapServers = 'zorkdata-92:9092', sinkDataType = 'metricavro',
    parallelism = '1', topic = 'result',
    schemaString = 'appsystem,appprogramname,hostname,ip|cpu_usage_1m|'
);

INSERT INTO streamx_metric_cpu_1m
    SELECT
        appprogramname,
        appsystem,
        hostname,
        ip,
        avg(cpu_usage_rate) AS cpu_usage_1m,
        TUMBLE_START(`timestamp`, INTERVAL '60' SECOND) AS `timestamp`
    FROM streamx_metric_cpu
    GROUP BY
        appprogramname,
        appsystem,
        hostname,
        ip,
        TUMBLE(`timestamp`, INTERVAL '60' SECOND);