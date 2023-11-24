package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Negation extends Unary {
    public Negation(SymbolicExpression rhs) {
        super(rhs);
    }


    @Override
    public String getName() {
        return "-";
    }

    @Override
    public String toString() {
        String rhs =  getRhs().toString();
        if (getRhs().getPriority() <  getPriority()){
            rhs = '(' + rhs + ')';
        }
        
        return getName() + rhs;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Negation other) {
            return getRhs().equals(other.getRhs());
        }

        return false;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
