package com.github.xiesen.mock.test;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiesen
 * @title: BatchCommon
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/2/17 15:47
 */
public class BatchCommon<T> {
    private Logger logger = Logger.getLogger(this.getClass());
    public int maxCount;//批处理触发最大条数
    public int waitMilliSecond;//批处理触发最大等待时间
    public List<T> dataList;//批处理缓存数据,有锁
    public Thread timeJob;//时间触发任务
    public Consumer<List<T>> function;//函数式编程,将方法当成参数,这个函数可以不用返回值,Function必须有返回值
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//时间格式化
    public boolean status = true;//两次休眠之间是否有达到条数触发,修改变量有锁,一定误差计时的误差范围是[0,最大等待时间]

    /**
     * 默认初始化
     */
    public BatchCommon() {
        this.dataList = new ArrayList<>(100);//保存数据使用
        this.function = result -> {
            logger.info(String.format("触发默认方法,数据长度:%s", result.size()));
        };
        this.maxCount = 100;
        this.waitMilliSecond = 3000;
        this.timeJob = this.initThread();
        this.timeJob.start();
    }

    /**
     * 指定参数初始化
     *
     * @param maxCount
     * @param waitMilliSecond
     */
    public BatchCommon(int maxCount, int waitMilliSecond, Consumer<List<T>> function) {
        this.dataList = new ArrayList<>(maxCount);//使用数组
        this.function = function;
        this.maxCount = maxCount;
        this.waitMilliSecond = waitMilliSecond;
        this.timeJob = this.initThread();
        this.timeJob.start();
    }

    /**
     * 重置时间间隔任务任务
     *
     * @return
     */
    public Thread initThread() {
        Thread job = new Thread(() -> {//定义
            try {
                while (true) {
                    Thread.sleep(waitMilliSecond);
                    if (this.isTimeExec()) waitMilliSecond();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return job;
    }

    /**
     * 是否需要触发时间执行
     *
     * @return
     */
    public synchronized boolean isTimeExec() {
        if (this.status) {
            this.status = false;//更新当前时间时间检查没有出现条数触发
            return false;//上一个时间检查存在条数触发,不可以更新任务
        } else {
            return true;//没有条数触发,可以执行任务
        }
    }

    /**
     * 往集合中添加需要处理的数据
     *
     * @param data
     */
    public synchronized void add(T data) {
        this.dataList.add(data);
        if (this.dataList.size() == this.maxCount) {//触发
            this.status = true;//记录已经发生条数触发
            exec("maxCount");//条数触发
        }
    }

    /**
     * 达到一定时间触发任务
     * 达到时间执行与达到数量执行两个方法需要加锁,即waitMilliSecond 与 add
     */
    public synchronized void waitMilliSecond() {
        if (this.dataList.size() != 0) {//空数据
            exec("timeType");//时间触发
        }
    }

    /**
     * 任务执行并清空已经处理的数据
     *
     * @param type 触发任务类型
     */
    public void exec(String type) {
        logger.warn(String.format("time :[%s], trigger type [%s]", getNowFormatDay(), type));
        function.accept(this.dataList);
        dataList.clear();//重置数据量
    }

    /**
     * 格式化获取当前时间
     *
     * @return
     */
    public String getNowFormatDay() {
        String dateString;
        Calendar cal = Calendar.getInstance();
        dateString = this.sdf.format(cal.getTime());
        return dateString;
    }


    public static void main(String[] args) throws InterruptedException {
        Consumer<List<String>> consumer = list -> {
            System.out.println(list.size());
        };
        BatchCommon<String> stringBatchCommon = new BatchCommon<>(2, 1000, consumer);
        while (true) {
            Thread.sleep(1000);
            stringBatchCommon.add("");
        }
    }
}
