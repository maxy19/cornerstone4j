package com.cornerstone4j.core.exception;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum GlobalExceptionEnum implements ErrorCode {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 失败
     */
    FAILED(-1, "失败"),
    ;

    private final Integer code;

    private final String msg;

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
