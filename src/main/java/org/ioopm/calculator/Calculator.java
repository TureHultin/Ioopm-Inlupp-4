package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.ast.visitor.EvaluationVisitor;
import org.ioopm.calculator.ast.visitor.NamedConstantChecker;
import org.ioopm.calculator.ast.visitor.ReassignmentChecker;
import org.ioopm.calculator.parser.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Calculator {
    int fullyEvaluated = 0;
    int enteredExpressions = 0;

    PrintStream out;
    Scanner scanner;

    public Calculator() {
        this.scanner = new Scanner(System.in);
        this.out = System.out;
    }

    public Calculator(Scanner scanner, PrintStream out) {
        this.out = out;
        this.scanner = scanner;
    }

    public void runEventLoop() throws IOException {
        CalculatorParser parser = new CalculatorParser();
        EnvironmentScopes vars = new EnvironmentScopes();
        vars.pushEnvironment();
        out.println("Welcome to the Symbolic Calculator!");

        boolean running = true;
        while (running) {
            final SymbolicExpression expr;
            out.print("? ");
            try {
                expr = parser.parse(scanner);
            } catch (SyntaxErrorException | IllegalExpressionException exception) {
                out.println(exception.getMessage());
                continue;
            }

            if (expr.isCommand()) {
                if (expr.equals(Clear.instance())) {
                    vars.clear();
                } else if (expr.equals(Quit.instance())) {
                    running = false;
                } else if (expr.equals(Vars.instance())) {
                    out.println(vars);
                } else {
                    out.println("Not an implemented command");
                }
            } else {
                enteredExpressions += 1;
                NamedConstantChecker namedConstantChecker = new NamedConstantChecker();
                if (!namedConstantChecker.check(expr)) {
                    printNamedConstantError(namedConstantChecker);
                    continue;
                }

                ReassignmentChecker reassignmentChecker = new ReassignmentChecker();
                if (!reassignmentChecker.check(expr)) {
                    printReassignmentError(reassignmentChecker);
                    continue;
                }

                SymbolicExpression evaluated = null;

                try {
                    evaluated = new EvaluationVisitor(vars).evaluate(expr);
                } catch (WrongArgumentNumberException | IllegalExpressionException e) {
                    out.println(e.getMessage());

                }

                if (evaluated != null) {
                    out.println(evaluated);

                    vars.put(new Variable("ans"), evaluated);

                    if (evaluated.isConstant()) {
                        fullyEvaluated += 1;
                    }
                }
            }
        }
        printStatisics();
    }

    private void printNamedConstantError(NamedConstantChecker namedConstantChecker) {
        out.println("Error: Can not reassign named constants");

        for (Assignment assignment : namedConstantChecker.getWrongAssignments()) {
            out.println("    " + assignment);
        }
    }

    private void printReassignmentError(ReassignmentChecker reassignmentChecker) {
        HashSet<Variable> reassignedVariables = reassignmentChecker.getReassignedVariables();
        out.print("Error: Can't reassign the variable");
        if (reassignedVariables.size() != 1) {
            out.print("s");
        }
        out.print(" ");
        for (Iterator<Variable> iterator = reassignedVariables.iterator(); iterator.hasNext(); ) {
            Variable v = iterator.next();
            out.print(v.toString());

            if (iterator.hasNext()) {
                out.print(", ");
            }
        }

        out.println(" more than once");
    }

    public static void main(String[] args) throws IOException {
        Calculator calculator = new Calculator();

        calculator.runEventLoop();
    }

    private void printStatisics() {
        out.println("Entered Expressions: " + enteredExpressions);
        out.println("Fully Evaluated: " + fullyEvaluated);
    }
}
