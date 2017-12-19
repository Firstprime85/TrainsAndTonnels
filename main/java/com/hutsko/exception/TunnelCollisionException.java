package com.hutsko.exception;

public class TunnelCollisionException extends Exception{
    public TunnelCollisionException() {
    }

    public TunnelCollisionException(String message) {
        super(message);
    }

    public TunnelCollisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TunnelCollisionException(Throwable cause) {
        super(cause);
    }
}
