package org.ioopm.calculator.ast;

import org.ioopm.calculator.parser.Environment;

public abstract class Command extends SymbolicExpression {
    @Override
    public int getPriority() {
        throw new RuntimeException("Tried to print command as part of an mathematical expression");
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public SymbolicExpression eval(Environment vars) {
        throw new RuntimeException("Can't eval() commands");
    }

    @Override
    public boolean equals(Object obj) {
        // They are Singleton types so are object identical
        return this == obj;
    }
}
