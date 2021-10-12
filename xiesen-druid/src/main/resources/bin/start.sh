#!/bin/bash
##################################################
#
# description: 测试 druid
# create time: 2021.08.11
# author: xiesen
#
##################################################

# druid2ck.properties 配置文件路径
confPath=D:\develop\workspace\zork\br-kafka-trace\src\main\resources\config.properties

java -cp xiesen-druid-1.0.0-jar-with-dependencies.jar com.github.xiesen.druid.TestDruid $confPath