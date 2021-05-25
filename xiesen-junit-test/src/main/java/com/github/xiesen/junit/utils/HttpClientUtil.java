package com.github.xiesen.junit.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author 谢森
 * @Description http 工具类
 * @Email xiesen310@163.com
 * @Date 2020/12/16 10:06
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static int poolManagerMaxTotal = 200;
    private static int poolManagerMaxPerRoute = 50;
    private static int socketTimeout = 30000;
    private static int connectTimeout = 30000;

    private static final String REGEX1 = "&";
    private static final String REGEX2 = "=";
    private static final String REGEX3 = "?";

    private static final String UTF_8 = "UTF-8";
    private static final String APPLICATION_JSON = "application/json";
    private static final String FAIL_ERROR = "请求失败";

    /**
     * 设置请求和传输超时时间
     */
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectTimeout)
            .setConnectionRequestTimeout(connectTimeout)
            .setCookieSpec(CookieSpecs.STANDARD)
            .build();

    private HttpClientUtil() {
    }

    /**
     * 获取httpclient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        return PoolManager.getHttpClient(socketTimeout, connectTimeout);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public static String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);
        StringEntity stringEntity = new StringEntity(params, UTF_8);
        stringEntity.setContentType(APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        return sendHttpPost(httpPost);
    }

    public static Boolean sendHttpPostWithResult(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);
        StringEntity stringEntity = new StringEntity(params, UTF_8);
        stringEntity.setContentType(APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        return sendHttpPostWithResult(httpPost);
    }

    public static String sendHttpPost(String httpUrl, String params, Map<String, String> header) {
        HttpPost httpPost = new HttpPost(httpUrl);
        StringEntity stringEntity = new StringEntity(params, UTF_8);
        stringEntity.setContentType(APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        return sendHttpPost(httpPost, header);
    }

    public static String sendHttpPostByHeader(String httpUrl, Map<String, Object> params, Map<String, String> header) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(httpUrl);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, UTF_8));
        return sendHttpPost(httpPost, header);
    }

    public static Boolean sendHttpPostByText(String httpUrl, Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer(httpUrl);
        sb.append("?");
        for (Iterator<?> iter = params.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<?, ?> element = (Map.Entry<?, ?>) iter.next();
            sb.append(element.getKey().toString());
            sb.append("=");
            sb.append(URLEncoder.encode(element.getValue().toString()));
            sb.append(REGEX1);
        }
        HttpPost httpPost = new HttpPost(sb.toString());
        httpPost.setHeader("Content-Type", "text/plain");
        return sendHttpPostWithResult(httpPost);
    }

    public static String sendHttpPostForm(String httpUrl, List<NameValuePair> params, Map<String, String> header) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(httpUrl);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, UTF_8);
        httpPost.setEntity(entity);
        entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        return sendHttpPost(httpPost, header);
    }

    public static String sendHttpDelete(String httpUrl) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        return sendHttpDelete(httpDelete);
    }

    public static String sendHttpPut(String httpUrl) {
        HttpPut httpPut = new HttpPut(httpUrl);
        return sendHttpPut(httpPut);
    }

    public static String sendHttpPut(String httpUrl, String params, Map<String, String> header) {
        HttpPut httpPut = new HttpPut(httpUrl);
        StringEntity stringEntity = new StringEntity(params, UTF_8);
        stringEntity.setContentType(APPLICATION_JSON);
        httpPut.setEntity(stringEntity);
        return sendHttpPut(httpPut, header);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(httpUrl);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, UTF_8));
        return sendHttpPost(httpPost);
    }


    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private static String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpClient = getHttpClient();
            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                responseContent = EntityUtils.toString(resEntity, UTF_8);
            } else {
                logger.info("请求bizpc服务失败，状态码为：{}", statusCode);
            }
        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private static Boolean sendHttpPostWithResult(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        Boolean responseContent = false;
        try {
            httpClient = getHttpClient();
            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                responseContent = true;
            } else {
                logger.info("请求bizpc服务失败，状态码为：{}", statusCode);
            }
        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    private static String sendHttpPost(HttpPost httpPost, Map<String, String> header) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpClient = getHttpClient();
            httpPost.setConfig(requestConfig);
            if (header != null && header.size() > 0) {
                Iterator<String> iterator = header.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    httpPost.setHeader(key, header.get(key));
                }
            }
            response = httpClient.execute(httpPost);
            response.getStatusLine().getStatusCode();
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, UTF_8);
        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    private static String sendHttpPut(HttpPut httpPut, Map<String, String> header) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpClient = getHttpClient();
            httpPut.setConfig(requestConfig);
            if (header != null && header.size() > 0) {
                Iterator<String> iterator = header.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    httpPut.setHeader(key, header.get(key));
                }
            }
            response = httpClient.execute(httpPut);
            response.getStatusLine().getStatusCode();
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, UTF_8);
        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    private static String sendHttpDelete(HttpDelete httpDelete) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpClient = getHttpClient();
            httpDelete.setConfig(requestConfig);
            response = httpClient.execute(httpDelete);
            response.getStatusLine().getStatusCode();
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, UTF_8);

        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    private static String sendHttpPut(HttpPut httpPut) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpClient = getHttpClient();
            httpPut.setConfig(requestConfig);
            response = httpClient.execute(httpPut);
            response.getStatusLine().getStatusCode();
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity, UTF_8);

        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    public static String httpGet(String url, Map map) throws Exception {
        CloseableHttpResponse resp = null;
        CloseableHttpClient client = null;
        HttpEntity he = null;
        String respContent = null;
        if (!map.isEmpty()) {
            try {
                StringBuffer urlinfo = new StringBuffer();
                urlinfo.append(url);
                urlinfo.append(REGEX3);
                urlinfo.append(getUrlParamsByMap(map));
                HttpGet httpGet = new HttpGet(urlinfo.toString());
                client = PoolManager.getHttpClient(30000, 30000);
                resp = client.execute(httpGet);
                respContent = EntityUtils.toString(resp.getEntity()).trim();
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    if (he != null) {
                        EntityUtils.consume(he);
                    }
                    if (resp != null) {
                        resp.close();
                    }
                    if (client != null) {
                        client.close();
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return respContent;
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String valuestr = getUrlEncoderString(entry.getValue().toString());
            sb.append(entry.getKey() + REGEX2 + valuestr);
            sb.append(REGEX1);
        }
        String params = sb.toString();
        if (params.endsWith(REGEX1)) {
            params = StringUtils.substringBeforeLast(params, REGEX1);
        }
        return params;
    }

    /**
     * URL 转码
     *
     * @param s
     * @return String
     */
    public static String getUrlEncoderString(String s) {
        String result = "";
        if (null == s) {
            return "";
        }
        try {
            result = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public static String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet);
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public static Boolean sendHttpGetWithParam(String httpUrl, Map<String, Object> params) {
        StringBuffer sb = new StringBuffer(httpUrl);
        sb.append(REGEX3);
        for (Iterator<?> iter = params.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<?, ?> element = (Map.Entry<?, ?>) iter.next();
            sb.append(element.getKey().toString());
            sb.append(REGEX2);
            if (element.getValue() != null) {
                sb.append(URLEncoder.encode(element.getValue().toString()));
            }
            sb.append(REGEX1);
        }
        HttpGet httpGet = new HttpGet(sb.toString());
        return getHttpGetResult(httpGet);
    }

    public static String sendHttpGetHeader(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        httpGet.setHeader("Content-Type", "application/json; charset=UTF-8");
        return sendHttpGet(httpGet);
    }

    /**
     * 发送get请求
     *
     * @param httpUrl
     * @param header
     * @return
     * @throws Exception
     */
    public static String sendHttpGet(String httpUrl, Map<String, String> header) {
        HttpGet httpGet = new HttpGet(httpUrl);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpClient = getHttpClient();
            httpGet.setConfig(requestConfig);
            if (header != null && header.size() > 0) {
                Iterator<String> iterator = header.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    httpGet.setHeader(key, header.get(key));
                }
            }
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                responseContent = EntityUtils.toString(resEntity, UTF_8);
            }
        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public static String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpsGet(httpGet);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    private static String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = PoolManager.getHttpClient();
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                responseContent = EntityUtils.toString(resEntity, UTF_8);
            }
        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    public static Boolean getHttpGetResult(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        Boolean responseContent = null;
        try {
            httpClient = getHttpClient();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                responseContent = true;
            } else {
                responseContent = false;
            }
        } catch (Exception e) {
            responseContent = false;
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求Https
     *
     * @param httpGet
     * @return
     */
    private static String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            PublicSuffixMatcher publicSuffixMatcher =
                    PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, UTF_8);
        } catch (Exception e) {
            logger.error(FAIL_ERROR, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return responseContent;
    }

    public static final byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                logger.error(FAIL_ERROR, e);
            }
        }
        return new byte[]{};
    }

}
