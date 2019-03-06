package org.cat73.pager.result;

import org.cat73.pager.bean.PageBody;

import java.util.Collection;

/**
 * 返回值处理器
 * <p>实现应自行保证线程安全性</p>
 */
public interface IPagerResultHandler {
    /**
     * 判断是否支持这个返回值
     * @param result 返回值
     * @return 是否支持
     * @throws Exception 如果抛出任何异常，均视为不支持这个返回值
     */
    boolean support(Object result) throws Exception;

    /**
     * 从返回值中取出分页查询结果
     * @param result 返回值
     * @return 取到的结果，不允许返回 null，至少要返回空集合
     * @throws Exception 如果抛出任何异常，均视为处理失败
     */
    Collection<?> getData(Object result) throws Exception;

    /**
     * 将分页结果存储进返回值
     * @param result 返回值
     * @param pageBody 分页结果
     * @throws Exception 如果抛出任何异常，均视为处理失败
     */
    void setData(Object result, PageBody<?> pageBody) throws Exception;
}
