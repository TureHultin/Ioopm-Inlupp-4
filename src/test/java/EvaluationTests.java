import org.ioopm.calculator.Tests;
import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluationTests {

    Environment vars;

    @BeforeEach
    void setVars() {
        vars = new Environment();
    }


    @Test
    void evaluatesToConstant() throws IllegalExpressionException {
        assertEquals(new Constant(15.0), new Subtraction(
                new Multiplication(
                        new Addition(
                                new Constant(1),
                                new Constant(1)),
                        new Constant(8)),
                new Constant(1)).eval(vars));
        assertEquals(new Constant(4.0), new Multiplication(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        ).eval(vars));
    }

    @Disabled("Binary helper is miss implemented")
    @Test
    void notFullyEvaluated() throws IllegalExpressionException {
        assertEquals(new Variable("x"), new Variable("x").eval(vars));

        SymbolicExpression complex = new Subtraction(
                new Multiplication(
                        new Addition(
                                new Constant(10),
                                new Constant(2)),
                        new Constant(2)),
                new Variable("x"));

        assertEquals(new Subtraction(
                        new Constant(24),
                        new Variable("x")),
                complex.eval(vars));
    }

    @Test
    void simpleAssignment() throws IllegalExpressionException {
        SymbolicExpression assignment = new Assignment(
                new Addition(new Constant(40), new Constant(2)), new Variable("x"));

        assertEquals(new Constant(42), assignment.eval(vars));
        assertEquals(new Constant(42), new Variable("x").eval(vars));
    }

    @Test
    void nestedAssignment() throws IllegalExpressionException {

        assertEquals(new Constant(3),
                new Assignment(
                        new Assignment(new Constant(3),
                                new Variable("x")),
                        new Variable("y")).eval(vars));

        assertEquals(new Constant(3), new Variable("x").eval(vars));
        assertEquals(new Constant(3), new Variable("y").eval(vars));
    }

    @Test
    void namedConstantNotReassignable() {
        SymbolicExpression assignment = new Assignment(
                new Constant(20),
                new NamedConstant("Answer", 42));

        Assertions.assertThrowsExactly(
                IllegalExpressionException.class,
                () -> assignment.eval(vars));

        Assertions.assertTrue(vars.isEmpty(), "Vars not updated after exception");

        assertEquals(new Variable("Answer"), new Variable("Answer").eval(vars));
    }
}
