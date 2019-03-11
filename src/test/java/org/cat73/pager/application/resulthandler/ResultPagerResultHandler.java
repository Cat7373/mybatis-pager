package org.cat73.pager.application.resulthandler;

import org.cat73.pager.application.bean.Result;
import org.cat73.pager.bean.PageBody;
import org.cat73.pager.result.IPagerResultHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Result 的分页返回值处理器
 */
@Component
public class ResultPagerResultHandler implements IPagerResultHandler<Result<Object>> {
    @Override
    public Collection<?> getData(Result<Object> result) {
        return (Collection<?>) result.getData();
    }

    @Override
    public void setData(Result<Object> result, PageBody<?> pageBody) {
        result.setData(pageBody);
    }
}
