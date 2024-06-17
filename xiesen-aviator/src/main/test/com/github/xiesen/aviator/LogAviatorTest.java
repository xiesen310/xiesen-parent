package com.github.xiesen.aviator;

import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;

public class LogAviatorTest {


    @JunitPerfConfig(duration = 10000, threads = 3)
    public void method1() {
        LogAviator.method1();
    }

    @JunitPerfConfig(duration = 10000, threads = 1)
    public void method2() {
        LogAviator.method2();
    }

    @JunitPerfConfig(duration = 10000)
    public void method3() {
        LogAviator.method3();
    }

    @JunitPerfConfig(duration = 10000)
    public void method4() {
        LogAviator.method4();
    }

    @JunitPerfConfig(duration = 10000)
    public void method5() {
        LogAviator.method5();
    }

}
