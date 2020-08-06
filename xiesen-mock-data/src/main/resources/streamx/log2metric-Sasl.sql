create table streamx_log_test(
    WATERMARK FOR timestamp AS withOffset('timestamp', 6000),
    timestamp bigint,
    source varchar,
    offset varchar,
    appprogramname varchar, appsystem varchar,
    hostname varchar, ip varchar, age varchar,
    country_code varchar,
    id varchar,
    message varchar,
    name varchar
) with (
    type = 'kafka11',
    zookeeperQuorum = 'zorkdata-92:2181',
    kafka.key.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer',
    sourceDataType = 'logavro',
    kafka.value.deserializer = 'org.apache.kafka.common.serialization.ByteArrayDeserializer',
    bootstrapServers = 'zorkdata-92:9092',
    groupId = 'default_group',
    parallelism = '1',
    topic = 'log2metric',
    schemaString = 'appprogramname,appsystem,hostname,ip||age,country_code,id,message,name',
    offsetReset = 'earliest'
);

create table streamx_metric_test (
    timestamp TIMESTAMP,
    appprogramname varchar,
    appsystem varchar,
    hostname varchar,
    ip varchar,
    countNum double
) with (
    type = 'kafka11',
    zookeeperQuorum = 'zorkdata-92:2181',
    kafka.key.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer',
    kafka.value.deserializer = 'org.apache.kafka.common.serialization.StringDeserializer',
    bootstrapServers = 'zorkdata-92:9092',
    sinkDataType = 'metricavro',
    parallelism = '1',
    topic = 'result',
    schemaString = 'appprogramname,hostname,ip,appsystem|countNum|'
);

insert into streamx_metric_test
    select
        appsystem,
        hostname, appprogramname,
        ip, cast(COUNT(1) as double) * 2000 AS countNum,
        TUMBLE_START(`timestamp`, INTERVAL '2' MINUTE) AS `timestamp`
    from streamx_log_test
group by
    TUMBLE(`timestamp`, INTERVAL '2' MINUTE),
    appsystem,
    hostname,
    appprogramname,
    ip;