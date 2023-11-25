package org.ioopm.calculator.parser;

import org.ioopm.calculator.ast.FunctionDeclaration;
import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class Environment extends HashMap<Variable, SymbolicExpression> {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Variables: ");
        TreeSet<Variable> vars = new TreeSet<>(this.keySet());
        for (Iterator<Variable> iter = vars.iterator(); iter.hasNext();) {
            Variable v = iter.next();

            sb.append(v.getName());
            sb.append(" = ");

            SymbolicExpression varContent = this.get(v);
            if (varContent instanceof FunctionDeclaration f) {
                sb.append(f.shortToString());
            } else {
                sb.append(varContent);
            }

            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public void pushEnvironment() {

    }

    public void popEnvironment() {

    }
}
