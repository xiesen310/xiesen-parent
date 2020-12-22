package com.github.xiesen.algorithm.xxhash;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author xiese
 * @Description ByteBufferAccess
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:20
 */
final class ByteBufferAbstractAccess extends AbstractAccess<ByteBuffer> {
    public static final ByteBufferAbstractAccess INSTANCE = new ByteBufferAbstractAccess();

    private ByteBufferAbstractAccess() {}

    @Override
    public long getLong(ByteBuffer input, long offset) {
        return input.getLong((int) offset);
    }

    @Override
    public long getUnsignedInt(ByteBuffer input, long offset) {
        return Primitives.unsignedInt(getInt(input, offset));
    }

    @Override
    public int getInt(ByteBuffer input, long offset) {
        return input.getInt((int) offset);
    }

    @Override
    public int getUnsignedShort(ByteBuffer input, long offset) {
        return Primitives.unsignedShort(getShort(input, offset));
    }

    @Override
    public int getShort(ByteBuffer input, long offset) {
        return input.getShort((int) offset);
    }

    @Override
    public int getUnsignedByte(ByteBuffer input, long offset) {
        return Primitives.unsignedByte(getByte(input, offset));
    }

    @Override
    public int getByte(ByteBuffer input, long offset) {
        return input.get((int) offset);
    }

    @Override
    public ByteOrder byteOrder(ByteBuffer input) {
        return input.order();
    }
}

