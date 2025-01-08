package com.cornerstone4j.aop.advice;


import cn.hutool.core.util.BooleanUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.cornerstone4j.aop.model.ProcessParams;
import com.cornerstone4j.core.exception.GlobalExceptionEnum;
import com.cornerstone4j.core.exception.ServiceException;
import com.cornerstone4j.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.ThreadContext;

import java.util.Objects;

import static com.cornerstone4j.core.constant.TraceConstant.TRACE_ID;

/**
 * @author maxy
 */
@Slf4j
public class FacadeLogAdvice extends AbstractLogAdvice implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation invocation) {
        long startTime = System.currentTimeMillis();
        Object[] arguments = invocation.getArguments();
        Boolean isJson = JSONUtil.isTypeJSON(String.valueOf(arguments));
        String reqParamJson = null;
        if (BooleanUtil.isFalse(isJson)) {
            reqParamJson = JSONUtil.toJsonStr(arguments);
        }
        handle(invocation);
        //类全路径
        String clazzPath = invocation.getMethod().getDeclaringClass().getName();
        if (config.isPrintLog()) {
            ProcessParams processParams = ProcessParams.builder().reqParamJson(reqParamJson)
                    .respResultJson(JSONUtil.toJsonStr(result))
                    .methodName(clazzPath.concat(".").concat(invocation.getMethod().getName()))
                    .duration(System.currentTimeMillis() - startTime).build();
            resultProcess(processParams);
        }
        return result;
    }

    /**
     * 逻辑处理
     *
     * @param invocation
     */
    protected void handle(MethodInvocation invocation) {
        try {
            ThreadContext.put(TRACE_ID, MD5.create().digestHex16(System.nanoTime() + invocation.getMethod().getName()));
            Object returnResult = invocation.proceed();
            result = postProcess(returnResult);
        } catch (ServiceException e) {
            result = Result.failure(e);
            errorInfo = e;
        } catch (Throwable e) {
            result = Result.error(GlobalExceptionEnum.FAILED);
            errorInfo = e;
        }
    }

    /**
     * 结果处理
     */
    private void resultProcess(ProcessParams processParams) {
        log.info("FacadeAdvice#methodName:{},elapsedTime:{}\nreq:{}\nresp:{} ", processParams.getMethodName(), processParams.getDuration(), processParams.getReqParamJson(), processParams.getRespResultJson());
        if (processParams.getDuration() > config.getSlowReqTime()) {
            log.warn("FacadeAdvice#slow request methodName:{},elapsedTime:{} .", processParams.getMethodName(), processParams.getDuration());
        }
        if (Objects.nonNull(errorInfo)) {
            log.error("ControllerAdvice#catch exception", errorInfo);
        }
    }
}
