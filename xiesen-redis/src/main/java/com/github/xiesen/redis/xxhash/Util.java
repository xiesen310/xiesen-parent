package com.github.xiesen.redis.xxhash;

import com.sun.istack.internal.NotNull;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.ByteOrder.nativeOrder;

/**
 * @author xiese
 * @Description xxhash算法工具类
 * @Email xiesen310@163.com
 * @Date 2020/8/30 14:08
 */
final class Util {
    static final boolean NATIVE_LITTLE_ENDIAN = nativeOrder() == LITTLE_ENDIAN;
    static final String JDK_VERSION_17006 = "1.7.0_06";
    static final String JDK_VERSION_19 = "1.9";

    /**
     * Known java.vm.name list:
     * <p>
     * HotSpot:
     * - Java HotSpot(TM) xx-Bit Server VM
     * - OpenJDK xx-Bit Server VM
     * <p>
     * J9:
     * - Eclipse OpenJ9 VM
     * - IBM J9 VM
     *
     * @param name
     * @return
     */
    static private boolean isHotSpotVm(@NotNull final String name) {
        return name.contains("HotSpot") || name.contains("OpenJDK");
    }

    /**
     * isJ9VM
     *
     * @param name
     * @return
     */
    static private boolean isj9Vm(@NotNull final String name) {
        return name.contains("Eclipse OpenJ9") || name.contains("IBM J9");
    }

    @NotNull
    static final StringHash VALID_STRING_HASH;

    static {
        StringHash stringHash = null;
        try {
            final String vmName = System.getProperty("java.vm.name");
            if (isHotSpotVm(vmName) || isj9Vm(vmName)) {
                final String javaVersion = System.getProperty("java.version");
                if (javaVersion.compareTo(JDK_VERSION_17006) >= 0) {
                    if (javaVersion.compareTo(JDK_VERSION_19) >= 0) {
                        stringHash = ModernCompactStringHash.INSTANCE;
                    } else {
                        stringHash = ModernHotSpotStringHash.INSTANCE;
                    }
                } else {
                    stringHash = HotSpotPrior7u6StringHash.INSTANCE;
                }
            } else {
                stringHash = HotSpotPrior7u6StringHash.INSTANCE;
            }
        } catch (final Throwable ignore) {
        } finally {
            if (null == stringHash) {
                VALID_STRING_HASH = UnknownJvmStringHash.INSTANCE;
            } else {
                VALID_STRING_HASH = stringHash;
            }
        }
    }

    static void checkArrayOffs(final int arrayLength, final int off, final int len) {
        if (len < 0 || off < 0 || off + len > arrayLength || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    static long getDirectBufferAddress(@NotNull final ByteBuffer buff) {
        return ((DirectBuffer) buff).address();
    }
}
