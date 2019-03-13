package org.cat73.pager.application.resulthandler;

import org.cat73.pager.application.bean.Result;
import org.cat73.pager.bean.PageBody;
import org.cat73.pager.result.IPagerResultHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Result 的分页返回值处理器
 */
@Component
public class ResultPagerResultHandler implements IPagerResultHandler<Result<Object>> {
    @Override
    @Nullable
    public Collection<?> getData(@Nonnull Result<Object> result) {
        return (Collection<?>) result.getData();
    }

    @Override
    public void setData(@Nonnull Result<Object> result, @Nonnull PageBody<?> pageBody) {
        result.setData(pageBody);
    }
}
