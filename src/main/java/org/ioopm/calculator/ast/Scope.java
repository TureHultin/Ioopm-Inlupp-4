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
    public String toString() {
        return "{ " + this.exp + " }";
    }

    @Override
    public int getPriority() {
        return 1000_000_000;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Scope other) {
            return this.exp.equals(other.exp);
        }
        return false;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
