package com.sleepgod.ok.exception;

/**
 * 无网络的异常
 */
public class NetUnavailableException extends RuntimeException {


    public NetUnavailableException(String detailMessage) {
        super(detailMessage);
    }
}
