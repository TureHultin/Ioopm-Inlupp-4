package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

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
    public boolean equals(Object obj) {
        if (obj instanceof Constant other) {
            return getValue() == other.getValue();
        }

        return false;
    }


    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
