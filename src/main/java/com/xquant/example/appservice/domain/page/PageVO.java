package com.xquant.example.appservice.domain.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author 05429
 */
@ApiModel(value = "分页结果对象")
@Getter
@AllArgsConstructor
public class PageVO<T> {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", required = true)
    private Integer pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量", required = true)
    private Integer pageSize;

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数", required = true)
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数", required = true)
    private Integer pages;

    /**
     * 分页数据
     */
    @ApiModelProperty(value = "分页数据", required = true)
    private List<T> list;

    public static <T> PageVO<T> wrap(List<T> rows) {
        if (rows instanceof Page<?>) {
            Page<T> page = (Page<T>)rows;
            return new PageVO<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getPages(),
                    page.getResult());
        } else {
            return new PageVO<>(1, rows.size(), (long)rows.size(), 1, rows);
        }
    }

    public static <T> PageVO<T> of(int pageNum, int pageSize, long total, int pages, List<T> data) {
        return new PageVO<>(pageNum, pageSize, total, pages, data);
    }

    public static <T, R> PageVO<R> of(PageInfo<T> pageInfo, List<R> data) {
        return new PageVO<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), pageInfo.getPages(),
                data);
    }
}
