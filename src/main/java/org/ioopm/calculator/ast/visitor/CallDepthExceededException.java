package org.ioopm.calculator.ast.visitor;

public class CallDepthExceededException extends RuntimeException {
    public CallDepthExceededException(String message) {
        super(message);
    }
}
