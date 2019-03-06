package org.cat73.pager;

import com.fasterxml.jackson.core.type.TypeReference;
import org.cat73.pager.application.PagerTestApplication;
import org.cat73.pager.internal.CodeMatcher;
import org.cat73.pager.internal.GetParamBuilder;
import org.cat73.pager.util.Jsons;
import org.cat73.pager.util.Lang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PagerTestApplication.class)
@Transactional
public abstract class SpringBootTestBase {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private CodeMatcher codeMatcher = new CodeMatcher();

    /**
     * 初始化 Mock 对象
     */
    @BeforeEach
    public void initMock(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .alwaysDo(print())
                .build();
    }

    /**
     * 获取原始的 Mock 对象，当糖方法无法满足测试需求时可以直接调用这个
     */
    @NonNull
    protected MockMvc mock() {
        return this.mockMvc;
    }

    /* 请求糖 */

    /**
     * 执行一次 GET 的 Mock 测试<br>
     * 会按照通用接口规范来请求和做基本的返回值检查(200 状态、success 为 true、code >= 0)
     * @param url 调用的 URL
     * @param paramsFunction 提供请求参数的接口
     * @return 接口调用的返回值
     */
    @NonNull
    protected MvcResult getMock(@NonNull String url, @NonNull Function<GetParamBuilder, GetParamBuilder> paramsFunction) {
        try {
            MockHttpServletRequestBuilder builder = get(url);
            paramsFunction.apply(new GetParamBuilder(builder));

            return this.mock()
                    .perform(
                            builder
                                    .characterEncoding("utf-8")
                                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(jsonPath("$.code").value(codeMatcher))
                    .andReturn();
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 执行一次 GET 的 Mock 测试<br>
     * 会按照通用接口规范来请求和做基本的返回值检查(200 状态、success 为 true、code >= 0)
     * @param url 调用的 URL
     * @return 接口调用的返回值
     */
    @NonNull
    protected MvcResult getMock(@NonNull String url) {
        return this.getMock(url, b -> b);
    }

    /* 原始的返回值处理糖 */

    /**
     * 将测试的返回值的 content(JSON) 反序列化的方法
     * @param result 测试的返回值
     * @param clazz 要转换为的类型
     * @return 转换的结果
     */
    protected <T> T mockResult(@NonNull MvcResult result, @NonNull Class<? extends T> clazz) {
        try {
            return Jsons.from(result.getResponse().getContentAsString(), clazz);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 将测试的返回值的 content(JSON) 反序列化的方法
     * @param result 测试的返回值
     * @param type 要转换为的类型
     * @return 转换的结果
     */
    protected <T> T mockResult(@NonNull MvcResult result, @NonNull TypeReference<? extends T> type) {
        try {
            return Jsons.from(result.getResponse().getContentAsString(), type);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /* 请求并处理返回值的糖 */

    /**
     * getMock -> mockResult
     */
    protected <T> T getMockAndResult(@NonNull String url, @NonNull Function<GetParamBuilder, GetParamBuilder> paramsFunction, @NonNull Class<? extends T> clazz) {
        try {
            return Jsons.from(this.getMock(url, paramsFunction).getResponse().getContentAsString(), clazz);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * getMock -> mockResult
     */
    protected <T> T getMockAndResult(@NonNull String url, @NonNull Function<GetParamBuilder, GetParamBuilder> paramsFunction, @NonNull TypeReference<? extends T> type) {
        try {
            return Jsons.from(this.getMock(url, paramsFunction).getResponse().getContentAsString(), type);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * getMock -> mockResult
     */
    protected <T> T getMockAndResult(@NonNull String url, @NonNull Class<? extends T> clazz) {
        try {
            return Jsons.from(this.getMock(url).getResponse().getContentAsString(), clazz);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * getMock -> mockResult
     */
    protected <T> T getMockAndResult(@NonNull String url, @NonNull TypeReference<? extends T> type) {
        try {
            return Jsons.from(this.getMock(url).getResponse().getContentAsString(), type);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }
}
