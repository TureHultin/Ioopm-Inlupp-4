package org.ioopm.calculator.ast.visitor;

import org.ioopm.calculator.ast.Assignment;
import org.ioopm.calculator.ast.Variable;

import java.util.HashSet;

public class ReassignmentChecker extends AstChecker {
    private final HashSet<Variable> boundVariables = new HashSet<>();
    private final HashSet<Variable> reassignedVariables = new HashSet<>();

    @Override
    public Boolean visit(Assignment n) {
        boolean otherResults = super.visit(n);
        Variable assignmentTarget = (Variable) n.getRhs();

        if (boundVariables.contains(assignmentTarget)) {
            reassignedVariables.add(assignmentTarget);
            return false;
        }

        boundVariables.add(assignmentTarget);
        return otherResults;


    }

    public HashSet<Variable> getReassignedVariables() {
        return reassignedVariables;
    }
}
