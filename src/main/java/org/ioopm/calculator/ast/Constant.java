package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;

public class Constant extends Atom {
    final double value;

    public Constant(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public SymbolicExpression eval(Environment vars) {
        return new Constant(getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Constant other) {
            return getValue() == other.getValue();
        }

        return false;
    }
}
