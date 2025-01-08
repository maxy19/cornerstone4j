package com.cornerstone4j.mybatisflex.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.PageUtil;
import com.cornerstone4j.core.model.PageRequest;
import com.cornerstone4j.core.model.PageResult;
import com.mybatisflex.core.paginate.Page;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;


@UtilityClass
public class PageUtils extends PageUtil {

    /**
     * convert
     *
     * @param page   {@link Page} 原始数据
     * @param target 目标数据类型
     * @param <S>    原始数据类型
     * @param <T>    目标数据类型
     * @return {@link PageResult}
     */
    public <S, T> PageResult<T> convert(Page<S> page, Class<T> target) {
        Page<T> targetPage = page.map(each -> BeanUtil.copyProperties(each, target));
        return convert(targetPage);
    }


    /**
     * convert
     *
     * @param page {@link Page} 原始数据
     * @return {@link PageResult}
     */
    public <T> PageResult<T> convert(Page<T> page) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.of(page.getPageSize(), page.getPageNumber(), page.getTotalRow());
        pageResult.setList(page.getRecords());
        return pageResult;
    }

    /**
     * 构建{@link Page}MyBatis Plus分页对象
     *
     * @param request 请求
     * @return {@link Page}<{@link T}>
     */
    public <T> Page<T> buildPageObj(PageRequest request) {
        return new Page<>(request.getPageNo(), request.getPageSize());
    }

    /**
     * 构建页面结果
     *
     * @param request 请求
     * @param list    列表
     * @param total   总计
     * @return {@link PageResult}<{@link T}>
     */
    public <T> PageResult<T> buildPageResult(PageRequest request, List<T> list, Integer total) {
        return new PageResult<T>()
            .of(request.getPageSize(), request.getPageNo(), total)
            .setList(list);
    }

    /**
     * 构建页面结果
     *
     * @param request 请求
     * @param mpPage  议员页面
     * @return {@link PageResult}<{@link T}>
     */
    public <T> PageResult<T> buildPageResult(PageRequest request, Page<T> mpPage) {
        return new PageResult<T>()
            .of(request.getPageSize(), request.getPageNo(), mpPage.getTotalRow())
            .setList(mpPage.getRecords());
    }

    /**
     * 子列表页面
     *
     * @param list     列表
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> subListWithPage(List<T> list, Integer pageNum, Integer pageSize) {
        return subListWithPage(list, Long.valueOf(pageNum), Long.valueOf(pageSize));
    }

    /**
     * 子列表页面
     *
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @param list     列表
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> subListWithPage(List<T> list, Long pageNum, Long pageSize) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        //记录总数
        Long count = (long) list.size();
        Long pageCount = count / pageSize;
        if (count % pageSize != 0) {
            pageCount++;
        }
        long fromIndex;
        long toIndex;
        if (pageNum > pageCount) {
            pageNum = pageCount;
        }

        fromIndex = (pageNum - 1) * pageSize;

        if (!pageNum.equals(pageCount)) {
            toIndex = fromIndex + pageSize;
        } else {
            toIndex = count;
        }
        return list.subList((int) fromIndex, (int) toIndex);
    }

}
