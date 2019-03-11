package org.cat73.pager.result;

import org.cat73.pager.bean.PageBody;

import java.util.Collection;

/**
 * 返回值处理器
 * <p>实现应自行保证线程安全性</p>
 */
public interface IPagerResultHandler<T> {
    /**
     * 是否允许缓存
     * <p>允许缓存的处理器性能会好一些</p>
     * <p>判断缓存命中的条件是，上次返回值的 Class 成功命中了这个 Handler</p>
     * <p>因此如果仅简单的判断 Class 是否一致无法区分 Handler 时，请覆盖这个方法，并返回 false</p>
     * <p><em>注意，如果要设置为返回 false，请将所有 Class 一致的类均设置为 false</em></p>
     * @return 是否允许缓存
     */
    default boolean allowCache() {
        return true;
    }

    /**
     * 判断是否支持这个返回值
     * <p>默认情况下，只要泛型能接收的返回值，就默认支持</p>
     * <p>如果有更复杂的条件，请覆盖这个方法，并实现自己的判断逻辑</p>
     * @param result 返回值
     * @return 是否支持
     * @throws Exception 如果抛出任何异常，均视为不支持这个返回值
     */
    default boolean support(T result) throws Exception {
        return true;
    }

    /**
     * 从返回值中取出分页查询结果
     * @param result 返回值
     * @return 取到的结果，不允许返回 null，至少要返回空集合
     * @throws Exception 如果抛出任何异常，均视为处理失败
     */
    Collection<?> getData(T result) throws Exception;

    /**
     * 将分页结果存储进返回值
     * @param result 返回值
     * @param pageBody 分页结果
     * @throws Exception 如果抛出任何异常，均视为处理失败
     */
    void setData(T result, PageBody<?> pageBody) throws Exception;
}
