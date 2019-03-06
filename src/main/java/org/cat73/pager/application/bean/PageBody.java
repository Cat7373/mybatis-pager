package org.cat73.pager.application.bean;

import java.util.List;
import java.util.Objects;

/**
 * 分页返回值的 data 部分
 * @param <T> 实际数据的数据类型
 */
public final class PageBody<T> {
    /**
     * 实际的数据列表
     */
    private List<T> listData;
    /**
     * 当前是第几页
     */
    private long page;
    /**
     * 总记录数
     */
    private long totalRow;
    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 获取实际的数据列表
     * @return 实际的数据列表
     */
    public List<T> getListData() {
        return listData;
    }

    /**
     * 设置实际的数据列表
     * @param listData 新的数据列表
     */
    public PageBody<T> setListData(List<T> listData) {
        this.listData = listData;
        return this;
    }

    /**
     * 获取当前是第几页
     * @return 当前是第几页
     */
    public long getPage() {
        return page;
    }

    /**
     * 设置当前是第几页
     * @param page 第几页
     */
    public PageBody<T> setPage(long page) {
        this.page = page;
        return this;
    }

    /**
     * 获取总记录数
     * @return 总记录数
     */
    public long getTotalRow() {
        return totalRow;
    }

    /**
     * 设置总记录数
     * @param totalRow 总记录数
     */
    public PageBody<T> setTotalRow(long totalRow) {
        this.totalRow = totalRow;
        return this;
    }

    /**
     * 获取总页数
     * @return 总页数
     */
    public long getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数
     * @param totalPage 总页数
     */
    public PageBody<T> setTotalPage(long totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PageBody<?> pageBody = (PageBody<?>) o;
        return this.page == pageBody.page &&
                this.totalRow == pageBody.totalRow &&
                this.totalPage == pageBody.totalPage &&
                Objects.equals(this.listData, pageBody.listData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listData, page, totalRow, totalPage);
    }

    @Override
    public String toString() {
        return "PageBody{" +
                "listData=" + listData +
                ", page=" + page +
                ", totalRow=" + totalRow +
                ", totalPage=" + totalPage +
                '}';
    }
}
