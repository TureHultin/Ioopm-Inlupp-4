import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.ast.visitor.EvaluationVisitor;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluationTests {

    Environment vars;

    @BeforeEach
    void setVars() {
        vars = new Environment();
    }

    SymbolicExpression eval(SymbolicExpression expression) {
        return new EvaluationVisitor(vars).evaluate(expression);
    }

    @Test
    void evaluatesToConstant() throws IllegalExpressionException {
        assertEquals(new Constant(15.0), eval(new Subtraction(
                new Multiplication(
                        new Addition(
                                new Constant(1),
                                new Constant(1)),
                        new Constant(8)),
                new Constant(1))));

        assertEquals(new Constant(4.0), eval(new Multiplication(
                new Addition(new Constant(1), new Constant(1)),
                new Addition(new Constant(1), new Constant(1))
        )));
    }

    @Test
    void notFullyEvaluated() throws IllegalExpressionException {
        assertEquals(new Variable("x"), eval(new Variable("x")));

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
                eval(complex)
        );
    }

    @Test
    void simpleAssignment() throws IllegalExpressionException {
        SymbolicExpression assignment = new Assignment(
                new Addition(new Constant(40), new Constant(2)), new Variable("x"));

        assertEquals(new Constant(42), eval(assignment));
        assertEquals(new Constant(42), eval(new Variable("x")));
    }

    @Test
    void nestedAssignment() throws IllegalExpressionException {

        assertEquals(new Constant(3),
                eval(new Assignment(
                        new Assignment(new Constant(3),
                                new Variable("x")),
                        new Variable("y"))));

        assertEquals(new Constant(3), eval(new Variable("x")));
        assertEquals(new Constant(3), eval(new Variable("y")));
    }

    @Test
    void namedConstantNotReassignable() {
        SymbolicExpression assignment = new Assignment(
                new Constant(20),
                new NamedConstant("Answer", 42));

        Assertions.assertThrowsExactly(
                IllegalExpressionException.class,
                () -> eval(assignment));

        Assertions.assertTrue(vars.isEmpty(), "Vars not updated after exception");

        assertEquals(new Variable("Answer"), eval(new Variable("Answer")));
    }
}
