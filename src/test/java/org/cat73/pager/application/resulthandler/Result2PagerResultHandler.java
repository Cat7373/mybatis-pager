package org.cat73.pager.application.resulthandler;

import org.cat73.pager.application.bean.Result2;
import org.cat73.pager.bean.PageBody;
import org.cat73.pager.result.IPagerResultHandler;

import java.util.Collection;

/**
 * Result2 的分页返回值处理器
 */
public class Result2PagerResultHandler implements IPagerResultHandler<Result2<Object>> {
    @Override
    public Collection<?> getData(Result2<Object> result) {
        return (Collection<?>) result.getData();
    }

    @Override
    public void setData(Result2<Object> result, PageBody<?> pageBody) {
        result.setData(pageBody);
    }
}
