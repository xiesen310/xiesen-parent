package com.github.xiesen.common.utils;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * @author xiesen
 */
public class Sm4UtilTest {
    public static void main(String[] args) throws Exception {
        byte[] genKeyBs = Sm4Util.generateKey();
        System.out.println(genKeyBs);
        String genKeyStr = ByteUtils.toHexString(genKeyBs);
        System.out.println("密钥: " + genKeyStr);

        System.out.println(Sm4Util.decryptEcb(genKeyStr, "nacos"));
    }
}
