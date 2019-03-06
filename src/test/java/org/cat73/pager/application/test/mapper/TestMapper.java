package org.cat73.pager.application.test.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cat73.pager.application.test.entity.TestEntity;

import java.util.List;

public interface TestMapper {
    @Select("SELECT * FROM pager_test WHERE id <= #{val}")
    List<TestEntity> queryIdLe(@Param("val") int val);

    @Select("SELECT * FROM pager_test WHERE id > #{val}")
    List<TestEntity> queryIdGt(@Param("val") int val);
}
