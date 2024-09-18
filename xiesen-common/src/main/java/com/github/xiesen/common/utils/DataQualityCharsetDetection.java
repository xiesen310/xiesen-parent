package com.github.xiesen.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public class DataQualityCharsetDetection {
    private static final String DATA_QUALITY_REGEX = "[\\x00-\\x7F]*";
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "Jul  5 10:32:02 zork-rd-dev-9094 kernel: bash (22051): drop_caches: 3 华为正式发布全球首款三折叠屏手机，售价19999元起 �������";
        if (isGarbled(str)) {
            System.out.println("字符串为乱码");
        } else {
            System.out.println("字符串不是乱码");
        }

    }



    private static boolean isGarbled(String text) {
        final double threshold = 0;
        int nonPrintableCount = 0;
        for (char c : text.toCharArray()) {
            if (!Character.isDefined(c) || Character.isISOControl(c)) {
                nonPrintableCount++;
            }
        }

        if ((double) nonPrintableCount / text.length() > threshold) {
            return true;
        }

        /// 尝试使用不同的编码解码
        try {
            Charset[] charsets = {StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII};
            for (Charset charset : charsets) {
                byte[] bytes = text.getBytes(charset);
                String decodedText = new String(bytes, charset);
                if (decodedText.equals(text)) {
                    /// 成功解码，不是乱码
                    return false;
                }
            }
        } catch (UnsupportedCharsetException e) {
            /// 如果出现编码不支持的异常，也可以认为是乱码
            return true;
        }

        /// 通常乱码的特点是包含一些无法识别的特殊字符,我们可以通过正则表达式来匹配这些特殊字符
        if (text.matches(DATA_QUALITY_REGEX)) {
            return true;
        }
        /// 未能确定为乱码
        return false;
    }
}
