import org.ioopm.calculator.Tests;
import org.ioopm.calculator.ast.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PrintingTests {
    @Test
    void printingConstant() {
        Tests.testPrinting("0.25", new Constant(0.25));
    }

    @Test
    void printingAssignments() {
        SymbolicExpression normal = new Assignment(new Constant(1), new Variable("y"));
        Assertions.assertEquals("1.0 = y", normal.toString());

        SymbolicExpression nested = new Assignment(
                new Assignment(
                        new Multiplication(
                                new Constant(1),
                                new Variable("x")),
                        new Variable("y")),
                new Variable("z"));

        Assertions.assertEquals("1.0 * x = y = z", nested.toString());
    }

    @Disabled("Just override the printing")
    @Test
    void negationPrintsNice() {
        SymbolicExpression nested = new Negation(new Variable("t"));
        Assertions.assertEquals("-t", nested.toString());
    }

    @Test
    void printingUnary() {
        SymbolicExpression singleSin = new Sin(new Variable("x"));
        Assertions.assertEquals("sin x", singleSin.toString());

        SymbolicExpression complexSin = new Sin(
                new Multiplication(
                        new Constant(10),
                        new Variable("t")
                )
        );
        Assertions.assertEquals("sin(10.0 * t)", complexSin.toString());

        SymbolicExpression nested = new Log(new Exp(new Variable("t")));
        Assertions.assertEquals("log(exp t)", nested.toString());
    }

    @Test
    void printingMultiplePriority() {
        SymbolicExpression expression = new Subtraction(
                new Multiplication(
                        new Addition(
                                new Constant(1),
                                new Constant(1)),
                        new Constant(8)),
                new Constant(1));
        Assertions.assertEquals("(1.0 + 1.0) * 8.0 - 1.0", expression.toString());

        SymbolicExpression parensOnBothSize = new Multiplication(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        );
        Assertions.assertEquals("(1.0 + 1.0) * (1.0 + 1.0)", parensOnBothSize.toString());

    }

    @Disabled("Fix printing first")
    @Test
    void funkyNoncommutativePrinting() {
        SymbolicExpression subtraction = new Subtraction(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        );
        Assertions.assertEquals("1.0 + 1.0 - (1.0 + 1.0)", subtraction.toString());

        SymbolicExpression addition = new Addition(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        );
        Assertions.assertEquals("1.0 + 1.0 + 1.0 + 1.0", addition.toString());

        SymbolicExpression division = new Division(
                new Multiplication(new Constant(1), new Constant(2)),
                new Multiplication(new Constant(3), new Constant(4)));
        Assertions.assertEquals("1.0 * 2.0 / (3.0 * 4.0)", division.toString());

        SymbolicExpression multiplication = new Division(
                new Multiplication(new Constant(1), new Constant(2)),
                new Multiplication(new Constant(3), new Constant(4)));
        Assertions.assertEquals("1.0 * 2.0 * 3.0 * 4.0", multiplication.toString());
    }
}
