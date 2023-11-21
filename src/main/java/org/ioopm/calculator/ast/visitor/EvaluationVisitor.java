package org.ioopm.calculator.ast.visitor;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

public class EvaluationVisitor implements Visitor<SymbolicExpression> {
    private final Environment vars;

    public EvaluationVisitor(Environment vars) {
        this.vars = vars;
    }

    public SymbolicExpression evaluate(SymbolicExpression expression) {
        return expression.accept(this);
    }

    /**
     * Helper to evaluate all arithmetic binary expressions
     *
     * @param binary   the binary node
     * @param makeNode a function to create the resulting node
     * @param reduce   the corresponding binary operator
     * @return the evaluated expression
     * @throws IllegalExpressionException if it contains a faulty Assignment
     */
    private SymbolicExpression evalBinary(Binary binary, BinaryOperator<SymbolicExpression> makeNode, DoubleBinaryOperator reduce) {
        SymbolicExpression lhsEval = binary.getLhs().accept(this);
        SymbolicExpression rhsEval = binary.getRhs().accept(this);

        if (lhsEval.isConstant() && rhsEval.isConstant()) {
            double simplifiedValue = reduce.applyAsDouble(lhsEval.getValue(), rhsEval.getValue());
            return new Constant(simplifiedValue);
        }

        return makeNode.apply(lhsEval, rhsEval);
    }

    /**
     * Helper to evaluate arithmetic unary expressions
     *
     * @param unary    the unary node
     * @param makeNode a function to create the resulting node
     * @param reduce   the corresponding operator
     * @return the evaluated expression
     * @throws IllegalExpressionException if it contains a faulty Assignment
     */
    private SymbolicExpression evalUnary(Unary unary, UnaryOperator<SymbolicExpression> makeNode, DoubleUnaryOperator reduce) {
        SymbolicExpression rhs = unary.getRhs().accept(this);

        if (rhs.isConstant()) {
            return new Constant(reduce.applyAsDouble(rhs.getValue()));
        }

        return makeNode.apply(rhs);

    }

    @Override
    public SymbolicExpression visit(Addition n) {
        return evalBinary(n, Addition::new, (left, right) -> left + right);
    }

    @Override
    public SymbolicExpression visit(Assignment n) {
        // rhs is already a fully evaluated identifier
        SymbolicExpression expression = n.getLhs().accept(this);
        if (n.getRhs() instanceof Variable rhs) {
            vars.put(rhs, expression);
        } else {
            throw new IllegalExpressionException("Can't assign to something that isin't a variable", n.getRhs());
        }
        return expression;
    }

    @Override
    public SymbolicExpression visit(Clear n) {
        throw new RuntimeException("Can't eval() commands");
    }

    @Override
    public SymbolicExpression visit(Constant n) {
        return new Constant(n.getValue());
    }

    @Override
    public SymbolicExpression visit(Cos n) {
        return evalUnary(n, Cos::new, Math::cos);
    }

    @Override
    public SymbolicExpression visit(Division n) {
        return evalBinary(n, Division::new, (left, right) -> left / right);

    }

    @Override
    public SymbolicExpression visit(Exp n) {
        return evalUnary(n, Exp::new, Math::exp);
    }

    @Override
    public SymbolicExpression visit(Log n) {
        return evalUnary(n, Log::new, Math::log);
    }

    @Override
    public SymbolicExpression visit(Multiplication n) {
        return evalBinary(n, Multiplication::new, (left, right) -> left * right);
    }

    @Override
    public SymbolicExpression visit(Negation n) {
        return evalUnary(n, Negation::new, rhs -> -rhs);
    }

    @Override
    public SymbolicExpression visit(Quit n) {
        throw new RuntimeException("Can't eval() commands");
    }

    @Override
    public SymbolicExpression visit(Sin n) {
        return evalUnary(n, Sin::new, Math::sin);

    }

    @Override
    public SymbolicExpression visit(Subtraction n) {
        return evalBinary(n, Subtraction::new, (left, right) -> left - right);
    }

    @Override
    public SymbolicExpression visit(Variable n) {
        return vars.getOrDefault(n, n);
    }

    @Override
    public SymbolicExpression visit(Vars n) {
        throw new RuntimeException("Can't eval() commands");
    }
}
