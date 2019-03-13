package org.cat73.pager.result;

import org.cat73.pager.bean.PageBody;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * 内置的支持 Map 类型的返回值的处理器
 * <p>要求这个 Map 中存在 key 为 data 的值，且值为 {@link Collection} 的子类</p>
 */
public final class MapResultHandler implements IPagerResultHandler<Map<String, Object>> {
    @Override
    public boolean support(@Nonnull Map<String, Object> result) {
        Object data = result.get("data");
        return data instanceof Collection;
    }

    @Override
    @Nullable
    public Collection<?> getData(@Nonnull Map<String, Object> result) {
        return ((Collection<?>) result.get("data"));
    }

    @Override
    public void setData(@Nonnull Map<String, Object> result, @Nonnull PageBody<?> pageBody) {
        result.put("data", pageBody);
    }
}
