package com.cornerstone4j.aop.autoconfigure;

import lombok.Data;

import java.util.List;

/**
 * @Author maxy
 * @Date 2024/02/02 18:12
 **/
@Data
public abstract class AbstractAdviceConfig {
    /**
     * 日志打印开关
     */
    private boolean printLog = true;
    /**
     * 慢请求阈值
     */
    private long slowReqTime = 1000L;
    /**
     * 顺序
     */
    private Integer order;
    /**
     * 排除切点方法/类
     */
    private List<String> exclude;

}
