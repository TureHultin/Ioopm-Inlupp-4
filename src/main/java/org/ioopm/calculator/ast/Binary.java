package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;

/**
 * Any expression with two children
 */
public abstract class Binary extends SymbolicExpression {
    public SymbolicExpression getLhs() {
        return lhs;
    }

    public SymbolicExpression getRhs() {
        return rhs;
    }

    private final SymbolicExpression lhs;
    private final SymbolicExpression rhs;

    Binary(SymbolicExpression lhs, SymbolicExpression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * Helper to evaluate all arithmetic binary expressions
     *
     * @param vars     the lexical environment
     * @param makeNode a function to create the resulting node
     * @param reduce   the corresponding binary operator
     * @return the evaluated expression
     * @throws IllegalExpressionException if it contains a faulty Assignment
     */
    SymbolicExpression evalBinary(Environment vars, BinaryOperator<SymbolicExpression> makeNode, DoubleBinaryOperator reduce) throws IllegalExpressionException {
        SymbolicExpression lhsEval = getLhs().eval(vars);
        SymbolicExpression rhsEval = getRhs().eval(vars);

        if (lhsEval.isConstant() && rhsEval.isConstant()) {
            double simplifiedValue = reduce.applyAsDouble(lhsEval.getValue(), rhsEval.getValue());
            return new Constant(simplifiedValue);
        }

        return makeNode.apply(lhs, rhs);
    }

    @Override
    public String toString() {
        String lhsString = lhs.toString();
        if (lhs.getPriority() < getPriority()) {
            lhsString = '(' + lhsString + ')';
        }
        String rhsString = rhs.toString();

        // The <= is to handle the weird things where
        // (1 + 2 ) - (1 + 2) == 1 + 2 - (1 + 2)
        // but not 1 + 2 - 1 + 2
        // or 2 * 2 / 2 * 2 is not 2 * 2 /(2 * 2)
        if (rhs.getPriority() <= getPriority()) {
            rhsString = '(' + rhsString + ')';
        }
        return lhsString + " " + getName() + " " + rhsString;
    }
}

