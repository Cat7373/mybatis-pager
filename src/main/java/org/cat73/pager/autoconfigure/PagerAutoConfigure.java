package org.cat73.pager.autoconfigure;

import org.cat73.pager.result.IPagerResultHandler;
import org.cat73.pager.result.MapResultHandler;
import org.cat73.pager.result.PagerResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * 分页插件的自动配置类
 */
@EnableConfigurationProperties(PagerConfigure.class)
@ComponentScan("org.cat73.pager")
public class PagerAutoConfigure {
    /**
     * 自动初始化返回值处理器
     * @param handlerList 返回值处理器的列表
     */
    @Autowired
    public void initialPagerResultHandlers(List<IPagerResultHandler<?>> handlerList) {
        // 内置的做第一个
        PagerResults.registerHandler(new MapResultHandler());
        // 扫描到的排序就不保证了，如有需要用户应手动调用注册方法，保证自己的在最前面
        handlerList.forEach(PagerResults::registerHandler);
    }
}
