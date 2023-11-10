package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.CalculatorParser;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;

public class Tests {
    public static void check(boolean value, String message) {
        if (value) {
            System.out.print("Passed: ");
        } else {
            System.out.print("Error: ");
        }
        System.out.println(message);
    }

    @SuppressWarnings("ThrowablePrintedToSystemOut")
    public static void testParsing(SymbolicExpression expected, String input) {
        CalculatorParser parser = new CalculatorParser();
        try {
            SymbolicExpression parsed = parser.parse(input, null);
            if (parsed.equals(expected)) {
                System.out.println("Passed: '" + input + "' parsed as " + parsed);
            } else {
                System.out.println("Error: expected '" + expected + "' but got '" + parsed + "' on line " + callerLineNumber());
            }
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    public static void testEvaluating(SymbolicExpression expected, SymbolicExpression e) {
        testEvaluating(expected, e, new Environment());
    }

    public static void testEvalCausesException(SymbolicExpression expected) {
        testEvalCausesException(expected, new Environment());
    }

    public static void testEvalCausesException(SymbolicExpression expected, Environment vars) {
        try {
            expected.eval(vars);
            System.out.println("Error: No exception was thrown " + Tests.callerLineNumber());
        } catch (IllegalExpressionException e) {
            System.out.println("Passed: " + expected + " threw " + e);
        }
    }

    public static int callerLineNumber() {
        return Thread.currentThread().getStackTrace()[3].getLineNumber();
    }

    public static void testEvaluating(SymbolicExpression expected, SymbolicExpression e, Environment vars) {
        try {
            SymbolicExpression r = e.eval(vars);

            if (r.equals(expected)) {
                System.out.println("Passed: " + e + " -> " + r);
            } else {
                System.out.println("Error: expected '" + expected + "' but got '" + e + "' on line " + callerLineNumber());
            }

        } catch (IllegalExpressionException exception) {
            System.out.println("Error exception thrown: " + exception.getMessage());
        }


    }

    public static void testPrinting(String expected, SymbolicExpression e) {
        if (expected.equals("" + e)) {
            System.out.println("Passed: " + e);
        } else {
            System.out.println("Error: expected '" + expected + "' but got '" + e + "'");
        }
    }

    public static void main(String[] args) {
        Tests.testPrinting("1.0", new Constant(1));

        Tests.testPrinting("1.0 = y", new Assignment(new Constant(1), new Variable("y")));

        Tests.testPrinting("1.0 * x = y = z", new Assignment(
                new Assignment(
                        new Multiplication(
                                new Constant(1),
                                new Variable("x")),
                        new Variable("y")),
                new Variable("z")));

        Tests.testPrinting("(1.0 + 1.0) * 8.0 - 1.0", new Subtraction(
                new Multiplication(
                        new Addition(
                                new Constant(1),
                                new Constant(1)),
                        new Constant(8)),
                new Constant(1)));


        Tests.testPrinting("sin x", new Sin(new Variable("x")));
        Tests.testPrinting("sin(10.0 * t)",
                new Sin(
                        new Multiplication(
                                new Constant(10),
                                new Variable("t")
                        )
                )
        );
        Tests.testPrinting("1.0 * 2.0 * 3.0", new Multiplication(
                new Multiplication(
                        new Constant(1),
                        new Constant(2)),
                new Constant(3)));
        Tests.testPrinting("1.0 + 1.0 - (1.0 + 1.0)", new Subtraction(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        ));

        Tests.testPrinting("(1.0 + 1.0) * (1.0 + 1.0)", new Multiplication(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        ));

        Tests.testEvaluating(new Constant(4.0), new Multiplication(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        ));
        Tests.testEvaluating(new Constant(15.0), new Subtraction(
                new Multiplication(
                        new Addition(
                                new Constant(1),
                                new Constant(1)),
                        new Constant(8)),
                new Constant(1)));

        Tests.testEvaluating(new Variable("x"), new Variable("x"));
        Tests.testEvaluating(new Constant(2), new Assignment(new Constant(2), new Variable("x")));
        Tests.testEvaluating(new Constant(42), new NamedConstant("Answer", 42));
        Tests.testEvaluating(new Constant(45), new Addition(new Constant(3), new NamedConstant("Answer", 42)));
        {
            Environment vars = new Environment();
            Tests.testEvaluating(new Constant(42), new Assignment(new Addition(new Constant(3), new Constant(39)), new Variable("x")), vars);
            Tests.testEvaluating(new Constant(42), new Variable("x"), vars);
        }

        {
            Environment vars = new Environment();
            Tests.testEvaluating(new Constant(3),
                    new Assignment(
                            new Assignment(new Constant(3),
                                    new Variable("x")),
                            new Variable("y")), vars);
            Tests.testEvaluating(new Constant(3), new Variable("x"), vars);
            Tests.testEvaluating(new Constant(3), new Variable("y"), vars);
        }

        Tests.testParsing(new NamedConstant("Answer", 42), "Answer");
        Tests.testParsing(new Assignment(new Constant(20), new NamedConstant("Answer", 42)), "20 = Answer");
        {
            Environment vars = new Environment();
            Tests.testEvalCausesException(new Assignment(new Constant(20), new NamedConstant("Answer", 42)), vars);
            Tests.check(vars.isEmpty(), "Vars not updated after exception");

            Tests.testEvaluating(new Variable("Answer"), new Variable("Answer"));
        }
    }
}
