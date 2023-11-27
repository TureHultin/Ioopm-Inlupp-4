package org.ioopm.calculator.ast.visitor;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.EnvironmentScopes;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.ioopm.calculator.parser.WrongArgumentNumberException;

import java.util.ArrayList;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

public class EvaluationVisitor implements Visitor<SymbolicExpression> {
    private Environment vars;
    private int callDepth = 0;
    private int maxCallDepth = 200;

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
        SymbolicExpression eval = vars.get(n);
        if (eval != null) {
            return eval;
        } else {
            return n;
        }
    }

    @Override
    public SymbolicExpression visit(Vars n) {
        throw new RuntimeException("Can't eval() commands");
    }

    @Override
    public SymbolicExpression visit(Scope n) {
        vars.pushEnvironment();
        SymbolicExpression exp = n.getExp().accept(this);
        vars.popEnvironment();
        return exp;
    }

    @Override
    public SymbolicExpression visit(FunctionDeclaration n) {
        
        vars.put(new Variable(n.getName()), n);

        return null;
    }

    @Override
    public SymbolicExpression visit(FunctionCall n) {
        callDepth += 1;
        if (callDepth > maxCallDepth) {
            throw new CallDepthExceededException("Error: Execeeded " + maxCallDepth + " calls");
        }

        SymbolicExpression callee = n.getCallee().accept(this);

        if (!(callee instanceof FunctionDeclaration)) {
            throw new IllegalExpressionException("Error: the callee '" + n.getCallee() + "' did not evaluate to a function");
        }

        final FunctionDeclaration declaration = (FunctionDeclaration) callee;
        ArrayList<String> parameters = declaration.getParameters();

        if (parameters.size() != n.getArguments().size()) {
            throw new WrongArgumentNumberException(n.getArguments().size(), declaration);
        }

        EnvironmentScopes calledScope = new EnvironmentScopes();

        // We put the called function into the inner scope
        // it's important we put it int first to allow shadowing the name with the arguments
        calledScope.put(new Variable(declaration.getName()), declaration);

        // We need to evaluate the arguments in the calling scope, 
        // but we want to put the values in the called functions scope
        for (int i = 0; i < parameters.size(); i++) {
            SymbolicExpression argument = n.getArguments().get(i).accept(this);

            calledScope.put(new Variable(parameters.get(i)), argument);
        }

        Environment outer = vars;
        // We evaluate the body in the new separate scope
        vars = calledScope;
        SymbolicExpression result = declaration.getBody().accept(this);

        vars = outer;
        callDepth -= 1;
        return result;
    }

    @Override
    public SymbolicExpression visit(Sequence n) {
        SymbolicExpression last = null;

        for (SymbolicExpression expression : n.getStatements()) {
            last = expression.accept(this);
        }

        if (last == null) {
            throw new IllegalExpressionException("Error: Executed an empty sequence");
        }

        return last;
    }

    @Override
    public SymbolicExpression visit(Conditional n) {
        final double lhs = n.getLhs().accept(this).getValue();
        final double rhs = n.getRhs().accept(this).getValue();

        if (Conditional.compare(n.getComparison(), lhs, rhs)) {
            return n.getThenBranch().accept(this);
        } else {
            return n.getElseBranch().accept(this);
        }
    }
}
