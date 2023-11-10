package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

public class Exp extends Unary {
    public Exp(SymbolicExpression rhs) {
        super(rhs);
    }

    @Override
    public String getName() {
        return "exp";
    }

    @Override
    public SymbolicExpression eval(Environment vars) throws IllegalExpressionException {
        return evalUnary(vars, Exp::new, Math::exp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Exp other) {
            return getRhs().equals(other.getRhs());
        }

        return false;
    }
}
