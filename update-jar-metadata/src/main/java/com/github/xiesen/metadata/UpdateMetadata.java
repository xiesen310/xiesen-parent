package com.github.xiesen.metadata;

import java.io.*;

/**
 * @author 谢森
 * @since 2021/4/9
 */
public class UpdateMetadata {
    public static void main(String[] args) throws IOException {

        String zipFilePath = "E:\\tmp\\smartdata-streamx\\lib";
        String targetPath = "E:\\tmp\\smartdata-streamx\\lib\\flink-dist_2.11-1.8.3";
        File file = new File(zipFilePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().endsWith("jar")) {
                    String name = f.getName();
                    String substring = name.substring(0, name.length() - 4);
                    String src = zipFilePath + "\\" + name;
                    String target = zipFilePath + "\\" + substring;
                    CompressFileUtils.unzip(src, target);
                    System.out.println(f.getName());

                    CompressFileUtils.delFolder(target + "\\" + "META-INF");


                }
            }
        }


    }


}
