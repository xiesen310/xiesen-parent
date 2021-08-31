package com.github.xiesen.jython;

import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class HelloPython {
    public static void main(String[] args) {
//        execPyScript();

//        execPyFile();
        execPyFunction();
    }

    private static void execPyScript() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("print('hello world')");
    }

    /**
     * 在 JVM 中执行 Python 脚本
     */
    private static void execPyFile() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("D:\\develop\\workspace\\python\\helloworld\\helloWorld.py");
    }

    /**
     * 在 JVM 中调用 Python 编写的函数
     */
    private static void execPyFunction() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("D:\\develop\\workspace\\python\\helloworld\\helloWorld.py");

        /**
         * 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
         */
        PyFunction pyFunction = interpreter.get("hello", PyFunction.class);

        // 调用函数
        PyObject pyObject = pyFunction.__call__();

        System.out.println(pyObject);
    }

}
