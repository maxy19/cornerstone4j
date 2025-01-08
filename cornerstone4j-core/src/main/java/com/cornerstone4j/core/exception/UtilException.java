package com.cornerstone4j.core.exception;


import com.cornerstone4j.core.model.Result;
import com.cornerstone4j.core.utils.LogMsgUtils;
import lombok.Getter;

public class UtilException extends ServiceException {

    @Getter
    private Integer code;

    @Getter
    private String msg;

    public UtilException(String msg) {
        super(msg);
        this.code = -1;
        this.msg = msg;
    }

    public UtilException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public UtilException(ErrorCode errorCode) {
        super(errorCode.msg());
        this.code = errorCode.code();
        this.msg = errorCode.msg();
    }

    public UtilException(Result<?> result) {
        super(result.getMsg());
        this.code = result.getCode();
        this.msg = result.getMsg();
    }

    public UtilException(ErrorCode resultCode, String errMsg, Object... args) {
        super(LogMsgUtils.format(errMsg, args));
        this.code = resultCode.code();
        this.msg = LogMsgUtils.format(errMsg, args);
    }

    public UtilException(String msg, Object... args) {
        super(LogMsgUtils.format(msg, args));
    }

    public UtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public UtilException(Throwable cause) {
        super(cause);
    }

    protected UtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
