package com.cornerstone4j.aop.advice;


import com.cornerstone4j.aop.autoconfigure.AbstractAdviceConfig;
import com.cornerstone4j.core.model.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author maxy
 **/
@Slf4j
public abstract class AbstractLogAdvice {

    protected AbstractAdviceConfig config;
    /**
     * 执行结果对象
     */
    protected Result result;
    /**
     * 抛出异常对象
     */
    protected Throwable errorInfo;

    /**
     * 后置处理器
     */
    protected Result postProcess(Object returnResult) {
        Result packResult;
        if (returnResult instanceof Result) {
            packResult = (Result) returnResult;
        } else {
            packResult = Result.success(returnResult);
        }
        return packResult;
    }

    public void setConfig(AbstractAdviceConfig config) {
        this.config = config;
    }


}
