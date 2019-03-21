package org.cat73.pager.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Json 工具类
 * @deprecated 考虑移除
 */
@Deprecated
public final class Jsons {
    private Jsons() {
        throw new UnsupportedOperationException();
    }

    /**
     * 内置的 ObjectMapper
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 将 JSON 字符串转换为 Java 对象
     * @param json 被转化的 JSON 字符串
     * @param clazz 要转换为的类型
     * @param <T> 返回值的数据类型
     * @return 转换结果
     */
    public static <T> T from(String json, Class<? extends T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 将 JSON 字符串转换为 Java 对象
     * @param json 被转化的 JSON 字符串
     * @param type 要转换为的类型
     * @param <T> 返回值的数据类型
     * @return 转换结果
     */
    public static <T> T from(String json, TypeReference<? extends T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }
}
