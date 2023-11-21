package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Log extends Unary {
    public Log(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "Log";
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Log other) {
            return getRhs().equals(other.getRhs());
        }

        return false;
    }


    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
