package org.cat73.pager.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json 工具类
 */
public final class Jsons {
    private Jsons() {
        throw new UnsupportedOperationException();
    }

    /**
     * 内置的 ObjectMapper
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取内置的 ObjectMapper，请一定不要对任何参数进行修改，如果需要自定义请重新 new 一个出来
     * @return 内置的 ObjectMapper
     */
    public static ObjectMapper mapper() {
        return mapper;
    }

    /**
     * 将 JSON 字符串转换为 Java 对象
     * @param json 被转化的 JSON 字符串
     * @param clazz 要转换为的类型
     * @param <T> 返回值的数据类型
     * @return 转换结果
     */
    public static <T> T from(String json, Class<? extends T> clazz) {
        return Lang.wrapCode(() -> mapper.readValue(json, clazz));
    }

    /**
     * 将 JSON 字符串转换为 Java 对象
     * @param json 被转化的 JSON 字符串
     * @param type 要转换为的类型
     * @param <T> 返回值的数据类型
     * @return 转换结果
     */
    public static <T> T from(String json, TypeReference<? extends T> type) {
        return Lang.wrapCode(() -> mapper.readValue(json, type));
    }

    /**
     * 将 Java 对象转换为 JSON 字符串
     * @param obj 被转换的对象
     * @param <T> 被转换的对象的数据类型
     * @return 转换结果
     */
    public static <T> String to(T obj) {
        return Lang.wrapCode(() -> mapper.writeValueAsString(obj));
    }
}
