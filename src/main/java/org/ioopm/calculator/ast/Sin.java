package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Sin extends Unary {
    public Sin(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "Sin";
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Sin other) {
            return getRhs().equals(other.getRhs());
        }

        return false;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
