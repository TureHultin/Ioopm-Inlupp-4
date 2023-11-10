package org.ioopm.calculator.ast;

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
}
