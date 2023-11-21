package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Cos extends Unary {
    public Cos(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "Cos";
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cos other) {
            return getRhs().equals(other.getRhs());
        }

        return false;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
