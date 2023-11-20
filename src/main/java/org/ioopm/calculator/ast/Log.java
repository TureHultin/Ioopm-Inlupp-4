package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

public class Log extends Unary {
    public Log(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "Log";
    }

    @Override
    public SymbolicExpression eval(Environment vars) throws IllegalExpressionException {
        return evalUnary(vars, Log::new, Math::log);
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
