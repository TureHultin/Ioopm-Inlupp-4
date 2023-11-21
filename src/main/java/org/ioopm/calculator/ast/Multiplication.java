package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Multiplication extends Binary {

    public Multiplication(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public String getName() {
        return "*";
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Multiplication other) {
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
