package org.cat73.pager.result;

import org.cat73.pager.bean.PageBody;
import org.cat73.pager.exception.PagerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 返回值处理工具类
 */
public final class Results {
    private Results() {
        throw new UnsupportedOperationException();
    }

    // TODO 缓存机制？
    /**
     * 返回值处理器列表
     */
    private static Collection<IPagerResultHandler> handlers = new ArrayList<>();
    /**
     * 内置的支持 Map 类型的返回值的处理器
     * <p>要求这个 Map 中存在 key 为 data 的值，且值为 {@link Collection} 的子类</p>
     */
    private static IPagerResultHandler mapHandler = new MapResultHandler();

    /**
     * 注册一个返回值处理器
     * @param handler 被注册的处理器
     */
    public static void registerHandler(IPagerResultHandler handler) {
        if (Results.handlers.contains(handler)) {
            throw new IllegalArgumentException("handler already exists.");
        }

        Results.handlers.add(handler);
    }

    /**
     * 寻找支持这个返回值的处理器
     * @param result 返回值
     * @return 找到的处理器
     */
    private static IPagerResultHandler findSupportHandler(Object result) {
        IPagerResultHandler handler = Results.handlers.stream()
                .filter(h -> {
                    try {
                        return h.support(result);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst().orElse(null);
        if (handler == null) {
            try {
                if (Results.mapHandler.support(result)) {
                    handler = Results.mapHandler;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        if (handler == null) {
            throw new PagerException("未找到支持的 Handler");
        }
        return handler;
    }

    /**
     * 获取返回值中的分页数据
     * @param result 返回值
     * @return 获取到的分页数据
     */
    public static List<?> getData(Object result) {
        Collection<?> collection;
        try {
            collection = Results.findSupportHandler(result)
                    .getData(result);
        } catch (Exception e) {
            throw new PagerException(e);
        }

        if (collection instanceof List) {
            return (List<?>) collection;
        } else {
            return new ArrayList<>(collection);
        }
    }

    /**
     * 设置返回值中的分页结果
     * @param result 返回值
     * @param pageBody 分页结果
     */
    public static void setData(Object result, PageBody<?> pageBody) {
        try {
            Results.findSupportHandler(result)
                    .setData(result, pageBody);
        } catch (Exception e) {
            throw new PagerException(e);
        }
    }
}
