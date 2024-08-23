package com.github.xiesen.test;

public class MyClass {
    public static void main(String[] args) {
        // 调用方法
        callMethod();
    }

    public static void callMethod() {
        // 调用另一个方法
        anotherMethod();
    }

    public static void anotherMethod() {
        // 获取调用这个方法的类信息
        getCallingClassName();
    }

    public static void getCallingClassName() {
        // 获取当前线程的堆栈跟踪
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        // 当前方法是第四个元素（索引为3），因此调用者是第三个元素（索引为2）
        // 第一个元素是getStackTrace()方法本身
        // 第二个元素是getCallingClassName()方法
        // 第三个元素是调用getCallingClassName()的方法
        StackTraceElement caller = stackTraceElements[2];

        // 输出调用者的类名
        System.out.println("Called from class: " + caller.getClassName());
    }
}
