package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

public class Division extends Binary {
    public Division(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public String getName() {
        return "/";
    }

    @Override
    public String toString() {
        return super.toStringNonCommutative();
    }

    @Override
    public SymbolicExpression eval(Environment vars) throws IllegalExpressionException {
        return evalBinary(vars, Division::new, (left, right) -> left / right);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Division other) {
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
