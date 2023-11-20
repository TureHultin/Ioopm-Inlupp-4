package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

/**
 * Command for quiting the calculator
 */
public class Quit extends Command {
    private static final Quit theInstance = new Quit();

    private Quit() {
    }

    public static Quit instance() {
        return theInstance;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
