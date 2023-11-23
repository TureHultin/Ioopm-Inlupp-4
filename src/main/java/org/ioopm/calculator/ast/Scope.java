package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;
import org.ioopm.calculator.parser.Environment;

public class Scope extends SymbolicExpression {
    SymbolicExpression exp;

    public Scope(SymbolicExpression exp) {
        this.exp =exp;
    }
    
    @Override
    public int getPriority() {
        return 1000_000_000;
    }

    @Override
    public SymbolicExpression eval(Environment vars) {
        return null;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
