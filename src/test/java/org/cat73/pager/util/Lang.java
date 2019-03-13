package org.cat73.pager.util;

import java.util.function.Supplier;

/**
 * 一些异常相关的工具类
 * @deprecated 考虑移除
 */
@Deprecated
public final class Lang {
    private Lang() {
        throw new UnsupportedOperationException();
    }

    /**
     * 将参数包装为 RuntimeException 后返回，如果它已经是一个 RuntimeException 则直接返回
     * @param e 被包装的异常
     * @return 包装后的异常
     */
    public static RuntimeException wrapThrow(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    /**
     * 根据格式化字符串生成 RuntimeException
     * @param format 格式化字符串
     * @param args 格式化时的参数
     * @return 生成的异常
     */
    public static RuntimeException makeThrow(String format, Object... args) {
        return new RuntimeException(String.format(format, args));
    }

    /**
     * 执行一段可能抛出异常代码，自动用 warpThrow 包装出现的异常
     * @param s 被包装的会抛出异常的代码
     * @param <T> 返回值参数的数据类型
     * @return 返回的数据
     */
    public static <T> T wrapCode(ThrowableSupplier<T> s) {
        try {
            return s.get();
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 执行一段可能抛出异常代码，自动用 warpThrow 包装出现的异常
     * @param r 被包装的会抛出异常的代码
     */
    public static void wrapCode(ThrowableRunnable r) {
        try {
            r.run();
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 允许抛出异常的 Supplier
     * @param <T> 返回值的数据类型
     */
    @FunctionalInterface
    public interface ThrowableSupplier<T> {
        T get() throws Exception;

        /**
         * 包装成普通的 Supplier
         * @return 包装后的 Supplier
         */
        default Supplier<T> wrap() {
            return () -> Lang.wrapCode(this);
        }
    }

    /**
     * 允许抛出异常的 Runnable
     */
    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Exception;

        /**
         * 包装成普通的 Runnable
         * @return 包装后的 Runnable
         */
        default Runnable wrap() {
            return () -> Lang.wrapCode(this);
        }
    }
}
