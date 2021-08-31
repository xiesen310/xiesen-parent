package com.github.xiesen.jython;

import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Properties;

public class HttpDemo {
    public static void main(String[] args) throws FileNotFoundException {
//        localExecPyFile();
        jvmExecJsonDemo();
    }

    private static void jvmExecJsonDemo() throws FileNotFoundException {
        PythonInterpreter interpreter = new PythonInterpreter();

        Properties props = new Properties();
        props.put("python.console.encoding", "UTF-8");
        /**
         *  don't respect java accessibility, so that we can access protected members on subclasses
         */
        props.put("python.security.respectJavaAccessibility", "false");
        props.put("python.import.site","false");
        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops, props, new String[0]);
        interpreter.exec("import sys");
        interpreter.exec("import sys.path");
        interpreter.exec("path = \"D:\\soft\\anaconda3\\Lib\\site-packages\"");
        interpreter.exec("path = \"D:\\soft\\anaconda3\\python38.zip\"");
        interpreter.exec("path = \"D:\\soft\\anaconda3\\lib\"");
        interpreter.exec("path = \"D:\\soft\\anaconda3\"");
        interpreter.exec("path = \"D:\\soft\\anaconda3\\lib\\site-packages\\win32\"");
        interpreter.exec("path = \"D:\\soft\\anaconda3\\lib\\site-packages\\win32\\lib\"");
        interpreter.exec("path = \"D:\\soft\\anaconda3\\lib\\site-packages\\Pythonwin\"");

        interpreter.exec("sys.path.append(path)");
        interpreter.exec("import sys.path");
        interpreter.exec("import requests");


//        interpreter.execfile("D:\\develop\\workspace\\python\\helloworld\\jsonDemo.py");
        interpreter.execfile(new FileInputStream("D:\\develop\\workspace\\python\\helloworld\\httpDemo.py"));
        interpreter.close();
    }

    private static void localExecPyFile() {
        String[] arguments = new String[]{"python", "D:\\develop\\workspace\\python\\helloworld\\httpDemo.py"};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            /**
             * 打印 python 执行结果
             */
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();

            // 返回执行是否成功, 0 代表成功,1 代表失败
            String result = process.waitFor() == 0 ? "success" : "fail";
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
