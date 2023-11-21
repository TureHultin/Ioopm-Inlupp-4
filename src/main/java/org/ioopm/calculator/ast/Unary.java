package org.ioopm.calculator.ast;

/**
 * An expression with one contained expression
 */
public abstract class Unary extends SymbolicExpression {
    public SymbolicExpression getRhs() {
        return rhs;
    }

    private final SymbolicExpression rhs;

    Unary(SymbolicExpression rhs) {
        this.rhs = rhs;
    }


    @Override
    public int getPriority() {
        return 150;
    }

    @Override
    public String toString() {
        String rhsString = rhs.toString();
        if (rhs instanceof Atom) {
            rhsString = ' ' + rhsString;
        } else {
            rhsString = '(' + rhsString + ')';
        }
        return getName() + rhsString;
    }
}


