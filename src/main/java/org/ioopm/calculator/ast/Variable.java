package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;

public class Variable extends Atom {
    public Variable(String identifier) {
        this.identifier = identifier;
    }

    private final String identifier;

    @Override
    public String toString() {
        return identifier;
    }


    @Override
    public SymbolicExpression eval(Environment vars) {
        // the nodes are immutable so it's fine to return
        return vars.getOrDefault(this, this);
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
}
