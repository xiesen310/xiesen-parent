package com.github.xiesen.batch;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;
/**
 * @author xiesen
 * @title: BatchExec
 * @projectName xiesen-parent
 * @description: TODO
 * @date 2022/2/17 15:41
 */
public class BatchExec<T> {
    private Logger logger = Logger.getLogger(this.getClass());
    public int maxCount;//批处理触发最大条数
    public int waitMilliSecond;//批处理触发最大等待时间
    public List<T> dataList;//批处理缓存数据
    public Thread timeJob;//时间触发任务
    public Function<List<T>, Object> function;//jdk1.8中的函数式编程,将方法当成参数
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//时间格式化

    /**
     * 默认初始化
     */
    public BatchExec() {
        this.dataList = new ArrayList<>(100);//保存数据使用
        this.function = result -> {
            logger.info(String.format("触发默认方法,数据长度:%s", result.size()));
            return result;
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
    public BatchExec(int maxCount, int waitMilliSecond, Function<List<T>, Object> function) {
        this.dataList = new ArrayList<>(maxCount);//使用连表
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
                Thread.sleep(waitMilliSecond);
                waitMilliSecond();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return job;
    }

    /**
     * 往集合中添加需要处理的数据
     *
     * @param data
     */
    public synchronized void add(T data) {
        this.dataList.add(data);
        if (this.dataList.size() == this.maxCount) {//触发
            timeJob.stop();
            exec("maxCount");
            //重置时间与数量
            this.timeJob = this.initThread();
            this.timeJob.start();
        }
    }

    /**
     * 达到一定时间触发任务
     * 达到时间执行与达到数量执行两个方法需要加锁,即waitMilliSecond 与 add
     */
    public synchronized void waitMilliSecond() {
        if (this.dataList.size() != 0) {//空数据
            exec("timeType");
        }
        //重置时间任务
        this.timeJob = this.initThread();
        this.timeJob.start();
    }

    /**
     * 任务执行并清空已经处理的数据
     *
     * @param type
     */
    public void exec(String type) {
        logger.debug(String.format("time :[%s], trigger type [%s]", getNowFormatDay(), type));
        function.apply(this.dataList);
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

}
