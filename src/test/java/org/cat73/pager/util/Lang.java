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
}
