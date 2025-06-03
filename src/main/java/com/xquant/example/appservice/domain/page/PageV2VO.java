package com.xquant.example.appservice.domain.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * @author 05429
 */
@ApiModel(value = "分页结果对象")
@Getter
@AllArgsConstructor
public class PageV2VO<T> {

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

    @ApiModelProperty(value = "列信息")
    private List<ColumnInfo> columns;

    @ApiModelProperty(value = "列信息分组列信息")
    private List<ColumnInfo> groups;

    @ApiModelProperty(value = "客户分组列信息")
    private List<ColumnInfo> customGroups;


    public static <T> PageV2VO<T> of(int pageNum, int pageSize, long total, int pages, List<T> data, List<ColumnInfo> columns,List<ColumnInfo> groups,List<ColumnInfo> customGroups) {
        return new PageV2VO<>(pageNum, pageSize, total, pages, data, columns, groups, customGroups);
    }


    @Data
    public static class ColumnInfo {

        @ApiModelProperty(value = "列编号")
        private String code;

        @ApiModelProperty(value = "列名称")
        private String name;
    }
}
