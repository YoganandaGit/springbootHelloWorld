package com.grpcdemo.app.grpc.exception;

/**
 * The type Invalid config exception.
 */
public class InvalidConfigException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * Instantiates a new Invalid config exception.
     */
    public InvalidConfigException() {
    }

    /**
     * Instantiates a new Invalid config exception.
     *
     * @param message the message
     */
    public InvalidConfigException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Invalid config exception.
     *
     * @param cause the cause
     */
    public InvalidConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Invalid config exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public InvalidConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
