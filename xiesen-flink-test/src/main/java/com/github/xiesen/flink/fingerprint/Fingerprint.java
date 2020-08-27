package com.github.xiesen.flink.fingerprint;

/**
 * Overview of Rabin's scheme given by Broder
 * Some Applications of Rabin's Fingerprinting Method
 *
 * @param <T>
 */
public interface Fingerprint<T> {
    public void pushBytes(byte[] bytes);

    public void pushBytes(byte[] bytes, int offset, int length);

    public void pushByte(byte b);

    public void reset();

    public T getFingerprint();

    public static interface WindowedFingerprint<T> extends Fingerprint<T> {
        public void popByte();
    }
}
