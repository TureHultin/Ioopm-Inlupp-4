package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Assignment extends Binary {

    public Assignment(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    public String getName() {
        return "=";
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Assignment other) {
            return this.getLhs().equals(other.getLhs())
                    && this.getRhs().equals(other.getRhs());
        }

        return false;
    }


    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
