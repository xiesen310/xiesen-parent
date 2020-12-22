package com.github.xiesen.algorithm.xxhash;

import java.nio.ByteOrder;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * @author xiese
 * @Description CharSequenceAccess
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:05
 */
abstract class AbstractCharSequenceAccess extends AbstractAccess<CharSequence> {

    static AbstractCharSequenceAccess charSequenceAccess(ByteOrder order) {
        return order == LITTLE_ENDIAN ?
                LittleEndianAbstractCharSequenceAccess.INSTANCE :
                BigEndianAbstractCharSequenceAccess.INSTANCE;
    }

    static AbstractCharSequenceAccess nativeCharSequenceAccess() {
        return charSequenceAccess(ByteOrder.nativeOrder());
    }

    private static int ix(long offset) {
        return (int) (offset >> 1);
    }

    protected static long getLong(CharSequence input, long offset,
                                  int char0Off, int char1Off, int char2Off, int char3Off,
                                  int char4Off, int delta) {
        final int base = ix(offset);
        if (0 == ((int) offset & 1)) {
            final long char0 = input.charAt(base + char0Off);
            final long char1 = input.charAt(base + char1Off);
            final long char2 = input.charAt(base + char2Off);
            final long char3 = input.charAt(base + char3Off);
            return char0 | (char1 << 16) | (char2 << 32) | (char3 << 48);
        } else {
            final long char0 = input.charAt(base + char0Off + delta) >>> 8;
            final long char1 = input.charAt(base + char1Off + delta);
            final long char2 = input.charAt(base + char2Off + delta);
            final long char3 = input.charAt(base + char3Off + delta);
            final long char4 = input.charAt(base + char4Off);
            return char0 | (char1 << 8) | (char2 << 24) | (char3 << 40) | (char4 << 56);
        }
    }

    protected static long getUnsignedInt(CharSequence input, long offset,
                                         int char0Off, int char1Off, int char2Off, int delta) {
        final int base = ix(offset);
        if (0 == ((int) offset & 1)) {
            final long char0 = input.charAt(base + char0Off);
            final long char1 = input.charAt(base + char1Off);
            return char0 | (char1 << 16);
        } else {
            final long char0 = input.charAt(base + char0Off + delta) >>> 8;
            final long char1 = input.charAt(base + char1Off + delta);
            final long char2 = Primitives.unsignedByte(input.charAt(base + char2Off));
            return char0 | (char1 << 8) | (char2 << 24);
        }
    }

    protected static char getUnsignedShort(CharSequence input,
                                           long offset, int char1Off, int delta) {
        if (0 == ((int) offset & 1)) {
            return input.charAt(ix(offset));
        } else {
            final int base = ix(offset);
            final int char0 = input.charAt(base + delta) >>> 8;
            final int char1 = input.charAt(base + char1Off);
            return (char) (char0 | (char1 << 8));
        }
    }

    protected static int getUnsignedByte(CharSequence input, long offset, int shift) {
        return Primitives.unsignedByte(input.charAt(ix(offset)) >> shift);
    }

    private AbstractCharSequenceAccess() {
    }

    @Override
    public int getInt(CharSequence input, long offset) {
        return (int) getUnsignedInt(input, offset);
    }

    @Override
    public int getShort(CharSequence input, long offset) {
        return (int) (short) getUnsignedShort(input, offset);
    }

    @Override
    public int getByte(CharSequence input, long offset) {
        return (int) (byte) getUnsignedByte(input, offset);
    }

    private static class LittleEndianAbstractCharSequenceAccess extends AbstractCharSequenceAccess {
        private static final AbstractCharSequenceAccess INSTANCE = new LittleEndianAbstractCharSequenceAccess();

        private LittleEndianAbstractCharSequenceAccess() {
        }

        @Override
        public long getLong(CharSequence input, long offset) {
            return getLong(input, offset, 0, 1, 2, 3, 4, 0);
        }

        @Override
        public long getUnsignedInt(CharSequence input, long offset) {
            return getUnsignedInt(input, offset, 0, 1, 2, 0);
        }

        @Override
        public int getUnsignedShort(CharSequence input, long offset) {
            return getUnsignedShort(input, offset, 1, 0);
        }

        @Override
        public int getUnsignedByte(CharSequence input, long offset) {
            return getUnsignedByte(input, offset, ((int) offset & 1) << 3);
        }

        @Override
        public ByteOrder byteOrder(CharSequence input) {
            return LITTLE_ENDIAN;
        }
    }

    private static class BigEndianAbstractCharSequenceAccess extends AbstractCharSequenceAccess {
        private static final AbstractCharSequenceAccess INSTANCE = new BigEndianAbstractCharSequenceAccess();

        private BigEndianAbstractCharSequenceAccess() {
        }

        @Override
        public long getLong(CharSequence input, long offset) {
            return getLong(input, offset, 3, 2, 1, 0, 0, 1);
        }

        @Override
        public long getUnsignedInt(CharSequence input, long offset) {
            return getUnsignedInt(input, offset, 1, 0, 0, 1);
        }

        @Override
        public int getUnsignedShort(CharSequence input, long offset) {
            return getUnsignedShort(input, offset, 0, 1);
        }

        @Override
        public int getUnsignedByte(CharSequence input, long offset) {
            return getUnsignedByte(input, offset, (((int) offset & 1) ^ 1) << 3);
        }

        @Override
        public ByteOrder byteOrder(CharSequence input) {
            return BIG_ENDIAN;
        }
    }
}