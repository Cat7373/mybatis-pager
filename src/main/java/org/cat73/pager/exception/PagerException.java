package org.cat73.pager.exception;

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
    public PagerException(String message) {
        super(message);
    }

    /**
     * 实例化一个分页过程中出现的异常
     * @param cause 子异常
     */
    public PagerException(Throwable cause) {
        super(cause);
    }
}
