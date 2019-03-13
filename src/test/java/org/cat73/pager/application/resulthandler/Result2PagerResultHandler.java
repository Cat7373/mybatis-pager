package org.cat73.pager.application.resulthandler;

import org.cat73.pager.application.bean.Result2;
import org.cat73.pager.bean.PageBody;
import org.cat73.pager.result.IPagerResultHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Result2 的分页返回值处理器
 */
public class Result2PagerResultHandler implements IPagerResultHandler<Result2<Object>> {
    @Override
    @Nullable
    public Collection<?> getData(@Nonnull Result2<Object> result) {
        return (Collection<?>) result.getData();
    }

    @Override
    public void setData(@Nonnull Result2<Object> result, @Nonnull PageBody<?> pageBody) {
        result.setData(pageBody);
    }
}
