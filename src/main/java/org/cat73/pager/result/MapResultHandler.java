package org.cat73.pager.result;

import org.cat73.pager.application.bean.PageBody;

import java.util.Collection;
import java.util.Map;

/**
 * 内置的支持 Map 类型的返回值的处理器
 * <p>要求这个 Map 中存在 key 为 data 的值，且值为 {@link Collection} 的子类</p>
 */
public class MapResultHandler implements IPagerResultHandler {
    @Override
    public boolean support(Object result) {
        if (result instanceof Map) {
            Object data = ((Map) result).get("data");
            return data instanceof Collection;
        }

        return false;
    }

    @Override
    public Collection<?> getData(Object result) {
        return ((Collection<?>) ((Map) result).get("data"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setData(Object result, PageBody<?> pageBody) {
        ((Map) result).put("data", pageBody);
    }
}
