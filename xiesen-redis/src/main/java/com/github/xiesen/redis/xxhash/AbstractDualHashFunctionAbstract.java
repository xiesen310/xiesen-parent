package com.github.xiesen.redis.xxhash;


import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * @author xiese
 * @Description An internal helper class for casting LongTupleHashFunction as LongHashFunction
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:23
 */
abstract class AbstractDualHashFunctionAbstract extends AbstractLongTupleHashFunction {
    private static final long serialVersionUID = 0L;

    private transient final int resultLength = newResultArray().length;

    private void checkResult(final long[] result) {
        if (null == result) {
            throw new NullPointerException();
        }
        if (result.length < resultLength) {
            throw new IllegalArgumentException("The input result array has not enough space!");
        }
    }

    /**
     * dualHashLong
     *
     * @param input
     * @param result
     * @return
     */
    protected abstract long dualHashLong(long input, @Nullable long[] result);

    @Override
    public void hashLong(final long input, final long[] result) {
        checkResult(result);
        dualHashLong(input, result);
    }

    /**
     * dualHashInt
     *
     * @param input
     * @param result
     * @return
     */
    protected abstract long dualHashInt(int input, @Nullable long[] result);

    @Override
    public void hashInt(final int input, final long[] result) {
        checkResult(result);
        dualHashInt(input, result);
    }

    /**
     * dualHashShort
     *
     * @param input
     * @param result
     * @return
     */
    protected abstract long dualHashShort(short input, @Nullable long[] result);

    @Override
    public void hashShort(final short input, final long[] result) {
        checkResult(result);
        dualHashShort(input, result);
    }

    /**
     * dualHashChar
     *
     * @param input
     * @param result
     * @return
     */
    protected abstract long dualHashChar(char input, @Nullable long[] result);

    @Override
    public void hashChar(final char input, final long[] result) {
        checkResult(result);
        dualHashChar(input, result);
    }

    /**
     * dualHashByte
     *
     * @param input
     * @param result
     * @return
     */
    protected abstract long dualHashByte(byte input, @Nullable long[] result);

    @Override
    public void hashByte(final byte input, final long[] result) {
        checkResult(result);
        dualHashByte(input, result);
    }

    /**
     * dualHashVoid
     *
     * @param result
     * @return
     */
    protected abstract long dualHashVoid(@Nullable long[] result);

    @Override
    public void hashVoid(final long[] result) {
        checkResult(result);
        dualHashVoid(result);
    }

    /**
     * dualHash
     *
     * @param input
     * @param access
     * @param off
     * @param len
     * @param result
     * @param <T>
     * @return
     */
    protected abstract <T> long dualHash(@Nullable T input, AbstractAccess<T> access, long off, long len, @Nullable long[] result);

    @Override
    public <T> void hash(@Nullable final T input, final AbstractAccess<T> access, final long off, final long len, final long[] result) {
        checkResult(result);
        dualHash(input, access, off, len, result);
    }

    @Override
    public <T> long[] hash(@Nullable final T input, final AbstractAccess<T> access, final long off, final long len) {
        final long[] result = newResultArray();
        dualHash(input, access, off, len, result);
        return result;
    }

    /**
     * longHashFunction
     */
    @NotNull
    private transient final AbstractLongHashFunction abstractLongHashFunction = new AbstractLongHashFunction() {
        @Override
        public long hashLong(final long input) {
            return dualHashLong(input, null);
        }

        @Override
        public long hashInt(final int input) {
            return dualHashInt(input, null);
        }

        @Override
        public long hashShort(final short input) {
            return dualHashShort(input, null);
        }

        @Override
        public long hashChar(final char input) {
            return dualHashChar(input, null);
        }

        @Override
        public long hashByte(final byte input) {
            return dualHashByte(input, null);
        }

        @Override
        public long hashVoid() {
            return dualHashVoid(null);
        }

        @Override
        public <T> long hash(@Nullable final T input, final AbstractAccess<T> access, final long off, final long len) {
            return dualHash(input, access, off, len, null);
        }
    };

    @NotNull
    protected AbstractLongHashFunction asLongHashFunction() {
        return abstractLongHashFunction;
    }
}
