package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class End extends Command {
    private static final End theInstance = new End();

    private End() {
    }

    public static End instance() {
        return theInstance;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        //return v.visit(this);
        // TODO: add to visitor 
        return null;
    }
}
