package org.cat73.pager.export;

import org.apache.poi.ss.usermodel.Workbook;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 导出模式格式化接口
 * @param <T> 被导出的数据的数据类型
 */
public interface IPagerExport<T> {
    /**
     * 将记录列表格式化成 Excel 工作簿
     * @param records 记录列表
     * @return 格式化后的工作簿
     */
    @Nonnull
    Workbook toWorkBook(@Nonnull List<T> records);

    /**
     * 获取导出文件的文件名(多数浏览器会不经用户确认，直接用这个名字作为文件名开始下载(如 Chrome)，或作为下载时文件名一栏默认填写的内容(如 IE))
     * <p>默认情况下会使用 前缀 + 当前时间 作为文件名，如需自定义文件名，可覆盖本方法</p>
     * @param prefix {@code @Pager} 注解的 filenamePrefix 参数的内容
     * @return 导出文件的文件名
     */
    @Nonnull
    default String getFilename(@Nonnull String prefix) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s%tF_%tT.xls", prefix, now, now);
    }

    // TODO 为保证向前兼容性而增加的方法
    //  下个大版本(2.x)去掉这个方法
    /**
     * 设置列标题列表(<code>@Pager</code> 注解上的 exportColumns 字段)
     * <p>如果实现需要使用这个字段，可覆盖本方法</p>
     * @param columns <code>@Pager</code> 注解上的 exportColumns 字段
     */
    default void setColumns(@Nonnull String[] columns) {}
}
