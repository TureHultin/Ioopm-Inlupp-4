package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Variable extends Atom implements Comparable<Variable> {
    public Variable(String identifier) {
        this.identifier = identifier;
    }

    private final String identifier;

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable other) {
            return identifier.equals(other.identifier);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }


    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

    @Override
    public int compareTo(Variable o) {
        return this.identifier.compareTo(o.identifier);
    }
}
