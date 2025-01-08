package com.cornerstone4j.web.autoconfigure.handler;

import cn.hutool.core.text.StrPool;
import com.cornerstone4j.core.exception.GlobalExceptionEnum;
import com.cornerstone4j.core.exception.ServiceException;
import com.cornerstone4j.core.model.Result;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class DefaultGlobalExceptionHandler {


    @ExceptionHandler({Exception.class})
    public Result<String> exceptionHandler(Exception exception) {
        log.warn("[Exception]", exception);
        return Result.failure(exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = ServiceException.class)
    public Result<String> exceptionHandler(ServiceException exception) {
        log.warn("[ServiceException]", exception);
        return Result.failure((exception.getCode() == 0 ? GlobalExceptionEnum.FAILED.code() : exception.getCode()), exception.getMessage());
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        log.warn("[MissingServletRequestParameterExceptionHandler]", exception);
        return Result.failure(GlobalExceptionEnum.FAILED, String.format("请求参数缺失:%s", exception.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {
        log.warn("[MethodArgumentTypeMismatchExceptionHandler]", exception);
        return Result.failure(GlobalExceptionEnum.FAILED, String.format("请求参数类型错误:%s", exception.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<String> bodyValidExceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        // 取出所有的校验不通过的描述
        List<String> fieldErrorMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.warn("MethodArgumentNotValidException: {}", exception.getMessage());
        return Result.failure(GlobalExceptionEnum.FAILED, String.join(StrPool.COMMA, fieldErrorMessages));
    }

    @ExceptionHandler({BindException.class})
    public Result<String> bindExceptionHandler(BindException exception) {
        log.warn("BindException: {}", exception.getMessage());
        FieldError fieldError = exception.getFieldError();
        // 断言，避免告警
        assert fieldError != null;
        return Result.failure(GlobalExceptionEnum.FAILED, ObjectUtils.isEmpty(fieldError.getDefaultMessage()) ? GlobalExceptionEnum.FAILED.msg() : fieldError.getDefaultMessage());
    }

    @ExceptionHandler({JsonParseException.class})
    public Result<String> exceptionHandler(JsonParseException exception) {
        log.warn("JsonParseException: {}", exception.getMessage());
        return Result.failure(exception.getMessage());
    }

    @ExceptionHandler({JsonMappingException.class})
    public Result<String> exceptionHandler(JsonMappingException exception) {
        log.warn("JsonMappingException: {}", exception.getMessage());
        return Result.failure(exception.getMessage());
    }


}
