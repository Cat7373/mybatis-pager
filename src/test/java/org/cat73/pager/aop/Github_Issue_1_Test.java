package org.cat73.pager.aop;

import com.fasterxml.jackson.core.type.TypeReference;
import org.cat73.pager.SpringBootTestBase;
import org.cat73.pager.application.bean.Result;
import org.cat73.pager.application.test.entity.TestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 测试 https://github.com/Cat7373/mybatis-pager/issues/1
 */
class Github_Issue_1_Test extends SpringBootTestBase {
    @Test
    void test() {
        // 首先调用一次会产生问题的分页查询接口
        Assertions.assertThrows(RuntimeException.class, () -> this.getMock("/api/test/github/issue/1/step1"));

        // 再调用正常查询的接口
        Result<List<TestEntity>> result = this.getMockAndResult("/api/test/github/issue/1/step2", new TypeReference<Result<List<TestEntity>>>() {});

        // 校验结果
        Assertions.assertEquals(20, result.getData().size());
    }
}
