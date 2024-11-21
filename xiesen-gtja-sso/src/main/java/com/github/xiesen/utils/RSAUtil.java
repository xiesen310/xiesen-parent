package com.github.xiesen.utils;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class RSAUtil {
    public static String rsaEncrypt(String input, String rsaPublicKey) {
        String result = "";
        try {
            // 将Base64编码后的公钥转换成PublicKey对象
            byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(rsaPublicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(keyBytes));
            // 加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] inputArray = input.getBytes();
            int inputLength = inputArray.length;
            System.out.println("加密字节数：" + inputLength);
            // 最大加密字节数，超出最大字节数需要分组加密
            int MAX_ENCRYPT_BLOCK = 117;
            // 标识
            int offSet = 0;
            byte[] resultBytes = {};
            byte[] cache = {};
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                    offSet += MAX_ENCRYPT_BLOCK;
                } else {
                    cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                    offSet = inputLength;
                }
                resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
                System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length,
                        cache.length);
            }
            result = org.apache.commons.codec.binary.Base64.encodeBase64String(resultBytes);
        } catch (Exception e) {
            System.out.println("rsaEncrypt error:" + e.getMessage());
        }
        System.out.println("加密的结果：" + result);
        return result;
    }

    public static Object sendPost(String url,
                                  List<NameValuePair> nameValuePairList) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            /**
             *  创建一个httpclient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建一个post对象
             */
            HttpPost post = new HttpPost(url);
            /**
             * 包装成一个Entity对象
             */
            StringEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            /**
             * 设置请求的内容
             */
            post.setEntity(entity);
            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Content-Type",
                    "application/x-www-form-urlencoded; charset=utf-8"));
            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            /**
             * 执行post请求
             */
            response = client.execute(post);
            /**
             * 获取响应码
             */
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                /**
                 * 通过EntityUitls获取返回内容
                 */
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                /**
                 * 转换成json,根据合法性返回json或者字符串
                 */
                try {
                    jsonObject = JSONObject.parseObject(result);
                    return jsonObject;
                } catch (Exception e) {
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
            client.close();
        }
        return null;
    }

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPvKPORPcwGiLG1w7aRTPYJoTOs5iLAzUtVQG6t+NvzIRsZR70LUDUJ1EZwfZVFCKY96xo8G7FPW+BXe/GAKxCtTkFODbN9uGuwJD3iPml2fvde+Tgzfpv6jEN/IxH2PM+i2aZBDSNxwJGDSTXsPamCCI/Usn2Y9Vzv5CV06C55wIDAQAB";

    //    {"appId": "GTJA_CLOUD", "checkType": "ip", "ip": "10.176.66.5",
    //            "sessionId": session_id, "timestamp": int(time.time())}
    public static void main(String[] args) {
        JSONObject jo = new JSONObject();
        jo.put("sessionId", "22030864aafbbf9db2473da6b553bd0cc147c4018034");
        jo.put("checkType", "ip");
        jo.put("ip", "10.176.66.5");
        jo.put("appId", "GTJA_CLOUD");
        Date tNow = new Date();
        long localts = tNow.getTime() / 1000;
        jo.put("timestamp", localts);
        System.out.println(jo.toJSONString());
        try {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
//            nameValuePairList.add(new BasicNameValuePair("ssoParam", ApiajaxServiceApplication
//                    .rsaEncrypt(URLEncoder.encode(jo.toJSONString()), publicKey)));
            nameValuePairList.add(new BasicNameValuePair("ssoParam",
                    rsaEncrypt(URLEncoder.encode(jo.toJSONString()), publicKey)));
            Object res = sendPost("http://10.187.224.88:8091/LinkSSOServer/ssoVerify",
                    nameValuePairList);
            System.out.println("SSO返回结果" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}