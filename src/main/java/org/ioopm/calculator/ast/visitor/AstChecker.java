package org.ioopm.calculator.ast.visitor;

import org.ioopm.calculator.ast.*;

public abstract class AstChecker implements Visitor<Boolean> {
    public boolean check(SymbolicExpression expr) {
        return expr.accept(this);
    }

    @Override
    public Boolean visit(Addition n) {
        boolean lhsResult = n.getLhs().accept(this);
        boolean rhsResult = n.getRhs().accept(this);
        return lhsResult && rhsResult;
    }

    @Override
    public Boolean visit(Assignment n) {
        boolean lhsResult = n.getLhs().accept(this);
        boolean rhsResult = n.getRhs().accept(this);
        return lhsResult && rhsResult;
    }

    @Override
    public Boolean visit(Clear n) {
        return true;
    }

    @Override
    public Boolean visit(Constant n) {
        return true;
    }

    @Override
    public Boolean visit(Cos n) {
        return n.getRhs().accept(this);
    }

    @Override
    public Boolean visit(Division n) {
        boolean lhsResult = n.getLhs().accept(this);
        boolean rhsResult = n.getRhs().accept(this);
        return lhsResult && rhsResult;
    }

    @Override
    public Boolean visit(Exp n) {
        return n.getRhs().accept(this);
    }

    @Override
    public Boolean visit(Log n) {
        return n.getRhs().accept(this);
    }

    @Override
    public Boolean visit(Multiplication n) {
        boolean lhsResult = n.getLhs().accept(this);
        boolean rhsResult = n.getRhs().accept(this);
        return lhsResult && rhsResult;
    }

    @Override
    public Boolean visit(Negation n) {
        return n.getRhs().accept(this);
    }

    @Override
    public Boolean visit(Quit n) {
        return true;
    }

    @Override
    public Boolean visit(Sin n) {
        return n.getRhs().accept(this);
    }

    @Override
    public Boolean visit(Subtraction n) {
        boolean lhsResult = n.getLhs().accept(this);
        boolean rhsResult = n.getRhs().accept(this);
        return lhsResult && rhsResult;
    }

    @Override
    public Boolean visit(Variable n) {
        return true;
    }

    @Override
    public Boolean visit(Vars n) {
        return true;
    }
}
