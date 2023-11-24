package org.ioopm.calculator.parser;

import java.util.*;

import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;

public class EnvironmentScopes extends Environment {
    Stack<Environment> stack = new Stack<>();

    public EnvironmentScopes() {
        pushEnvironment();
    }

    @Override
    public SymbolicExpression get(Object var) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (stack.get(i).containsKey(var)) {
                return stack.get(i).get(var);
            }
        }
        return null;
    }

    @Override
    public SymbolicExpression put(Variable var, SymbolicExpression exp) {
        return stack.peek().put(var, exp);
    }

    @Override
    public void pushEnvironment() {
        stack.push(new Environment());
    }

    @Override
    public void popEnvironment() {
        stack.pop();
    }

    @Override
    public void clear() {
        stack.clear();
        pushEnvironment();
    }

    public String toString() {
        return stack.peek().toString();
    }
}
