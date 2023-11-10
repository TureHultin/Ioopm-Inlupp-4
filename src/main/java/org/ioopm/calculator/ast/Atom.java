package org.ioopm.calculator.ast;

public abstract class Atom extends SymbolicExpression {
    @Override
    public int getPriority() {
        return 1000_000_000;
    }
}

