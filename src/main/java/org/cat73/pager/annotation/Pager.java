package org.cat73.pager.annotation;

import org.cat73.pager.bean.PageBody;
import org.cat73.pager.export.IPagerExport;
import org.cat73.pager.result.PagerResults;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.Map;

/**
 * 被此注解标记的 Controller 中的 RequestHandler 将自动支持分页查询
 * <p>请求的返回值为 {@link Map} 时，会尝试读取其 data 属性，如存在且值为 {@link Collection} 的子类，则会将其视为查询结果，并将其转换为 {@link PageBody}</p>
 * <p>请求的返回值为其他类型时，请使用 {@link PagerResults#registerHandler} 来支持这种类型</p>
 * <p>正常情况下(外部访问而非内部调用时，且方法上直接或间接修饰着`@ResponseBody`)，结果会序列化成`JSON`输出，例：</p>
 * <pre>{"page":1,"totalPage":5,"totalRow":48,"listData":[...]}</pre>
 * <p>当请求中附带 pageSize 参数时，则以这个参数的值当做每页的结果数</p>
 * <p>当请求中附带 page 参数时，则以这个参数的值当做当前的页码，不写默认为第一页(从 1 开始)</p>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Pager {
    /**
     * 每页结果数的默认值，默认为 10
     * <p>请求中未传 pageSize 参数时，会使用这个值作为 pageSize</p>
     * @return 每页结果数的默认值
     */
    int df() default 10;

    /**
     * 每页结果数的最小值，默认为 1
     * <p>如果 pageSize 小于这个值，则会被强制限制为这个值</p>
     * @return 每页结果数的最小值
     */
    int min() default 1;

    /**
     * 每页结果数的最大值，默认为 100
     * <p>如果 pageSize 大于这个值，则会被强制限制为这个值</p>
     * @return 每页结果数的最大值
     */
    int max() default 100;

    /**
     * 导出模式使用的导出实现类
     * <p>如果为可实例化的类(有 public 的无参构造方法)，则这个接口支持导出为 Excel 文件</p>
     * <p>请求参数中设置 export 为 true 即可禁用分页并把请求转为文件下载</p>
     * <p>导出模式默认是禁用分页的，即查出所有结果并导出</p>
     * <p>将请求参数中设置 pager 为 true 可在导出模式时继续分页</p>
     * @return 导出模式使用的导出实现
     */
    Class<? extends IPagerExport> export() default IPagerExport.class;

    /**
     * 导出的文件的文件名前缀
     * <p>默认情况下，SimplePagerExport 会使用这个参数作为导出时下载的文件名的前缀</p>
     * <p>如希望自定义导出的文件名，可通过覆盖 IPagerExport 的 getFilename 方法来实现</p>
     * <p>如使用自己实现的导出类，可自行决定是否参考这个字段</p>
     * @return 导出的文件的文件名前缀
     */
    String filenamePrefix() default "";

    /**
     * 导出的 Excel 的列标题列表
     * <p>默认情况下，SimplePagerExport 会使用这个参数作为 Excel 的第一行的内容，每个元素为一列</p>
     * <p>如保持默认值，则会视为没有列标题</p>
     * <p>如使用自己实现的导出类，可自行决定是否参考这个字段</p>
     * @return 导出的 Excel 的列标题列表
     */
    String[] exportColumns() default {};
}
