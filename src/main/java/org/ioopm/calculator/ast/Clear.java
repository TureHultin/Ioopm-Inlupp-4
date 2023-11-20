package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

/**
 * Command for removing the local variables
 */
public class Clear extends Command {
    private static final Clear theInstance = new Clear();

    private Clear() {
    }

    public static Clear instance() {
        return theInstance;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
