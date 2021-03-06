package org.cat73.pager.application.bean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * 公共返回值
 */
public class Result<T> {
    /**
     * 状态码，公共约定：
     * < 0: 错误
     * >= 0: 成功
     */
    private int code;
    /**
     * 错误信息(失败时有)
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return this.code == result.code &&
                Objects.equals(this.message, result.message) &&
                Objects.equals(this.data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, data);
    }

    @Override
    @Nonnull
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
