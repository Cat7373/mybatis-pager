package org.cat73.pager.exception;

import javax.annotation.Nonnull;

/**
 * 分页过程中出现的异常
 */
public class PagerException extends RuntimeException {
    /**
     * 实例化一个分页过程中出现的异常
     */
    public PagerException() {}

    /**
     * 实例化一个分页过程中出现的异常
     * @param message 错误信息
     */
    public PagerException(@Nonnull String message) {
        super(message);
    }

    /**
     * 实例化一个分页过程中出现的异常
     * @param cause 子异常
     */
    public PagerException(@Nonnull Throwable cause) {
        super(cause);
    }
}
