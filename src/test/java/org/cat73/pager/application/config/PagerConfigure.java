package org.cat73.pager.application.config;

import org.cat73.pager.bean.PageBody;
import org.cat73.pager.application.bean.Result;
import org.cat73.pager.result.IPagerResultHandler;
import org.cat73.pager.result.PagerResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * 分页插件的配置
 */
@Configuration
public class PagerConfigure {
    @Autowired
    public void resultHandler() {
        PagerResults.registerHandler(new IPagerResultHandler() {
            @Override
            public boolean support(Object result) {
                return result.getClass() == Result.class && ((Result) result).getData() instanceof Collection;
            }

            @Override
            public Collection<?> getData(Object result) {
                return ((Collection<?>) ((Result) result).getData());
            }

            @SuppressWarnings("unchecked")
            @Override
            public void setData(Object result, PageBody<?> pageBody) {
                ((Result) result).setData(pageBody);
            }
        });
    }
}
