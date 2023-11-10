package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.CalculatorParser;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.ioopm.calculator.parser.SyntaxErrorException;

import java.io.IOException;
import java.util.Scanner;

public class Calculator {
    int fullyEvaluated = 0;
    int enteredExpressions = 0;

    void runEventLoop(Scanner scanner) throws IOException {
        CalculatorParser parser = new CalculatorParser();
        Environment vars = new Environment();
        System.out.println("Welcome to the Symbolic Calculator!");

        boolean running = true;
        while (running) {
            final SymbolicExpression expr;
            System.out.print("? ");

            try {
                expr = parser.parse(scanner.nextLine(), vars);
            } catch (SyntaxErrorException | IllegalExpressionException exception) {
                System.out.println(exception.getMessage());
                continue;
            }


            if (expr.isCommand()) {
                if (expr.equals(Clear.instance())) {
                    vars.clear();
                } else if (expr.equals(Quit.instance())) {
                    running = false;
                } else if (expr.equals(Vars.instance())) {
                    System.out.println(vars);
                } else {
                    System.out.println("Not an implemented command");
                }
            } else {
                enteredExpressions += 1;
                Environment savedVars = (Environment) vars.clone();
                try {
                    final SymbolicExpression evaluated = expr.eval(vars);
                    System.out.println(evaluated);
                    vars.put(new Variable("ans"), evaluated);

                    if (evaluated.isConstant()) {
                        fullyEvaluated += 1;
                    }

                } catch (IllegalExpressionException e) {
                    vars = savedVars;

                    System.out.println(e.getMessage());
                }
            }

        }

    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        calculator.runEventLoop(scanner);
        calculator.printStatisics();
    }

    private void printStatisics() {
        System.out.println("Entered Expressions: " + enteredExpressions);
        System.out.println("Fully Evaluated: " + fullyEvaluated);
    }
}
