package com.github.xiesen.consanguinity.utils;

import org.neo4j.driver.*;

/**
 * @author xiesen
 */
public class TestNeo4j {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://192.168.90.24:27687", AuthTokens.basic("neo4j", "zorkdata.com"));
        Session session = driver.session();

        /*session.run("CREATE (n:Part {name: $name,title: $title})",
                parameters("name", "Arthur001", "title", "King001"));*/

        // 删除全部数据
        session.run("match (n) detach delete n");

//        session.run("CREATE (student1:Person {sid: '1001',name:'Steven',gender:'M',age:'18'})");
//        session.run("CREATE (student2:Person {sid: '1002',name:'Mary',gender:'M',age:'19'})");
//        session.run("MATCH (student1:Person),(student2:Person) where student1.name = 'Steven' AND student2.name = 'Mary' CREATE (student1)-[:classmate] ->(student2) RETURN student1,student2");

        // filebeat
        session.run("CREATE (node91:filebeat {name: '192.168.1.91', type: 'filebeat',appSystem:'poc 测试',serviceName:'模块',serviceCode:'模块',clusterName:'集群',appProgramName:'模块',ip:'92.168.1.91'})");
        session.run("CREATE (node92:filebeat {name: '192.168.1.92', type: 'filebeat',appSystem:'poc 测试',serviceName:'模块',serviceCode:'模块',clusterName:'集群',appProgramName:'模块',ip:'92.168.1.91'})");
//        session.run("CREATE (node93:filebeat {name: '192.168.1.93', type: 'filebeat',appSystem:'poc 测试',serviceName:'模块',serviceCode:'模块',clusterName:'集群',appProgramName:'模块',ip:'92.168.1.91'})");
//        session.run("CREATE (node94:filebeat {name: '192.168.1.94', type: 'filebeat',appSystem:'poc 测试',serviceName:'模块',serviceCode:'模块',clusterName:'集群',appProgramName:'模块',ip:'92.168.1.91'})");
//        session.run("CREATE (node95:filebeat {name: '192.168.1.95', type: 'filebeat',appSystem:'poc 测试',serviceName:'模块',serviceCode:'模块',clusterName:'集群',appProgramName:'模块',ip:'92.168.1.91'})");

        // metricbeat
        session.run("CREATE (node96:metricbeat {name: '192.168.1.96', type: 'metricbeat',appSystem:'poc 测试',serviceName:'模块',serviceCode:'模块',clusterName:'集群',appProgramName:'模块',ip:'92.168.1.91'})");
        session.run("CREATE (node97:metricbeat {name: '192.168.1.97', type: 'metricbeat',appSystem:'poc 测试',serviceName:'模块',serviceCode:'模块',clusterName:'集群',appProgramName:'模块',ip:'92.168.1.91'})");

        // logstash
        session.run("CREATE (node2_92:logstash {name:'192.168.2.92',type: 'logstash',networkArea: '集中交易'})");
        session.run("CREATE (node2_93:logstash {name:'192.168.2.93',type: 'logstash',networkArea: '集中交易'})");

        // kafka
        session.run("CREATE (node3:kafka {name:'192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092',type: 'kafka',topic: 'ods_default_log'})");
        session.run("CREATE (node4:kafka {name:'192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092',type: 'kafka',topic: 'ods_all_metric'})");
        session.run("CREATE (node5:kafka {name:'192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092',type: 'kafka',topic: 'dwd_default_log'})");
        session.run("CREATE (node6:kafka {name:'192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092',type: 'kafka',topic: 'dwd_all_metric'})");

        session.run("CREATE (node3_1:streamx {name:'merge_analysis_task_ods_default_log',type: '数据解析',topic: 'dwd_default_log',format:'log'})");
        session.run("CREATE (node3_2:streamx {name:'merge_analysis_task_ods_all_metric',type: '数据解析',topic: 'dwd_all_metric',format:'metric'})");
        session.run("CREATE (node4_1:streamx {name:'log2es_dwd_default_log',type: '数据入库Es',index: 'dwd_default_log' ,format:'log'})");
        session.run("CREATE (node4_2:streamx {name:'metricstore_dwd_all_metric',type: '指标入库',database: 'dwd_all_metric',format:'metric'})");

        // 定义关系
        session.run("MATCH (node91:filebeat),(node2_92:logstash)  where node91.name = '192.168.1.91' AND node2_92.name = '192.168.2.92' CREATE (node91)-[:filebeat数据采集] ->(node2_92) RETURN node91,node2_92");
        session.run("MATCH (node92:filebeat),(node2_92:logstash)  where node92.name = '192.168.1.92' AND node2_92.name = '192.168.2.92'  CREATE (node92)-[:filebeat数据采集] ->(node2_92) RETURN node92,node2_92");
//        session.run("MATCH (node93:filebeat),(node2_92:logstash)  where node93.name = '192.168.1.93' AND node2_92.name = '192.168.2.92' CREATE (node93)-[:filebeat数据采集] ->(node2_92) RETURN node93,node2_92");
//        session.run("MATCH (node94:filebeat),(node2_92:logstash)  where node94.name = '192.168.1.94' AND node2_92.name = '192.168.2.92' CREATE (node94)-[:filebeat数据采集] ->(node2_92) RETURN node94,node2_92");
//        session.run("MATCH (node95:filebeat),(node2_92:logstash)  where node95.name = '192.168.1.95' AND node2_92.name = '192.168.2.92' CREATE (node95)-[:filebeat数据采集] ->(node2_92) RETURN node95,node2_92");
        session.run("MATCH (node96:metricbeat),(node2_92:logstash)  where node96.name = '192.168.1.96' AND node2_92.name = '192.168.2.92' CREATE (node96)-[:metricbeat数据采集] ->(node2_92) RETURN node96,node2_92");
        session.run("MATCH (node97:metricbeat),(node2_92:logstash)  where node97.name = '192.168.1.97' AND node2_92.name = '192.168.2.92' CREATE (node97)-[:metricbeat数据采集] ->(node2_92) RETURN node97,node2_92");

        session.run("MATCH (node91:filebeat),(node2_93:logstash)  where node91.name = '192.168.1.91' AND node2_93.name = '192.168.2.93' CREATE (node91)-[:filebeat数据采集] ->(node2_93) RETURN node91,node2_93");
        session.run("MATCH (node92:filebeat),(node2_93:logstash)  where node92.name = '192.168.1.92' AND node2_93.name = '192.168.2.93'  CREATE (node92)-[:filebeat数据采集] ->(node2_93) RETURN node92,node2_93");
//        session.run("MATCH (node93:filebeat),(node2_93:logstash)  where node93.name = '192.168.1.93' AND node2_93.name = '192.168.2.93' CREATE (node93)-[:filebeat数据采集] ->(node2_93) RETURN node93,node2_93");
//        session.run("MATCH (node94:filebeat),(node2_93:logstash)  where node94.name = '192.168.1.94' AND node2_93.name = '192.168.2.93' CREATE (node94)-[:filebeat数据采集] ->(node2_93) RETURN node94,node2_93");
//        session.run("MATCH (node95:filebeat),(node2_93:logstash)  where node95.name = '192.168.1.95' AND node2_93.name = '192.168.2.93' CREATE (node95)-[:filebeat数据采集] ->(node2_93) RETURN node95,node2_93");
        session.run("MATCH (node96:metricbeat),(node2_93:logstash)  where node96.name = '192.168.1.96' AND node2_93.name = '192.168.2.93' CREATE (node96)-[:metricbeat数据采集] ->(node2_93) RETURN node96,node2_93");
        session.run("MATCH (node97:metricbeat),(node2_93:logstash)  where node97.name = '192.168.1.97' AND node2_93.name = '192.168.2.93' CREATE (node97)-[:metricbeat数据采集] ->(node2_93) RETURN node97,node2_93");


        session.run("MATCH (node3:kafka),(node2_92:logstash)  where node3.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node2_92.name = '192.168.2.92'  AND node3.topic='ods_default_log' CREATE (node2_92)-[:日志数据传输到kafka] ->(node3) RETURN node2_92,node3");
        session.run("MATCH (node3:kafka),(node2_93:logstash)  where node3.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node2_93.name = '192.168.2.93'  AND node3.topic='ods_default_log' CREATE (node2_93)-[:日志数据传输到kafka] ->(node3) RETURN node2_93,node3");

        session.run("MATCH (node4:kafka),(node2_92:logstash)  where node4.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node2_92.name = '192.168.2.92'  AND node4.topic='ods_all_metric' CREATE (node2_92)-[:指标数据传输到kafka] ->(node4) RETURN node2_92,node4");
        session.run("MATCH (node4:kafka),(node2_93:logstash)  where node4.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node2_93.name = '192.168.2.93'  AND node4.topic='ods_all_metric' CREATE (node2_93)-[:指标数据传输到kafka] ->(node4) RETURN node2_93,node4");

//        session.run("MATCH (node4:kafka),(node2_93:logstash)  where node4.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node2_93.name = '192.168.2.93' CREATE (node2_93)-[:数据传输到kafka] ->(node4) RETURN node2_93,node4");

        session.run("MATCH (node3:kafka),(node3_1:streamx)  where node3.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node3.topic='ods_default_log' AND node3_1.name = 'merge_analysis_task_ods_default_log' AND node3_1.format='log' AND node3_1.topic = 'dwd_default_log' CREATE (node3)-[:日志数据解析] ->(node3_1) RETURN node3,node3_1");

        session.run("MATCH (node3_1:streamx),(node5:kafka)  where  node3_1.name = 'merge_analysis_task_ods_default_log' " +
                "AND node3_1.format='log' " +
                "AND node3_1.topic = 'dwd_default_log' " +
                "AND node5.topic = 'dwd_default_log' " +
                "AND node5.type='kafka' " +
                "CREATE (node3_1)-[:数据回写到kafka] ->(node5) RETURN node3_1, node5");
        session.run("MATCH (node5:kafka), (node4_1:streamx) where node5.topic = 'dwd_default_log' AND node4_1.format='log' AND node4_1.name = 'log2es_dwd_default_log' AND node4_1.format='log' CREATE (node5)-[:数据入库到Es] ->(node4_1) RETURN node5,node4_1");


        session.run("MATCH (node4:kafka),(node3_2:streamx)  where node4.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node4.topic='ods_all_metric' AND node3_2.name = 'merge_analysis_task_ods_all_metric' AND node3_2.format='metric' AND node3_2.topic = 'dwd_all_metric' CREATE (node4)-[:指标数据解析] ->(node3_2) RETURN node4,node3_2");
        session.run("MATCH (node3_2:streamx),(node6:kafka)  where  node3_2.name = 'merge_analysis_task_ods_all_metric' " +
                "AND node3_2.format='metric' " +
                "AND node3_2.topic = 'dwd_all_metric' " +
                "AND node6.topic = 'dwd_all_metric' " +
                "AND node6.type='kafka' " +
                "CREATE (node3_2)-[:数据回写到kafka] ->(node6) RETURN node3_2, node6");
        session.run("MATCH (node6:kafka), (node4_2:streamx) where node6.topic = 'dwd_all_metric' AND node4_2.format='metric' AND node4_2.name = 'metricstore_dwd_all_metric' CREATE (node6)-[:指标数据入库] ->(node4_2) RETURN node6,node4_2");

//        session.run("MATCH (node4:kafka),(node3_2:streamx)  where node4.name = '192.168.70.6:19092,192.168.70.7:19092,192.168.70.7:19092' AND node4.topic='ods_all_metric' AND node3_2.name = 'merge_analysis_task_ods_default_log' AND node3_2.format='metric' CREATE (node4)-[:数据解析] ->(node3_2) RETURN node4,node3_2");
//        session.run("MATCH (node3_2:streamx), (node4_2:streamx) where node3_2.name = 'merge_analysis_task_ods_default_log' AND node3_2.topic='dwd_all_metric' AND node3_2.format='metric' AND node4_2.name = 'metricstore_dwd_all_metric' AND node4_2.format='metric' CREATE (node3_2)-[:数据入库到Es] ->(node4_2) RETURN node3_2,node4_2");
        session.close();
        driver.close();
    }
}
