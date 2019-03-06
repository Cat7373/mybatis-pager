package org.cat73.pager.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet 工具类，只能在请求上下文中使用
 * <p><em>内部使用，不保证未来依旧存在，不建议外部使用</em></p>
 */
public final class ServletBox {
    private ServletBox() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取当前会话的 Request
     * @return 当前会话的 Request
     */
    public static HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取当前会话的 Response
     * @return 当前会话的 Response
     */
    public static HttpServletResponse response() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
