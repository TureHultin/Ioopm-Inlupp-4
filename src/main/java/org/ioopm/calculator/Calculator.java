package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.CalculatorParser;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.ioopm.calculator.parser.SyntaxErrorException;

import java.io.IOException;
import java.io.PrintStream;
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
        Environment vars = new Environment();
        out.println("Welcome to the Symbolic Calculator!");

        boolean running = true;
        while (running) {
            final SymbolicExpression expr;
            out.print("? ");

            try {
                expr = parser.parse(scanner.nextLine(), vars);
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
                Environment savedVars = (Environment) vars.clone();
                try {
                    final SymbolicExpression evaluated = expr.eval(vars);
                    out.println(evaluated);
                    vars.put(new Variable("ans"), evaluated);

                    if (evaluated.isConstant()) {
                        fullyEvaluated += 1;
                    }

                } catch (IllegalExpressionException e) {
                    vars = savedVars;

                    out.println(e.getMessage());
                }
            }

        }

    }

    public static void main(String[] args) throws IOException {
        Calculator calculator = new Calculator();

        calculator.runEventLoop();
        calculator.printStatisics();
    }

    private void printStatisics() {
        System.out.println("Entered Expressions: " + enteredExpressions);
        System.out.println("Fully Evaluated: " + fullyEvaluated);
    }
}
