package com.github.xiesen.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GtjaqljUtil {
    public static String encode3DES(String strEncode, String strKey, String StrIv) {
        String result = null;
        try {
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] key = base64decoder.decodeBuffer(strKey);
            byte[] iv = base64decoder.decodeBuffer(StrIv);
            result = encryptWithIV(key, iv, strEncode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前日期时间并格式化为 "yyyy-MM-dd HH:mm:ss" 格式的字符串
     * @return 格式化后的日期时间字符串
     */
    public static String getFormattedCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取并返回格式化后的当前日期字符串
     *
     * @return 格式为 "YYYYMMdd" 的日期字符串
     */
    public static String generateFormattedDate() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 返回格式化后的日期字符串
        return currentDate.format(formatter);
    }



    private static String encryptWithIV(byte[] key, byte[] iv, String str) throws BadPaddingException,
            Exception {
        SecureRandom sr = new SecureRandom();
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec ips = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS7Padding");
        cipher.init(1, securekey, ips, sr);
        byte[] bt = cipher.doFinal(str.getBytes("UTF-8"));
        return new String(new BASE64Encoder().encode(bt));
    }
}
