package org.ioopm.calculator.parser;

/**
 * Exception thrown when a function call is invalid, e.g. due to
 * incorrect number of arguments or undefined functions.
 */
public class FunctionCallException extends RuntimeException {
    public FunctionCallException(String message) {
        super(message);
    }
}
