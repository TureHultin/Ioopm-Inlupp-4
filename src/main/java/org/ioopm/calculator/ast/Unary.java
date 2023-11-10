package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

/**
 * An expression with one contained expression
 */
public abstract class Unary extends SymbolicExpression {
    public SymbolicExpression getRhs() {
        return rhs;
    }

    private final SymbolicExpression rhs;

    Unary(SymbolicExpression rhs) {
        this.rhs = rhs;
    }

    /**
     * Helper to evaluate arithmetic unary expressions
     *
     * @param vars     the lexical environment
     * @param makeNode a function to create the resulting node
     * @param reduce   the corresponding operator
     * @return the evaluated expression
     * @throws IllegalExpressionException if it contains a faulty Assignment
     */
    SymbolicExpression evalUnary(Environment vars, UnaryOperator<SymbolicExpression> makeNode, DoubleUnaryOperator reduce) throws IllegalExpressionException {
        SymbolicExpression rhs = getRhs().eval(vars);

        if (rhs.isConstant()) {
            return new Constant(reduce.applyAsDouble(rhs.getValue()));
        }

        return makeNode.apply(rhs);

    }

    @Override
    public int getPriority() {
        return 150;
    }

    @Override
    public String toString() {
        String rhsString = rhs.toString();
        if (rhs instanceof Atom) {
            rhsString = ' ' + rhsString;
        } else {
            rhsString = '(' + rhsString + ')';
        }
        return getName() + rhsString;
    }
}


