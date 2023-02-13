package com.github.xiesen.consanguinity.logstash;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

/**
 * @author xiesen
 */
public class TestLogstashConf {
    public static final String LOGSTASH_PATH = "D:\\develop\\workspace\\xiesen-parent\\xiesen-consanguinity\\src\\main\\resources\\logstash.conf";

    public static void main(String[] args) {

        File confFile = new File(LOGSTASH_PATH);
        final Config config = ConfigFactory.parseFile(confFile);

        System.out.println(config);

    }
}
