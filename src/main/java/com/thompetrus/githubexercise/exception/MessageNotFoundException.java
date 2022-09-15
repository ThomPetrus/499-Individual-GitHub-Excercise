package com.thompetrus.githubexercise.exception;

/**
 * Standard custom exception class to be caught by the custom handler.
 * Occurs when a message is not found for a requested key.
 */
public class MessageNotFoundException extends RuntimeException {

    /**
     * Simple exception to be thrown if no message is found for a given key.
     *
     * @param message - Message not found message to be passed to this exception.
     */
    public MessageNotFoundException(String message) {
        super(message);
    }
}

