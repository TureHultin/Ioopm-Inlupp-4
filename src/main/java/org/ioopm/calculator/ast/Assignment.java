package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

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
    public SymbolicExpression eval(Environment vars) throws IllegalExpressionException {
        // rhs is already a fully evaluated identifier
        SymbolicExpression expression = getLhs().eval(vars);
        if (getRhs() instanceof Variable rhs) {
            vars.put(rhs, expression);
        } else {
            throw new IllegalExpressionException("Can't assign to something that isin't a variable", getRhs());
        }
        return expression;
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
