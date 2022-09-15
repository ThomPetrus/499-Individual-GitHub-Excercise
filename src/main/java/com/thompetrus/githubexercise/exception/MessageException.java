package com.thompetrus.githubexercise.exception;


/**
 * Standard custom exception class to be caught by the custom handler.
 * Occurs when anything goes wrong getting/setting the messages in Redis.
 */
public class MessageException extends RuntimeException {

    /**
     * Standard override of RunTimeException to be caught appropriately.
     *
     * @param message - error message
     * @param cause - throwable cause
     */
    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
