package org.cat73.pager.application.test.web;

import org.cat73.pager.annotation.Pager;
import org.cat73.pager.application.bean.Result;
import org.cat73.pager.application.bean.Result2;
import org.cat73.pager.application.test.entity.TestEntity;
import org.cat73.pager.application.test.mapper.TestMapper;
import org.cat73.pager.export.SimplePagerExport;
import org.cat73.pager.util.Pagers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TestMapper testMapper;

    @Nullable
    @Pager(export = TestExport.class, filenamePrefix = "test_", exportColumns = { "idx", "id", "名称" })
    @GetMapping("/list")
    public Result<List<TestEntity>> list() {
        // 前面插一个查询
        List<TestEntity> list1 = Pagers.skipPager(() -> this.testMapper.queryIdLe(5));
        if (list1.size() != 5) {
            throw new AssertionError();
        }

        // 分页的查询
        Result<List<TestEntity>> result = new Result<>();
        result.setData(this.testMapper.queryIdLe(1000));

        // 后面插一个查询
        List<TestEntity> list2 = Pagers.skipPager(() -> this.testMapper.queryIdGt(15));
        if (list2.size() != 5) {
            throw new AssertionError();
        }

        // 返回结果
        return result;
    }

    @Nonnull
    @Pager
    @GetMapping("/list2")
    public Result2<List<TestEntity>> list2() {
        // 分页的查询
        Result2<List<TestEntity>> result = new Result2<>();
        result.setData(this.testMapper.queryIdLe(1000));

        // 返回结果
        return result;
    }

    public static class TestExport extends SimplePagerExport<TestEntity> {
        @Override
        public List<?> row(TestEntity obj, int idx) {
            return Arrays.asList(idx, obj.getId(), obj.getName());
        }
    }
}
