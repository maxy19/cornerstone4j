package com.cornerstone4j.core.exception;


import com.cornerstone4j.core.model.Result;
import com.cornerstone4j.core.utils.LogMsgUtils;
import lombok.Getter;

public class ServiceException extends RuntimeException {

    @Getter
    private Integer code;

    @Getter
    private String msg;

    public ServiceException(String msg) {
        super(msg);
        this.code = -1;
        this.msg = msg;
    }

    public ServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.msg());
        this.code = errorCode.code();
        this.msg = errorCode.msg();
    }

    public ServiceException(Result<?> result) {
        super(result.getMsg());
        this.code = result.getCode();
        this.msg = result.getMsg();
    }

    public ServiceException(ErrorCode errorCode, String errMsg, Object... args) {
        super(LogMsgUtils.format(errMsg, args));
        this.code = errorCode.code();
        this.msg = LogMsgUtils.format(errMsg, args);
    }

    public ServiceException(String msg, Object... args) {
        super(LogMsgUtils.format(msg, args));
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
