package org.cat73.pager.autoconfigure;

import org.cat73.pager.aop.PagerPlugin;
import org.cat73.pager.result.IPagerResultHandler;
import org.cat73.pager.result.MapResultHandler;
import org.cat73.pager.result.PagerResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * 分页插件的自动配置类
 */
@EnableConfigurationProperties(PagerConfigure.class)
public class PagerAutoConfigure {
    /**
     * 将 PagerPlugin 注册为 Spring Bean
     * @param pagerConfigure 分页配置
     * @return PagerPlugin 的实例
     */
    @Bean
    @Nonnull
    public PagerPlugin pagerPlugin(@Nonnull PagerConfigure pagerConfigure) {
        return new PagerPlugin(pagerConfigure);
    }

    /**
     * 自动初始化返回值处理器
     * @param handlerList 返回值处理器的列表
     */
    @Autowired(required = false)
    public void initialPagerResultHandlers(@Nullable List<IPagerResultHandler<?>> handlerList) {
        // 内置的做第一个
        PagerResults.registerHandler(new MapResultHandler());
        // 扫描到的排序就不保证了，如有需要用户应手动调用注册方法，保证优先顺序
        Optional.ofNullable(handlerList).ifPresent(l -> l.forEach(PagerResults::registerHandler));
    }
}
