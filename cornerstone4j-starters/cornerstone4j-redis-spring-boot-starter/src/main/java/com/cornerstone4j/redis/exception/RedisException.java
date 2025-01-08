package com.datamesh.multiverse.redis.exception;


import com.cornerstone4j.core.exception.ErrorCode;
import com.cornerstone4j.core.exception.ServiceException;
import com.cornerstone4j.core.model.Result;
import com.cornerstone4j.core.utils.LogMsgUtils;
import lombok.Getter;

/**
 * @author maxy
 */
public class RedisException extends ServiceException {

    @Getter
    private Integer code;

    @Getter
    private String msg;

    public RedisException(String msg) {
        super(msg);
        this.code = -1;
        this.msg = msg;
    }

    public RedisException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public RedisException(ErrorCode errorCode) {
        super(errorCode.msg());
        this.code = errorCode.code();
        this.msg = errorCode.msg();
    }

    public RedisException(Result<?> result) {
        super(result.getMsg());
        this.code = result.getCode();
        this.msg = result.getMsg();
    }

    public RedisException(ErrorCode errorCode, String errMsg, Object... args) {
        super(LogMsgUtils.format(errMsg, args));
        this.code = errorCode.code();
        this.msg = LogMsgUtils.format(errMsg, args);
    }

    public RedisException(String msg, Object... args) {
        super(LogMsgUtils.format(msg, args));
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }

    protected RedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}