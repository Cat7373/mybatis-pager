package org.cat73.pager.internal;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.annotation.Nonnull;

/**
 * 给 GET 请求输入参数的 Builder
 */
public class GetParamBuilder {
    private MockHttpServletRequestBuilder builder;

    public GetParamBuilder(@Nonnull MockHttpServletRequestBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    public GetParamBuilder param(@Nonnull String key, @Nonnull String... values) {
        this.builder.param(key, values);
        return this;
    }
}
