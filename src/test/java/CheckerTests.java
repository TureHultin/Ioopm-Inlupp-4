import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.ast.visitor.NamedConstantChecker;
import org.ioopm.calculator.ast.visitor.ReassignmentChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

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


    static <T> ArrayList<T> params(T... params) {
        ArrayList<T> p = new ArrayList<>();
        Collections.addAll(p, params);
        return p;
    }

    static FunctionDeclaration funcDecl(String name, ArrayList<String> params, SymbolicExpression... statements) {
        FunctionDeclaration f = new FunctionDeclaration(name, params);
        for (SymbolicExpression statement : statements) {
            f.addToBody(statement);
        }
        return f;
    }

    static Stream<SymbolicExpression> failingReassignment() {
        return Stream.of(
                CheckerTests.funcDecl("foo", CheckerTests.params(),
                        new Assignment(new Constant(1), new Variable("foo"))),
                CheckerTests.funcDecl("foo", CheckerTests.params("bar"),
                        new Assignment(new Constant(1), new Variable("bar"))),
                CheckerTests.funcDecl("calc", CheckerTests.params(),
                        new Assignment(new Constant(1), new Variable("bar")),
                        new Assignment(new Constant(2), new Variable("bar")),
                        new Assignment(new Constant(2), new Variable("baz"))
                ),

                new FunctionCall(new Variable("foo"), CheckerTests.params(
                        new Assignment(new Constant(5), new Variable("x")),
                        new Assignment(new Constant(1), new Variable("x"))
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("failingReassignment")
    void dontAllowReassignment(SymbolicExpression expression) {
        assertFalse(new ReassignmentChecker().check(expression));
    }

    @Test
    void DontAllowNamedConstantReassignmenInFunc() {
        FunctionDeclaration declaration = funcDecl("foo", params("bla"),
                new Assignment(new Variable("bla"), new NamedConstant("pi", 4)));
        assertFalse(new NamedConstantChecker().check(declaration));
    }
}
