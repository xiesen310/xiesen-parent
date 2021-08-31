package com.github.xiesen.jython;

import java.io.*;

/**
 * 在本地环境中调用 Python 脚本
 */
public class PyCaller {
    private static final String PY_URL = "D:\\develop\\workspace\\python\\helloworld\\helloWorld.py";

    public static void execPy() {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("python " + PY_URL);
            int i = proc.waitFor();
            String result = i == 0 ? "success" : "fail";
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 测试码
    public static void main(String[] args) throws IOException, InterruptedException {
        execPy();
    }
}
