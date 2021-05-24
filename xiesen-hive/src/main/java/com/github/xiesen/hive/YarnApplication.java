package com.github.xiesen.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;

/**
 * yarn application 操作
 *
 * @author 谢森
 * @since 2021/5/24
 */
public class YarnApplication {
    private YarnClient yarnClient;

    public static final String YARN_CONF_DIR = "D:\\tmp\\yarn-clientconfig\\yarn-conf";
    public static final String APPLICATION_ID = "application_1617959156629_0637";

    public static void main(String[] args) throws IOException, YarnException {
        YarnApplication application = new YarnApplication();
        application.init(YARN_CONF_DIR);
        application.killApplication(APPLICATION_ID);
    }


    /**
     * 初始化 yarn 客户端
     *
     * @param yarnConfDir yarn 配置文件目录
     */
    public void init(String yarnConfDir) {
        Configuration configuration = YarnConfLoader.getYarnConf(yarnConfDir);
        yarnClient = YarnClient.createYarnClient();
        yarnClient.init(configuration);
        yarnClient.start();
    }

    /**
     * 根据应用 id 杀掉 yarn 应用
     *
     * @param applicationId 应用 id
     * @throws YarnException
     * @throws IOException
     */
    private void killApplication(String applicationId) throws YarnException,
            IOException {
        ApplicationId appId = ConverterUtils.toApplicationId(applicationId);
        ApplicationReport appReport = null;
        try {

            appReport = yarnClient.getApplicationReport(appId);
        } catch (ApplicationNotFoundException e) {
            System.out.println("Application with id '" + applicationId +
                    "' doesn't exist in RM.");
            throw e;
        }

        if (appReport.getYarnApplicationState() == YarnApplicationState.FINISHED
                || appReport.getYarnApplicationState() == YarnApplicationState.KILLED
                || appReport.getYarnApplicationState() == YarnApplicationState.FAILED) {
            System.out.println("Application " + applicationId + " has already finished ");
        } else {
            System.out.println("Killing application " + applicationId);
            yarnClient.killApplication(appId);
        }
    }
}
