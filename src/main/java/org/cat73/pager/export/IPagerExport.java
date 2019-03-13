package org.cat73.pager.export;

import org.apache.poi.ss.usermodel.Workbook;

import javax.annotation.Nonnull;
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
     * @param prefix {@code @Pager} 注解的 filenamePrefix 参数的内容
     * @return 导出文件的文件名
     */
    @Nonnull
    String getFilename(@Nonnull String prefix);
}
