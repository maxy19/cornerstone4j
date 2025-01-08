package com.cornerstone4j.core.exception;

import java.io.Serializable;


public interface ErrorCode extends Serializable {
    /**
     * Return the integer value of this status code.
     */
    Integer code();

    /**
     * Return the String value of error reason phrase.
     */
    String msg();
}
