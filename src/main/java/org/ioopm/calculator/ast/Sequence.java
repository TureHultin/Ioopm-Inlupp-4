package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

import java.util.ArrayList;
import java.util.Objects;

public class Sequence extends SymbolicExpression {
    private final ArrayList<SymbolicExpression> statements = new ArrayList<>();

    public void add(SymbolicExpression expression) {
        statements.add(expression);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SymbolicExpression statement : statements) {
            sb.append(statement.toString()).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<SymbolicExpression> getStatements() {
        return statements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Sequence sequence) {
            return statements.equals(sequence.statements);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }
}
