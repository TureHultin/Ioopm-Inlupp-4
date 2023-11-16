package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

public class Cos extends Unary {
    public Cos(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "Cos";
    }


    @Override
    public SymbolicExpression eval(Environment vars) throws IllegalExpressionException {
        return evalUnary(vars, Cos::new, Math::cos);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cos other) {
            return getRhs().equals(other.getRhs());
        }

        return false;
    }
}
