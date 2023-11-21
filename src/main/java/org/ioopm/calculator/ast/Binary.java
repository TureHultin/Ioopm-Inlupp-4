package org.ioopm.calculator.ast;

/**
 * Any expression with two children
 */
public abstract class Binary extends SymbolicExpression {
    public SymbolicExpression getLhs() {
        return lhs;
    }

    public SymbolicExpression getRhs() {
        return rhs;
    }

    private final SymbolicExpression lhs;
    private final SymbolicExpression rhs;

    Binary(SymbolicExpression lhs, SymbolicExpression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    protected String toStringNonCommutative() {
        String lhsString = getLhs().toString();

        if (getLhs().getPriority() < getPriority()) {
            lhsString = '(' + lhsString + ')';
        }

        String rhsString = rhs.toString();

        // The <= is to handle the weird things where
        // (1 + 2 ) - (1 + 2) == 1 + 2 - (1 + 2)
        // but not 1 + 2 - 1 + 2
        // or 2 * 2 / 2 * 2 is not 2 * 2 /(2 * 2)
        if (getRhs().getPriority() <= getPriority()) {
            rhsString = '(' + rhsString + ')';
        }
        return lhsString + " " + getName() + " " + rhsString;
    }

    @Override
    public String toString() {
        String lhsString = lhs.toString();
        if (lhs.getPriority() < getPriority()) {
            lhsString = '(' + lhsString + ')';
        }
        String rhsString = rhs.toString();


        if (rhs.getPriority() < getPriority()) {
            rhsString = '(' + rhsString + ')';
        }
        return lhsString + " " + getName() + " " + rhsString;
    }
}

