package org.cat73.pager.application.test.web;

import org.cat73.pager.annotation.Pager;
import org.cat73.pager.application.bean.Result;
import org.cat73.pager.application.test.entity.TestEntity;
import org.cat73.pager.application.test.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 测试 https://github.com/Cat7373/mybatis-pager/issues/1
 */
@RestController
@RequestMapping("/api/test/github/issue/1")
public class Github_Issue_1_Controller {
    @Autowired
    private TestMapper testMapper;

    @Pager(df = 2)
    @GetMapping("/step1")
    public void step1() {
        throw new RuntimeException();
    }

    @Nonnull
    @GetMapping("/step2")
    public Result<List<TestEntity>> step2() {
        // 普通查询
        List<TestEntity> list = this.testMapper.queryIdLe(1000);

        // 返回结果
        Result<List<TestEntity>> result = new Result<>();
        result.setData(list);
        return result;
    }
}
