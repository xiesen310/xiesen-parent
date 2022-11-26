package com.github.xiesen.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author xiesen
 * @title: HadoopTets
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/11/8 17:45
 */
public class HadoopTest {
    public static void main(String[] args) throws Exception {


        test1();
//        simpleAuth();
    }

    private static void simpleAuth() throws IOException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://zork7084:8020");
        //FileSystem hdfs = FileSystem.get(conf);
        FileSystem hdfs = FileSystem.get(new URI("hdfs://zork7084:8020"), conf, "hdfs");
        Path path = new Path("/user/hdfs");
        FileStatus[] files = hdfs.listStatus(path);
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getPath());
        }
    }

    private static void test1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://zork7084:8020");
        FileSystem hdfs = FileSystem.get(conf);
        Path path = new Path("/user/hdfs");
        FileStatus[] files = hdfs.listStatus(path);
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getPath());
        }
    }
}
