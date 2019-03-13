package org.cat73.pager.aop;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cat73.pager.SpringBootTestBase;
import org.cat73.pager.bean.PageBody;
import org.cat73.pager.application.bean.Result;
import org.cat73.pager.application.test.entity.TestEntity;
import org.cat73.pager.internal.CodeMatcher;
import org.cat73.pager.util.Jsons;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @see org.cat73.pager.aop.PagerPlugin
 */
class PagerPluginTest extends SpringBootTestBase {
    private static final String TEST_URL = "/api/test/list";
    private static final String TEST2_URL = "/api/test/list2";

    /**
     * 不带参数的分页查询测试(默认第一页，10 条记录)
     */
    @Test
    void pagerTest01() {
        // 由于 Result2 和 Result 结构完全一致，因此生成的 JSON 也会完全一致
        // Result2 的测试目标主要是 PagerResults 那边的处理，因此这里就没必要多写一个测试了
        // 用普通的测试，拿 Result 直接接响应即可
        Result<PageBody<TestEntity>> result = super.getMockAndResult(TEST2_URL, new TypeReference<Result<PageBody<TestEntity>>>() {});

        Assertions.assertNotNull(result.getData());
        Assertions.assertTrue(result.getData() instanceof PageBody);
        Assertions.assertEquals(1, result.getData().getPage());
        Assertions.assertEquals(20, result.getData().getTotalRow());
        Assertions.assertEquals(2, result.getData().getTotalPage());

        Assertions.assertNotNull(result.getData().getListData());
        Assertions.assertEquals(10, result.getData().getListData().size());

        for (int i = 0, n = 1; i < 10; i++, n++) {
            Assertions.assertEquals(Integer.valueOf(n), result.getData().getListData().get(i).getId());
            Assertions.assertEquals("node-" + n, result.getData().getListData().get(i).getName());
        }
    }

    /**
     * 带参数的分页查询测试(第 2 页，8 条记录)
     */
    @Test
    void pagerTest02() {
        Result<PageBody<TestEntity>> result = super.getMockAndResult(TEST_URL, p -> p.param("page", "2").param("pageSize", "8"), new TypeReference<Result<PageBody<TestEntity>>>() {});

        Assertions.assertNotNull(result.getData());
        Assertions.assertTrue(result.getData() instanceof PageBody);
        Assertions.assertEquals(2, result.getData().getPage());
        Assertions.assertEquals(20, result.getData().getTotalRow());
        Assertions.assertEquals(3, result.getData().getTotalPage());

        Assertions.assertNotNull(result.getData().getListData());
        Assertions.assertEquals(8, result.getData().getListData().size());

        for (int i = 0, n = 9; i < 8; i++, n++) {
            Assertions.assertEquals(Integer.valueOf(n), result.getData().getListData().get(i).getId());
            Assertions.assertEquals("node-" + n, result.getData().getListData().get(i).getName());
        }
    }

    /**
     * 带参数的分页查询测试(第 3 页，7 条记录，Header 传参)
     */
    @Test
    void pagerTest03() throws Exception {
        MockHttpServletRequestBuilder builder = get(TEST_URL);

        builder.header("Page", "3");
        builder.header("PageSize", "7");

        MvcResult mvcResult = this.mock()
                .perform(
                        builder
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(jsonPath("$.code").value(new CodeMatcher()))
                .andReturn();

        Result<PageBody<TestEntity>> result =  Jsons.from(mvcResult.getResponse().getContentAsString(), new TypeReference<Result<PageBody<TestEntity>>>() {});

        // ==== 校验结果 ====

        Assertions.assertNotNull(result.getData());
        Assertions.assertTrue(result.getData() instanceof PageBody);
        Assertions.assertEquals(3, result.getData().getPage());
        Assertions.assertEquals(20, result.getData().getTotalRow());
        Assertions.assertEquals(3, result.getData().getTotalPage());

        Assertions.assertNotNull(result.getData().getListData());
        Assertions.assertEquals(6, result.getData().getListData().size());

        for (int i = 0, n = 15; i < 6; i++, n++) {
            Assertions.assertEquals(Integer.valueOf(n), result.getData().getListData().get(i).getId());
            Assertions.assertEquals("node-" + n, result.getData().getListData().get(i).getName());
        }
    }

    @Nonnull
    private MvcResult download(@Nonnull String url, @Nonnull Consumer<MockHttpServletRequestBuilder> custom) throws Exception {
        // 下载文件
        MockHttpServletRequestBuilder builder = get(url);
        custom.accept(builder);

        return this.mock()
                .perform(
                        builder.characterEncoding("utf-8")
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * 导出模式 - 全部数据
     */
    @Test
    void exportTest01() throws Exception {
        MvcResult mvcResult = this.download(TEST_URL, b -> b.param("export", "true"));

        Assertions.assertEquals("application/octet-stream", mvcResult.getResponse().getContentType());
        Assertions.assertTrue(mvcResult.getResponse().getHeader("Content-Disposition").matches("^.*?filename\\*=utf-8''test.*$"));

        try (InputStream in = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray())) {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();
            Row row = rowIterator.next();
            Assertions.assertEquals("idx", row.getCell(0).getStringCellValue());
            Assertions.assertEquals("id", row.getCell(1).getStringCellValue());
            Assertions.assertEquals("名称", row.getCell(2).getStringCellValue());

            int i = 1;
            int n = 1;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                Assertions.assertEquals((double) i, row.getCell(0).getNumericCellValue());
                Assertions.assertEquals((double) n, row.getCell(1).getNumericCellValue());
                Assertions.assertEquals("node-" + n, row.getCell(2).getStringCellValue());

                i++;
                n++;
            }

            Assertions.assertEquals(20 + 1, i);
        }
    }

    /**
     * 导出模式 - 继续分页，混合传参
     */
    @Test
    void exportTest02() throws Exception {
        MvcResult mvcResult = this.download(TEST_URL, b -> b
                .param("page", "2")
                .header("PageSize", "7")
                .header("Export", "true")
                .param("pager", "true")
        );

        Assertions.assertEquals("application/octet-stream", mvcResult.getResponse().getContentType());
        Assertions.assertTrue(mvcResult.getResponse().getHeader("Content-Disposition").matches("^.*?filename\\*=utf-8''test.*$"));

        try (InputStream in = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray())) {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();
            Row row = rowIterator.next();
            Assertions.assertEquals("idx", row.getCell(0).getStringCellValue());
            Assertions.assertEquals("id", row.getCell(1).getStringCellValue());
            Assertions.assertEquals("名称", row.getCell(2).getStringCellValue());

            int i = 1;
            int n = 8;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                Assertions.assertEquals((double) i, row.getCell(0).getNumericCellValue());
                Assertions.assertEquals((double) n, row.getCell(1).getNumericCellValue());
                Assertions.assertEquals("node-" + n, row.getCell(2).getStringCellValue());

                i++;
                n++;
            }

            Assertions.assertEquals(7 + 1, i);
        }
    }
}
