package com.github.xiesen.junit.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author 谢森
 * @Description http 连接池
 * @Email xiesen310@163.com
 * @Date 2020/12/16 10:09
 */
public class PoolManager {
    private static PoolingHttpClientConnectionManager clientConnectionManager = null;
    private static CloseableHttpClient httpclient = null;
    private static int defaultMaxTotal = 200;
    private static int defaultMaxPerRoute = 25;
    private static int defaultSocketTimeout = 30000;
    private static int defaultConnectTimeout = 30000;

    public synchronized static void getInstance(int maxTotal, int maxPerRoute) {
        try {
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .build();
            clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            if (maxTotal > 0) {
                clientConnectionManager.setMaxTotal(maxTotal);
            } else {
                clientConnectionManager.setMaxTotal(defaultMaxTotal);
            }
            if (maxPerRoute > 0) {
                clientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
            } else {
                clientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
            }
        } catch (Exception e) {
            System.out.println("初始化clientConnectionManager失败" + e);
        }
    }


    /**
     * 获取 httpClient
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getHttpClient() {
        return getHttpClient(defaultConnectTimeout, defaultSocketTimeout);
    }

    /**
     * 获取 httpClient
     *
     * @param connectTimeout http超时时间
     * @param socketTimeout  socket 超时时间
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getHttpClient(int connectTimeout, int socketTimeout) {
        return getHttpClient(connectTimeout, socketTimeout, defaultMaxTotal, defaultMaxPerRoute);
    }

    /**
     * 获取 httpClient
     *
     * @param connectTimeout http超时时间
     * @param socketTimeout  socket 超时时间
     * @param maxTotal       设置最大连接数
     * @param maxPerRoute    将每个路由默认最大连接数
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getHttpClient(int connectTimeout, int socketTimeout, int maxTotal,
                                                    int maxPerRoute) {

        RequestConfig requestConfig =
                RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
        if (clientConnectionManager == null) {
            clientConnectionManager = new PoolingHttpClientConnectionManager();
            clientConnectionManager.setMaxTotal(maxTotal);
            clientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
        }
        if (httpclient == null) {
            httpclient = HttpClients.custom().setConnectionManager(clientConnectionManager)
                    .setDefaultRequestConfig(requestConfig).setConnectionManagerShared(true).build();
        }
        return httpclient;
    }
}
