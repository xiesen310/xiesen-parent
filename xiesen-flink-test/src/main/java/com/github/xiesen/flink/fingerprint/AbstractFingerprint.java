package com.github.xiesen.flink.fingerprint;

import com.github.xiesen.flink.rabin.Polynomial;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/8/24 9:21
 */
public abstract class AbstractFingerprint implements Fingerprint<Polynomial> {
    protected final Polynomial poly;

    public AbstractFingerprint(Polynomial poly) {
        this.poly = poly;
    }

    public void pushBytes(final byte[] bytes) {
        for (byte b : bytes) {
            pushByte(b);
        }
    }

    public void pushBytes(final byte[] bytes, final int offset, final int length) {
        final int max = offset + length;
        int i = offset;
        while (i < max) {
            pushByte(bytes[i++]);
        }
    }

    public abstract void pushByte(byte b);

    public abstract void reset();

    public abstract Polynomial getFingerprint();

    @Override
    public String toString() {
        return getFingerprint().toHexString();
    }
}