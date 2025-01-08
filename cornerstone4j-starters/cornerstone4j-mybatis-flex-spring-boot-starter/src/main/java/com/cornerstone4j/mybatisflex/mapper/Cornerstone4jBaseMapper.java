package com.cornerstone4j.mybatisflex.mapper;

import com.cornerstone4j.core.model.PageRequest;
import com.cornerstone4j.core.model.PageResult;
import com.cornerstone4j.mybatisflex.util.PageUtils;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;


public interface Cornerstone4jBaseMapper<T> extends BaseMapper<T> {

    /**
     * 分页查询
     *
     * @param pageRequest  页面请求
     * @param queryWrapper 查询包装
     * @return {@link PageResult}<{@link T}>
     */
    default PageResult<T> selectPage(PageRequest pageRequest, QueryWrapper queryWrapper) {
        Page<T> page = Page.of(pageRequest.getPageNo(), pageRequest.getPageSize());
        // 转换返回
        return PageUtils.buildPageResult(pageRequest, paginate(page, queryWrapper));
    }


    /**
     * 分页查询
     *
     * @param pageRequest    页面请求
     * @param queryCondition 查询包装
     * @return {@link PageResult}<{@link T}>
     */
    default PageResult<T> selectPage(PageRequest pageRequest, QueryCondition queryCondition) {
        // 转换返回
        return PageUtils.buildPageResult(pageRequest, paginate(pageRequest.getPageNo(), pageRequest.getPageSize(), queryCondition));
    }

    /**
     * 分页查询
     *
     * @param pageRequest  页面请求
     * @param queryWrapper 查询包装
     * @return {@link PageResult}<{@link R}>
     */
    default <R> PageResult<R> selectPage(PageRequest pageRequest, QueryWrapper queryWrapper, Class<R> asType) {
        // 转换返回
        return PageUtils.buildPageResult(pageRequest, paginateAs(pageRequest.getPageNo(), pageRequest.getPageSize(), queryWrapper, asType));
    }
}
