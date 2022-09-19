package com.github.xiesen.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

/**
 * @author xiesen
 * @title: ReadHdfsFile
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/8/29 16:37
 */
public class ReadHdfsFile {
    public static void main(String[] args) throws IOException {
        readFileToConsole("/stu.csv");
    }

    //    读取hdfs文件系统中的文件
    private static void readFileToConsole(String path) throws IOException {
//        获取配置
        Configuration conf = new Configuration();
//        配置
        conf.set("fs.defaultFS", "hdfs://cdh-2:8020");
//        获取hdfs文件系统的操作对象
        FileSystem fs = FileSystem.get(conf);
//        具体的对文件系统的操作
        FSDataInputStream fsDataInputStream = fs.open(new Path(path));
        IOUtils.copyBytes(fsDataInputStream, System.out, 4096, true);


    }
}
