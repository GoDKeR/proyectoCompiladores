package com.univ.compiladores.tokenizer.errors;

/**
 * Created by Facu on 8/10/2016.
 */
public class ParserException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ParserException(String message) {
        super(message);
    }
}
