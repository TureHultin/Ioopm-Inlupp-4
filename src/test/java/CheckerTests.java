import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.ast.visitor.NamedConstantChecker;
import org.ioopm.calculator.ast.visitor.ReassignmentChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckerTests {
    boolean check_named_constant(SymbolicExpression expr) {
        return new NamedConstantChecker().check(expr);
    }

    boolean check_reassignment(SymbolicExpression expr) {
        return new ReassignmentChecker().check(expr);
    }

    @Test
    void invalidAssignment() {
        SymbolicExpression expr = new Assignment(new Constant(1), new NamedConstant("Q", 1));
        assertFalse(check_named_constant(expr));

    }

    @Test
    void noAssignment() {
        SymbolicExpression expr = new NamedConstant("Q", 1);
        assertTrue(check_named_constant(expr));
        assertTrue(check_reassignment(expr));

    }

    @Test
    void validAssignment() {
        SymbolicExpression expr = new Assignment(
                new Assignment(new Constant(-99), new Variable("Y")),
                new Variable("X"));

        assertTrue(check_named_constant(expr));
        assertTrue(check_reassignment(expr));
    }

    @Test
    void invalidNested() {
        SymbolicExpression expr = new Assignment(
                new Assignment(new Constant(-99), new Variable("X")),
                new Variable("X"));

        assertTrue(check_named_constant(expr));
        assertFalse(check_reassignment(expr));
    }

    @Test
    void invalidSplit() {
        String name = "x";
        SymbolicExpression expr = new Addition(
                new Assignment(new Constant(99), new Variable(name)),
                new Assignment(new Constant(-99), new Variable(name)));

        assertTrue(check_named_constant(expr));
        assertFalse(check_reassignment(expr));
    }

}
