import org.ioopm.calculator.ast.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BasicFunctionalityTests {

    static Stream<SymbolicExpression> mathExpressions() {
        return Stream.of(
                new Addition(new Constant(0), new Constant(0)),
                new Subtraction(new Constant(0), new Constant(0)),
                new Multiplication(new Constant(0), new Constant(0)),
                new Division(new Constant(0), new Constant(0)),
                new Assignment(new Constant(0), new Variable("x")),


                new Cos(new Constant(0)),
                new Sin(new Constant(0)),
                new Exp(new Constant(0)),
                new Log(new Constant(0)),
                new Negation(new Constant(0)),

                new Constant(0),
                new NamedConstant("Banana", 0),
                new Variable("x")
        );
    }

    static Stream<SymbolicExpression> symbolicExpressions() {
        return Stream.concat(mathExpressions(), commands());
    }

    static Stream<Command> commands() {
        return Stream.of(Clear.instance(), Vars.instance(), Quit.instance());
    }

    @ParameterizedTest
    @MethodSource("symbolicExpressions")
    void isConstantEquivalentGetValue(SymbolicExpression expression) {

        if (expression.isConstant()) {
            assertDoesNotThrow(() -> expression.getValue());
        } else {
            //TODO use a more appropriate Exception
            assertThrows(RuntimeException.class, () -> expression.getValue());
        }
    }

    @ParameterizedTest
    @MethodSource("commands")
    void isCommandWorksWithCommands(SymbolicExpression command) {
        assertTrue(command.isCommand());
    }

    @ParameterizedTest
    @MethodSource("mathExpressions")
    void mathExpressionsArentCommands(SymbolicExpression expression) {
        assertFalse(expression.isCommand());
    }

    @Test
    void mulDivPriority() {
        SymbolicExpression div = new Division(new Constant(1), new Constant(1));
        SymbolicExpression mul = new Multiplication(new Constant(1), new Constant(1));
        SymbolicExpression add = new Addition(new Constant(1), new Constant(1));

        assertEquals(div.getPriority(),
                mul.getPriority());
        assertTrue(add.getPriority() < mul.getPriority());
    }

    @Test
    void addSubPriority() {
        SymbolicExpression add = new Addition(new Constant(1), new Constant(1));
        SymbolicExpression sub = new Subtraction(new Constant(1), new Constant(1));
        SymbolicExpression assignment = new Assignment(new Constant(1), new Variable("x"));

        assertEquals(add.getPriority(),
                sub.getPriority());
        assertTrue(assignment.getPriority() < add.getPriority());
    }

    @Test
    void unaryPriority() {
        SymbolicExpression mul = new Multiplication(new Constant(1), new Constant(1));
        SymbolicExpression sin = new Sin(new Constant(1));
        SymbolicExpression cos = new Cos(new Constant(1));
        SymbolicExpression log = new Log(new Constant(1));
        SymbolicExpression exp = new Exp(new Constant(1));
        SymbolicExpression negation = new Negation(new Constant(1));

        assertEquals(sin.getPriority(), cos.getPriority());
        assertEquals(cos.getPriority(), log.getPriority());
        assertEquals(log.getPriority(), exp.getPriority());
        assertEquals(exp.getPriority(), negation.getPriority());

        assertTrue(mul.getPriority() < sin.getPriority());

    }
}
