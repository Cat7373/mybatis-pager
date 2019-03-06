package org.cat73.pager.internal;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * 给 GET 请求输入参数的 Builder
 */
public class GetParamBuilder {
    private MockHttpServletRequestBuilder builder;

    public GetParamBuilder(MockHttpServletRequestBuilder builder) {
        this.builder = builder;
    }

    public GetParamBuilder param(String key, String... values) {
        this.builder.param(key, values);
        return this;
    }
}
