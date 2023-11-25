package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

import java.util.ArrayList;

public class FunctionCall extends SymbolicExpression {
    SymbolicExpression callee;
    ArrayList<SymbolicExpression> arguments;

    public FunctionCall(SymbolicExpression callee, ArrayList<SymbolicExpression> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }

    @Override
    public int getPriority() {
        return SymbolicExpression.MaxPriority;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FunctionCall other) {
            return callee.equals(other.callee) && arguments.equals(other.arguments);
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (callee.getPriority() < getPriority()) {
            sb.append('(').append(callee.toString()).append(')');
        } else {
            sb.append(callee.toString());
        }
        sb.append('(');
        for (int i = 0; i < arguments.size(); i++) {
            SymbolicExpression argument = arguments.get(i);
            sb.append(argument);
            if (i < arguments.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');

        return sb.toString();
    }

    public SymbolicExpression getCallee() {
        return callee;
    }

    public ArrayList<SymbolicExpression> getArguments() {
        return arguments;
    }
}
