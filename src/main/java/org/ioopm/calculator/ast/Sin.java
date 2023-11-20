package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

public class Sin extends Unary {
    public Sin(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "Sin";
    }

    @Override
    public SymbolicExpression eval(Environment vars) throws IllegalExpressionException {
        return evalUnary(vars, Sin::new, Math::sin);
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
