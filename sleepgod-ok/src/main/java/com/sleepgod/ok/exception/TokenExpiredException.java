package com.sleepgod.ok.exception;

/**
 * token异常
 */
public class TokenExpiredException extends RuntimeException {


    public TokenExpiredException(String message) {
        super(message);
    }

}
