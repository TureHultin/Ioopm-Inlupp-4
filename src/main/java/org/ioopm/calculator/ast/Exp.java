package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Exp extends Unary {
    public Exp(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "Exp";
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Exp other) {
            return getRhs().equals(other.getRhs());
        }

        return false;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
