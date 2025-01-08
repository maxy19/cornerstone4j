package com.cornerstone4j.core.model;

import cn.hutool.core.date.SystemClock;
import com.cornerstone4j.core.exception.ErrorCode;
import com.cornerstone4j.core.exception.GlobalExceptionEnum;
import com.cornerstone4j.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 874200365941306385L;
    private Integer code;
    private String msg;
    private T data;
    private Long timestamp;

    private Result(ErrorCode errorCode) {
        this(errorCode.code(), errorCode.msg());
    }

    private Result(ErrorCode errorCode, T data) {
        this(errorCode.code(), errorCode.msg(), data);
    }

    public Result(int code, String msg) {
        this(code, msg, null);
    }

    public Result(int code, String msg, T data) {
        this.timestamp = SystemClock.now();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static boolean isSuccess(Result<?> packResult) {
        return Optional.ofNullable(packResult).map((r) -> r.code).map((code) -> GlobalExceptionEnum.SUCCESS.code() == code).orElse(Boolean.FALSE);
    }

    public static boolean isNotSuccess(Result<?> r) {
        return !isSuccess(r);
    }

    public static Integer getErrCode(Result<?> r) {
        return Optional.ofNullable(r).isPresent() ? Optional.of(r.code).get() : GlobalExceptionEnum.FAILED.code();
    }

    public static String getErrMsg(Result<?> r) {
        return Optional.ofNullable(r).isPresent() ? Optional.of(r.msg).get() : GlobalExceptionEnum.FAILED.msg();
    }


    public static <T> T getData(Result<T> packResult) {
        return Optional.ofNullable(packResult).filter((r) -> r.code == GlobalExceptionEnum.SUCCESS.code()).map((r) -> r.data).orElse(null);
    }

    public static <T> Result<T> success() {
        return new Result(GlobalExceptionEnum.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result(GlobalExceptionEnum.SUCCESS, data);
    }

    public static <T> Result<T> failure() {
        return new Result(GlobalExceptionEnum.FAILED);
    }

    public static <T> Result<T> failure(String msg) {
        return new Result(GlobalExceptionEnum.FAILED.code(), msg);
    }

    public static <T> Result<T> failure(int code, String msg) {
        return new Result(code, msg);
    }

    public static <T> Result<T> failure(ErrorCode errorCode, String msg) {
        return new Result(errorCode, msg);
    }

    public static <T> Result<T> failure(ErrorCode errorCode, String msg, T data) {
        return new Result(errorCode.code(), msg, data);
    }

    public static <T> Result<T> failure(ErrorCode errorCode) {
        return new Result(errorCode);
    }

    public static <T> Result<T> failure(ServiceException ex) {
        return failure(ex.getCode(), ex.getMsg());
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result(errorCode);
    }

    public static <T> Result<T> error(ErrorCode errorCode, String msg) {
        return new Result(errorCode.code(), msg);
    }

}
