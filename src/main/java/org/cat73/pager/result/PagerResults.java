package org.cat73.pager.result;

import org.cat73.pager.bean.PageBody;
import org.cat73.pager.exception.PagerException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 返回值处理工具类
 */
public final class PagerResults {
    private PagerResults() {
        throw new UnsupportedOperationException();
    }

    /**
     * 返回值处理器列表
     */
    private static final List<IPagerResultHandler<?>> handlers = new ArrayList<>();

    /**
     * 注册一个返回值处理器，越晚注册的处理器会越优先被使用
     * @param handler 被注册的处理器
     */
    public static void registerHandler(IPagerResultHandler<?> handler) {
        if (PagerResults.handlers.contains(handler)) {
            throw new IllegalArgumentException("handler already exists.");
        }

        PagerResults.handlers.add(0, handler);
    }

    // TODO 缓存机制？
    // TODO javadoc
    @SuppressWarnings("unchecked")
    private static boolean isSupport(IPagerResultHandler<?> handler, Object result) {
        Class<?> clazz = handler.getClass();
        out: while (true) {
            for (Type interfaceType : clazz.getGenericInterfaces()) {
                if (interfaceType instanceof ParameterizedType) {
                    ParameterizedType parameterizedInterfaceType = (ParameterizedType) interfaceType;
                    if (parameterizedInterfaceType.getRawType() == IPagerResultHandler.class) {
                        Type typeArgument = parameterizedInterfaceType.getActualTypeArguments()[0];
                        if (typeArgument instanceof ParameterizedType) {
                            ParameterizedType parameterizedTypeArgument = (ParameterizedType) typeArgument;
                            clazz = (Class<?>) parameterizedTypeArgument.getRawType();
                            break out;
                        } else {
                            clazz = (Class<?>) typeArgument;
                            break out;
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
            clazz = clazz.getSuperclass();
        }
        if (clazz.isAssignableFrom(result.getClass())) {
            try {
                return ((IPagerResultHandler<Object>) handler).support(result);
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    /**
     * 寻找支持这个返回值的处理器
     * @param result 返回值
     * @return 找到的处理器
     */
    @SuppressWarnings("unchecked")
    private static IPagerResultHandler<Object> findSupportHandler(Object result) {
        IPagerResultHandler<?> handler = PagerResults.handlers.stream()
                .filter(h -> {
                    try {
                        return PagerResults.isSupport(h, result);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst().orElse(null);
        if (handler == null) {
            throw new PagerException("未找到支持的 Handler");
        }
        return ((IPagerResultHandler<Object>) handler);
    }

    /**
     * 获取返回值中的分页数据
     * @param result 返回值
     * @return 获取到的分页数据
     */
    public static List<?> getData(Object result) {
        Collection<?> collection;
        try {
            collection = PagerResults.findSupportHandler(result)
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
            PagerResults.findSupportHandler(result)
                    .setData(result, pageBody);
        } catch (Exception e) {
            throw new PagerException(e);
        }
    }
}
