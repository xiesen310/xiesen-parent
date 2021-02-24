package com.github.xiesen.classloader;

import java.lang.reflect.Method;

/**
 * @author 谢森
 * @since 2021/2/23
 */
public class MyClassLoaderTest {
    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getContextClassLoader());

        MyClassLoader myClassLoader = new MyClassLoader("D:\\develop\\workspace\\xiesen\\xiesen-parent\\xiesen-kafka\\target\\classes\\com\\github\\xiesen\\kafka\\HelloWorld.class");
        Thread.currentThread().setContextClassLoader(myClassLoader);

        Class clazz = myClassLoader.loadClass("com.github.xiesen.kafka.HelloWorld");
        Object obj = clazz.newInstance();

        Method m1 = clazz.getMethod("sayHello", String.class);
        m1.invoke(obj, "xs");
        System.out.println(Thread.currentThread().getContextClassLoader());
        System.out.println(obj.getClass().getClassLoader());
    }
}
