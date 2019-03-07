package org.cat73.pager.result;

import org.cat73.pager.bean.PageBody;
import org.cat73.pager.exception.PagerException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
    private static final List<IPagerResultHandler<Object>> handlers = new ArrayList<>();
    /**
     * 处理器的缓存列表
     */
    private static final Map<Class<?>, IPagerResultHandler<Object>> handlerCaches = new HashMap<>();

    /**
     * 注册一个返回值处理器，越晚注册的处理器会越优先被使用
     * @param handler 被注册的处理器
     */
    public static void registerHandler(IPagerResultHandler<?> handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }

        // 强转成 Object 泛型
        IPagerResultHandler<Object> typeSafeHandler = PagerResults.typeSafeHandler(handler);

        // 校验是否已存在
        if (PagerResults.handlers.contains(typeSafeHandler)) {
            throw new IllegalArgumentException("handler already exists.");
        }

        // 插入到头部
        PagerResults.handlers.add(0, typeSafeHandler);
    }

    /**
     * 强转成 Object 泛型
     * @param handler ? 泛型的返回值处理器
     * @return Object 泛型的返回值处理器
     */
    @SuppressWarnings("unchecked")
    private static IPagerResultHandler<Object> typeSafeHandler(IPagerResultHandler<?> handler) {
        return (IPagerResultHandler<Object>) handler;
    }

    /**
     * 判断一个返回值处理器是否支持一个返回值
     * @param handler 返回值处理器
     * @param result 返回值
     * @return 是否支持
     */
    private static boolean isSupport(IPagerResultHandler<Object> handler, Object result) {
        // 从返回值处理器中寻找 IPagerResultHandler 接口上的泛型参数
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
        // 如果泛型一致，才去调用这个处理器的 support 方法
        if (clazz.isAssignableFrom(result.getClass())) {
            try {
                // 返回这个处理器是否支持这个返回值
                return handler.support(result);
            } catch (Exception e) {
                // 如果出现任何异常，则认为不支持
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
    private static IPagerResultHandler<Object> findSupportHandler(Object result) {
        Class<?> resultClazz = result.getClass();

        // 尝试搜索缓存
        IPagerResultHandler<Object> handler = PagerResults.handlerCaches.get(resultClazz);

        // 未命中，走常规搜索
        if (handler == null) {
            handler = PagerResults.handlers.stream()
                    .filter(h -> {
                        try {
                            return PagerResults.isSupport(h, result);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .findFirst().orElse(null);
        }

        // 如果未找到则抛出异常
        if (handler == null) {
            throw new PagerException("未找到支持的 Handler");
        }

        // 如果允许缓存，则保存缓存
        if (handler.allowCache()) {
            handlerCaches.put(resultClazz, handler);
        }

        // 返回结果
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
