package org.ioopm.calculator.parser;

import org.ioopm.calculator.ast.FunctionDeclaration;

public class WrongArgumentNumberException extends RuntimeException {
    int found;
    FunctionDeclaration declaration;


    public WrongArgumentNumberException(int found, FunctionDeclaration declaration) {
        super("Error: Expected " + declaration.getParameters().size() + " arguments but found " + found
                + "\n it is declared as " + declaration.shortToString());
        this.found = found;
        this.declaration = declaration;
    }
}
