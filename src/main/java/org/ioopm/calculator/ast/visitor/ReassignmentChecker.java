package org.ioopm.calculator.ast.visitor;

import org.ioopm.calculator.ast.Assignment;
import org.ioopm.calculator.ast.FunctionDeclaration;
import org.ioopm.calculator.ast.Scope;
import org.ioopm.calculator.ast.Variable;

import java.util.HashSet;
import java.util.Stack;

public class ReassignmentChecker extends AstChecker {
    private final Stack<HashSet<Variable>> boundVariables = new Stack<>();
    private final HashSet<Variable> reassignedVariables = new HashSet<>();

    public ReassignmentChecker() {
        boundVariables.push(new HashSet<>());
    }

    @Override
    public Boolean visit(Assignment n) {
        boolean otherResults = super.visit(n);
        Variable assignmentTarget = (Variable) n.getRhs();

        if (boundVariables.peek().contains(assignmentTarget)) {
            reassignedVariables.add(assignmentTarget);
            return false;
        }

        boundVariables.peek().add(assignmentTarget);
        return otherResults;

    }

    @Override
    public Boolean visit(Scope n) {
        boundVariables.push(new HashSet<>());
        Boolean result = super.visit(n);
        boundVariables.pop();
        return result;
    }

    @Override
    public Boolean visit(FunctionDeclaration n) {
        ReassignmentChecker innerChecker = new ReassignmentChecker();

        innerChecker.boundVariables.peek().add(new Variable(n.getName()));
        for (String parameter : n.getParameters()) {
            innerChecker.boundVariables.peek().add(new Variable(parameter));
        }

        return innerChecker.check(n.getBody());
    }

    public HashSet<Variable> getReassignedVariables() {
        return reassignedVariables;
    }
}
