package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

import java.util.ArrayList;

public class FunctionDeclaration extends SymbolicExpression {
    private final String name;
    private final ArrayList<String> parameters;
    private Sequence body = new Sequence();

    public FunctionDeclaration(String name, ArrayList<String> parameters) {
        super();
        this.name = name;
        this.parameters = parameters;
    }

    public void addToBody(SymbolicExpression expression) {
        body.add(expression);
    }

    public void setBody(Sequence body) {
        this.body = body;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("function ").append(name);
        sb.append(parametersToString());
        sb.append("\n");
        for (SymbolicExpression statement : body.getStatements()) {
            sb.append(statement).append('\n');
        }
        return sb.append("end").toString();
    }

    private String parametersToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            sb.append(parameters.get(i));
            if (i != parameters.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FunctionDeclaration other) {
            return this.name.equals(other.name)
                    && this.parameters.equals(other.parameters)
                    && this.body.equals(other.body);
        }

        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public Sequence getBody() {
        return body;
    }

    public String shortToString() {
        return "function " + getName() + parametersToString();
    }
}
