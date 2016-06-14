package com.codesparkle.poker.shared.net;

public class NetException extends RuntimeException {

    public NetException(Throwable cause) {
        super(cause);
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException() {
    }
}
