package com.cornerstone4j.aop.advice;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.cornerstone4j.aop.model.ProcessParams;
import com.cornerstone4j.aop.utils.RequestUtils;
import com.cornerstone4j.core.exception.GlobalExceptionEnum;
import com.cornerstone4j.core.exception.ServiceException;
import com.cornerstone4j.core.model.Result;
import com.cornerstone4j.core.model.signal.SignalInfo;
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
public class ControllerLogAdvice extends AbstractLogAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) {
        long startTime = System.currentTimeMillis();
        Object[] arguments = invocation.getArguments();
        String uri = RequestUtils.getUri();
        String reqParamJson;
        reqParamJson = JSONUtil.toJsonStr(ArrayUtil.length(arguments) == 1 ? arguments[0] : arguments);
        handle(invocation);
        if (config.isPrintLog()) {
            ProcessParams processParams = ProcessParams.builder().reqParamJson(reqParamJson)
                    .respResultJson(JSONUtil.toJsonStr(result))
                    .uri(uri).duration(System.currentTimeMillis() - startTime)
                    .build();
            resultProcess(processParams);
        }
        return result;
    }

    private SignalInfo getSignalInfoByArgs(Object[] arguments) {
        if (ArrayUtil.isNotEmpty(arguments) && arguments[0] instanceof SignalInfo signalInfo) {
            return signalInfo;
        }
        return null;
    }

    private void handle(MethodInvocation invocation) {
        handle(invocation, null);
    }

    private void handle(MethodInvocation invocation, SignalInfo signalInfo) {
        try {
            ThreadContext.put(TRACE_ID, MD5.create().digestHex16(System.nanoTime() + invocation.getMethod().getName()));
            if (Objects.nonNull(signalInfo)) {
                Object[] arguments = invocation.getArguments();
                arguments[0] = signalInfo;
            }
            result = postProcess(invocation.proceed());
        } catch (ServiceException e) {
            result = Result.failure(e);
            errorInfo = e;
        } catch (Throwable e) {
            result = Result.error(GlobalExceptionEnum.FAILED);
            errorInfo = e;
        }
    }

    /**
     * 后置处理器
     *
     * @param processParams
     */
    private void resultProcess(ProcessParams processParams) {
        log.info("ControllerAdvice#uri:{},elapsedTime:{}\nreq:{}\nresp:{} ", processParams.getUri(), processParams.getDuration(), processParams.getReqParamJson(), processParams.getRespResultJson());
        if (processParams.getDuration() > config.getSlowReqTime()) {
            log.warn("ControllerAdvice#slow request uri:{},elapsedTime:{} .", processParams.getUri(), processParams.getDuration());
        }
        if (Objects.nonNull(errorInfo)) {
            log.error("ControllerAdvice#catch exception", errorInfo);
        }
    }


}
