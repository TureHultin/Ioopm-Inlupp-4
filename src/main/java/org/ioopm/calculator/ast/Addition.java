package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

public class Addition extends Binary {

    public Addition(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public String getName() {
        return "+";
    }

    @Override
    public SymbolicExpression eval(Environment vars) throws IllegalExpressionException {
        return evalBinary(vars, Addition::new, (left, right) -> left + right);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Addition other) {
            return this.getLhs().equals(other.getLhs())
                    && this.getRhs().equals(other.getRhs());
        }

        return false;
    }
}

