package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Scope extends SymbolicExpression {
    SymbolicExpression exp;

    public SymbolicExpression getExp() {
        return exp;
    }

    public Scope(SymbolicExpression exp) {
        this.exp = exp;
    }
    
    @Override
    public int getPriority() {
        return 1000_000_000;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
