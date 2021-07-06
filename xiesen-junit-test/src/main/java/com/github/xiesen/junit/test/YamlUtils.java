package com.github.xiesen.junit.test;

import org.ho.yaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author 谢森
 * @since 2021/2/1
 */
public class YamlUtils {
    public static void main(String[] args) {
        try {
            Yaml.load(new File("D:\\develop\\workspace\\xiesen\\xiesen-parent\\xiesen-junit-test\\src\\main" +
                    "\\resources\\general_dump.yaml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
