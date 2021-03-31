package com.github.xiesen.junit.test;

import cn.hutool.core.io.file.FileReader;

/**
 * @author 谢森
 * @since 2021/3/25
 */
public class FileReaderUtils {
    public static void main(String[] args) {
        FileReader fileReader = new FileReader("analysis_conf.conf");
        String result = fileReader.readString();

        System.out.println(result);
    }
}
