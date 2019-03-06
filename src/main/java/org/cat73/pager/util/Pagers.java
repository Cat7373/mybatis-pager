package org.cat73.pager.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import org.springframework.lang.NonNull;

import java.util.function.Supplier;

/**
 * 分页工具类
 */
public class Pagers {
    protected Pagers() {
        throw new UnsupportedOperationException();
    }

    /**
     * 跳过分页的查询包装
     * @param query 执行查询的方法体
     * @param <R> query 的返回值的数据类型
     * @return query 的返回值
     */
    public static <R> R skipPager(@NonNull Supplier<R> query) {
        // 获取当前的分页设置并清除当前的分页设置
        Page<?> page = PageHelper.getLocalPage();
        PageHelper.clearPage();

        // 执行查询
        R result = query.get();

        // 重新设置分页
        if (page != null) {
            InternalPageHelper.internalSetLocalPage(page);
        }

        // 返回结果
        return result;
    }

    /**
     * 用于绕过 PageHelper 仅限子类调用限制的子类
     */
    private static class InternalPageHelper extends PageMethod {
        /**
         * 设置分页
         * @param page 分页设置
         */
        private static void internalSetLocalPage(Page<?> page) {
            PageMethod.setLocalPage(page);
        }
    }
}
