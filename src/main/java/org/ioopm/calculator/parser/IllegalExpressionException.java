package org.ioopm.calculator.parser;

import org.ioopm.calculator.ast.SymbolicExpression;

public class IllegalExpressionException extends Exception {
    SymbolicExpression expression;

    public IllegalExpressionException(String s) {
        super(s);
    }

    public IllegalExpressionException(String s, SymbolicExpression expression) {
        super(s);
        this.expression = expression;
    }
}
