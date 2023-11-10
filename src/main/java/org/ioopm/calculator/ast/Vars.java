package org.ioopm.calculator.ast;

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
}
