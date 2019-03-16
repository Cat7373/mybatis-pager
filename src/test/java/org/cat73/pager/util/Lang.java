package org.cat73.pager.util;

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
     * 允许抛出异常的 Supplier
     * @param <T> 返回值的数据类型
     */
    @FunctionalInterface
    public interface ThrowableSupplier<T> {
        T get() throws Exception;
    }
}
