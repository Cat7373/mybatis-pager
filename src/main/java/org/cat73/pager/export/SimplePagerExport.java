package org.cat73.pager.export;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.cat73.pager.util.Times;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class SimplePagerExport<T> implements IPagerExport<T> {
    @Override
    @Nonnull
    public Workbook toWorkBook(@Nonnull List<T> records) {
        // 创建工作簿
        Workbook workbook = new HSSFWorkbook();
        // 创建表
        Sheet sheet = workbook.createSheet();

        // 当前行号
        int[] rownum = new int[] { 0 };

        // 输出列标题
        Optional.ofNullable(this.columns()).ifPresent(columns ->
                this.fillRow(sheet.createRow(rownum[0]++), columns));

        // 输出每一行
        int recordNo = 1;
        for (T record : records) {
            Optional.ofNullable(this.row(record, recordNo++)).ifPresent(columns ->
                    this.fillRow(sheet.createRow(rownum[0]++), columns));
        }

        return workbook;
    }

    /**
     * 填充一行
     * @param row 被填充的行
     * @param columns 用于填充的数据列表，每个数据代表一列
     */
    private void fillRow(@Nonnull Row row, @Nonnull List<?> columns) {
        int colnum = 0;
        for (Object column : columns) {
            this.setCellValue(column, row.createCell(colnum++));
        }
    }

    @Override
    @Nonnull
    public String getFilename(@Nonnull String prefix) {
        return String.format("%s%tF_%tT.xls", prefix, LocalDateTime.now(), LocalDateTime.now());
    }

    /**
     * 将值输出到单元格中，会遵照以下规则输出
     * <ul>
     *     <li>null -&gt; 空字符串</li>
     *     <li>Date -&gt;格式化为时间字符串(yyyy-MM-dd HH:mm:ss)</li>
     *     <li>LocalDate -&gt; 格式化为时间字符串(yyyy-MM-dd)</li>
     *     <li>LocalDateTime -&gt; 格式化为时间字符串(yyyy-MM-dd HH:mm:ss)</li>
     *     <li>Number -&gt; 会作为 double 的数字输出，可能会损失精度，如果不希望损失精度，请使用 String</li>
     *     <li>其他 -&gt; 会直接调用对象的 toString 方法转为字符串后输出</li>
     * </ul>
     *
     * @param value 值
     * @param cell 单元格
     */
    protected void setCellValue(@Nullable Object value, @Nonnull Cell cell) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Date) {
            cell.setCellValue(Times.format((Date) value));
        } else if (value instanceof LocalDate) {
            cell.setCellValue(Times.format((LocalDate) value));
        } else if (value instanceof LocalTime) {
            cell.setCellValue(Times.format((LocalTime) value));
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(Times.format((LocalDateTime) value));
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    /**
     * 获取列标题列表，列表中每个元素代表一列
     * <p>如返回 null，则输出的表中没有列标题</p>
     *
     * @return 列标题列表
     */
    @Nullable
    protected List<?> columns() {
        return null;
    }

    /**
     * 给入一个对象转换为一行记录，列表中每个元素代表一列<br>
     * <p>如返回 null，则忽略该对象，不输出到表格中</p>
     *
     * @param obj 被转换的对象，如果原始数据中会存在 null，则这里也会将 null 如实的传递进去
     * @param idx 当前是第几条记录(从 1 开始)
     * @return 值列表
     */
    @Nullable
    protected abstract List<?> row(@Nullable T obj, int idx);
}
