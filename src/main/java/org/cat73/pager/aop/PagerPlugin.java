package org.cat73.pager.aop;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.cat73.pager.annotation.Pager;
import org.cat73.pager.autoconfigure.PagerConfigure;
import org.cat73.pager.bean.PageBody;
import org.cat73.pager.exception.PagerException;
import org.cat73.pager.export.IPagerExport;
import org.cat73.pager.result.PagerResults;
import org.cat73.pager.util.ServletBox;
import org.cat73.pager.util.Strings;
import org.springframework.http.MediaType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 分页插件
 */
@Aspect
public class PagerPlugin {
    /**
     * 分页配置
     */
    private final PagerConfigure pagerConfigure;

    /**
     * 实例化一个分页插件
     * @param pagerConfigure 分页配置
     */
    public PagerPlugin(@Nonnull PagerConfigure pagerConfigure) {
        this.pagerConfigure = pagerConfigure;
    }

    // TODO 限制必须是 RequestHandler
    /**
     * 环绕所有带 @Pager 注解的 RequestHandler，对其提供分页支持
     * @param joinPoint AOP 切入点
     * @param pager 分页注解
     * @return 实际的 Handler 的返回值
     * @throws Throwable 如果 AOP 切入点抛出了异常
     */
    @Nullable
    @Around("@annotation(pager)")
    public Object aroundHandler(@Nonnull ProceedingJoinPoint joinPoint, @Nonnull Pager pager) throws Throwable {
        try {
            boolean export = this.prePager(pager);

            // 分页对象
            Page<?> page = PageHelper.getLocalPage();

            // 调用目标方法
            Object resultObject = joinPoint.proceed();

            // 清理分页
            PageHelper.clearPage();

            // 根据是否为导出模式选择行为
            if (export) {
                // 结果中的数据(如无返回值或获取到 null 的数据，则用空列表代替)
                List<?> data = Optional.ofNullable(resultObject)
                        .map(PagerResults::getData)
                        .orElse(Collections.emptyList());

                // 导出为 Excel
                this.export(data, pager);
                return null;
            } else {
                // 如果原接口返回 null，那应该尊重原接口的行为，也返回 null
                if (resultObject == null) {
                    return null;
                }

                // 结果中的数据(如无返回值或获取到 null 的数据，则用空列表代替)
                @SuppressWarnings("unchecked")
                List<Object> data = (List<Object>) Optional.ofNullable(PagerResults.getData(resultObject))
                        .orElse(Collections.emptyList());

                // 当前页
                long currentPage = page.getPageNum();
                // 总记录数
                long totalRow = page.getTotal();
                // 总页数
                long totalPage = page.getPages();

                // 修改结果中的数据

                PagerResults.setData(resultObject, new PageBody<>()
                        .setListData(data)
                        .setPage(currentPage)
                        .setTotalRow(totalRow)
                        .setTotalPage(totalPage)
                );

                // 返回结果
                return resultObject;
            }
        } finally {
            PageHelper.clearPage();
        }
    }

    /**
     * 在调用 Handler 之前对分页进行配置
     * @param pager 分页注解对象
     * @return 是否启用导出模式
     */
    private boolean prePager(@Nonnull Pager pager) {
        // 确保之前的分页设置被清理掉
        PageHelper.clearPage();

        // 是否启用导出模式
        boolean export = false;

        // 获取请求对象
        HttpServletRequest request = ServletBox.request();
        // 判断是否为导出模式(非抽象才能实例化，把非抽象的视为导出模式)
        if (pager.export() != IPagerExport.class) {
            if (!Modifier.isAbstract(pager.export().getModifiers()) && PagerPlugin.existPublicNoArgConstructor(pager.export())) {
                String exportString = PagerPlugin.tryGetParam(request, this.pagerConfigure.getPrefix() + "export");
                // 导出模式
                if ("true".equalsIgnoreCase(exportString)) {
                    export = true;

                    // 是否继续分页
                    String keepPagerString = PagerPlugin.tryGetParam(request, this.pagerConfigure.getPrefix() + "pager");
                    if (!"true".equalsIgnoreCase(keepPagerString)) {
                        return true;
                    }
                }
            } else {
                throw new PagerException(String.format("@Pager 的 export 属性为 %s，但其无法被实例化.", pager.export().getName()));
            }
        }

        // 从请求中获取 page 参数
        int page;
        String pageString = PagerPlugin.tryGetParam(request, this.pagerConfigure.getPrefix() + "page", "1");
        if (Strings.isBlank(pageString)) {
            page = 1; // 未传默认为第一页
        } else {
            page = Integer.parseInt(pageString);
        }

        // 从请求中读取每页的结果数
        int pageSize;
        String pageSizeString = PagerPlugin.tryGetParam(request, this.pagerConfigure.getPrefix() + "pageSize");
        if (Strings.isBlank(pageSizeString)) {
            pageSize = pager.df(); // 未传默认用注解中的默认值作为每页的记录数
        } else {
            pageSize = Integer.parseInt(pageSizeString);
        }

        // 限制每页结果数的最大最小值
        pageSize = Math.min(Math.max(pageSize, pager.min()), pager.max());

        // 设置分页参数
        PageHelper.startPage(page, pageSize);

        // 返回结果
        return export;
    }

    /**
     * 尝试读取参数<br>
     * 目前只支持在 URL 上的查询字符串和 Header 中的参数，如果两者均存在，则 URL 参数优先
     * @param request 请求对象
     * @param name 参数名
     * @return 获取到的参数值，如没获取到，则返回 null
     */
    @Nullable
    private static String tryGetParam(@Nonnull HttpServletRequest request, @Nonnull String name) {
        return PagerPlugin.tryGetParam(request, name, null);
    }

    /**
     * 尝试读取参数<br>
     * 目前只支持在 URL 上的查询字符串和 Header 中的参数，如果两者均存在，则 URL 参数优先
     * @param request 请求对象
     * @param name 参数名
     * @param df 没获取到时的默认值
     * @return 获取到的参数值，如没获取到，则返回 df 参数传入的内容
     */
    @Nullable
    private static String tryGetParam(@Nonnull HttpServletRequest request, @Nonnull String name, @Nullable String df) {
        String result = request.getParameter(name);
        if (result != null) {
            return result;
        }

        result = request.getHeader(name);
        if (result != null) {
            return result;
        }

        return df;
    }

    /**
     * 判断一个类是否存在 public 的无参构造方法
     * @param clazz 被判断的类
     * @return 是否存在 public 的无参构造方法
     */
    private static boolean existPublicNoArgConstructor(@Nonnull Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 导出查到的记录为 Excel 工作簿，并通过 Response 输出给请求方
     * @param data 被输出的记录
     * @param pager 分页注解(用于获取导出配置)
     */
    private void export(@Nonnull List<?> data, @Nonnull Pager pager) {
        try {
            // 实例化导出对象
            IPagerExport<Object> export = this.newInstance(pager.export());

            // 设置响应头
            HttpServletResponse response = ServletBox.response();
            response.reset();
            // 文件名
            String filename = URLEncoder.encode(export.getFilename(pager.filenamePrefix()), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + filename);
            // MIME
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            // 生成并输出 Excel 文件
            export.setColumns(pager.exportColumns());
            @SuppressWarnings("unchecked")
            Workbook workbook = export.toWorkBook((List) data);
            workbook.write(response.getOutputStream());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | IOException e) {
            throw new PagerException(e);
        }
    }

    /**
     * 用无参构造实例化一个 Class
     * @param clazz 被实例化的 Class
     * @param <T> 数据的类型
     * @return 实例化的对象
     * @throws NoSuchMethodException 如果没有找到无参的构造方法
     * @throws IllegalAccessException 如果无权访问无参的构造方法
     * @throws InvocationTargetException 如果构造方法抛出了异常
     * @throws InstantiationException 如果这个类是一个抽象类
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    private <T> T newInstance(@Nonnull Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (T) clazz.getConstructor().newInstance();
    }
}
