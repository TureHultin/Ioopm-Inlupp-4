package org.ioopm.calculator.parser;
import java.util.*;

import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;

public class EnvironmentScopes extends Environment {
    Stack<Environment> stack = new Stack<>();
 
    public SymbolicExpression get(Object var) {
        Iterator<Environment> iter = stack.iterator();
        while (iter.hasNext()) {
            Environment env = iter.next();
            if (env.containsKey(var)) {
                return env.get(var);
            }
        }
        return stack.peek().get(var);
    }

    public SymbolicExpression put(Variable var, SymbolicExpression exp) {
        return stack.peek().put(var, exp);
    }
    
    public void pushEnvironment() {
        stack.push(new Environment());
    }

    public Environment popEnvironment() {
        return stack.pop();
    }
}
