package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

/**
 * Command for printing the local variables
 */
public class Vars extends Command {
    private static final Vars theInstance = new Vars();

    private Vars() {
    }

    public static Vars instance() {
        return theInstance;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
