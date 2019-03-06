package org.cat73.pager.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * 分页模块的配置.
 */
@ConfigurationProperties("pager")
public class PagerConfigure {
    /**
     * 分页参数的前缀.
     */
    private String prefix = "";

    /**
     * 获取分页参数的前缀
     * @return 分页参数的前缀
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 设置分页参数的前缀
     * @param prefix 新的分页参数的前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PagerConfigure that = (PagerConfigure) o;
        return Objects.equals(this.prefix, that.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix);
    }

    @Override
    public String toString() {
        return "PagerConfigure{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
