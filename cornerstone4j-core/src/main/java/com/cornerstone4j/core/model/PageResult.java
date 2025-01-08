package com.cornerstone4j.core.model;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


@Getter
@ToString
@Schema(description = "分页结果")
public class PageResult<T> implements Serializable {

    @Schema(description = "数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> list = new ArrayList<>();

    @Schema(description = "总量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer total;

    @Schema(description = "总页数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer  totalPage;

    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNo;

    @Schema(description = "每页条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;

    public PageResult() {
        this.total = 0;
        this.totalPage = 0;
        this.pageNo = 1;
        this.pageSize = 10;
    }

    public PageResult<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public PageResult<T> setTotal(Integer total) {
        this.total = total;
        return this;
    }

    public PageResult<T> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public PageResult<T> setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public PageResult<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageResult<T> of(Long pageSize, Long pageNo, Long total) {
        return this.of(pageSize.intValue(), pageNo.intValue(), total.intValue());
    }

    public PageResult<T> of(Integer pageSize, Integer pageNo, Long total) {
        return this.of(pageSize, pageNo, total.intValue());
    }

    public PageResult<T> of(Integer pageSize, Integer pageNo, Integer total) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        if (total > 0) {
            this.total = total;

            if (total % this.pageSize > 0) {
                this.totalPage = (total / this.pageSize) + 1;
            } else {
                this.totalPage = (total / this.pageSize);
            }
        } else {
            this.totalPage = 0;
            this.total = 0;
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <R> PageResult<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getList().stream().map(mapper).collect(toList());
        return ((PageResult<R>)this).setList(collect);
    }

    @SuppressWarnings("unchecked")
    public <R> PageResult<R> convert(Class<R> target) {
        List<R> collect = this.getList().stream().map(s -> BeanUtil.copyProperties(s, target)).collect(toList());
        return ((PageResult<R>)this).setList(collect);
    }

}
