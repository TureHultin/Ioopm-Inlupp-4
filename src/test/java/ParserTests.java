import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.CalculatorParser;
import org.ioopm.calculator.parser.DuplicatedParametersException;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.ioopm.calculator.parser.SyntaxErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {
    CalculatorParser parser;

    void assertRoundtrip(SymbolicExpression tree) throws IOException {
        SymbolicExpression parsed = parser.parse(new Scanner(tree.toString()));
        assertEquals(tree, parsed);
    }

    @BeforeEach
    void initParser() {
        parser = new CalculatorParser();
    }

    @Test
    void constant() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Constant(12);
        String string = tree.toString();
        SymbolicExpression parseResult = parser.parseLine(string);
        assertEquals(tree, parseResult);
    }


    @Test
    void arithmatic() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Multiplication(
                new Addition(new Constant(12), new Variable("Q")),
                new Constant(100));
        String string = tree.toString();
        SymbolicExpression parseResult = parser.parseLine(string);
        assertEquals(tree, parseResult);
    }

    @Test
    void funkyParens() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Subtraction(
                new Constant(100),
                new Addition(new Constant(12), new Variable("Q"))
        );

        String string = tree.toString();
        SymbolicExpression parseResult = parser.parseLine(string);
        assertEquals(tree, parseResult);
    }

    @Test
    void nestedNegation() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Negation(new Negation(new Negation(new Variable("x"))));

        String string = tree.toString();
        SymbolicExpression parseResult = parser.parseLine(string);
        assertEquals(tree, parseResult);

    }

    static Stream<SymbolicExpression> namedUnaries() {
        return Stream.of(new Sin(new Constant(1)),
                new Cos(new Constant(1)),
                new Log(new Constant(1)),
                new Exp(new Constant(1)));
    }

    @ParameterizedTest
    @MethodSource("namedUnaries")
    void namedUnary(SymbolicExpression tree) throws IOException, IllegalExpressionException {
        String string = tree.toString();
        SymbolicExpression parseResult = parser.parseLine(string);
        assertEquals(tree, parseResult);

    }

    @Test
    void CantUseNameConstantAsParameter() {
        assertThrowsExactly(SyntaxErrorException.class, () -> parser.parse(new Scanner("function bla(pi)\nend")));
    }

    @Test
    void CantUseNamedConstantAsName() {
        assertThrowsExactly(SyntaxErrorException.class, () -> parser.parse(new Scanner("function L(a)\nend")));
    }

    @Test
    void parsesAllLinesInFunction() throws IOException {
        ArrayList<String> params = new ArrayList<>();
        params.add("a");
        params.add("b");
        params.add("q");
        FunctionDeclaration expected = new FunctionDeclaration("func", params);
        expected.addToBody(new Assignment(new Constant(1), new Variable("y")));
        expected.addToBody(new Variable("y"));
        String function = "function func(a, b, q)\n1 = y\ny\n end";

        FunctionDeclaration declaration = (FunctionDeclaration) parser.parse(new Scanner(function));
        assertEquals(expected, declaration);
    }

    @Test
    void denyRepeatedParameters() {
        assertThrowsExactly(DuplicatedParametersException.class,
                () -> parser.parseLine("function func(a, a, a)"));
    }

    @Test
    void functionRoundtrip() throws IOException {
        ArrayList<String> params = new ArrayList<>();
        params.add("a");
        params.add("b");
        params.add("c");
        FunctionDeclaration tree = new FunctionDeclaration("TESTFUNC", params);
        tree.addToBody(new Assignment(new Constant(41), new Variable("X")));
        tree.addToBody(new Multiplication(new Variable("X"), new Constant(1.0 / 2.0)));

        SymbolicExpression parsed = parser.parse(new Scanner(tree.toString()));
        assertEquals(tree, parsed);
    }

    @Test
    void unbalancedbracesFail() {
        assertThrowsExactly(SyntaxErrorException.class, () -> parser.parseLine("(1 = y"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"foo()", "bar(1, 1, 1)", "baz(a)", "baz()(c)"})
    void canParseCall(String functionCall) throws IOException {
        SymbolicExpression parsed = parser.parseLine(functionCall);

        assertInstanceOf(FunctionCall.class, parsed);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{foo}()", "{foo = x}(1, 1, 1)", "(baz)(a)", "bar("})
    void cantParseWeirdCalls(String functionCall) throws IOException {
        assertThrows(SyntaxErrorException.class, () -> parser.parseLine(functionCall));
    }

    @Test
    void conditionalRoundtrip() throws IOException {
        Scope thanBranch = new Scope(new Constant(10));
        Scope elseBranch = new Scope(new Variable("x"));

        SymbolicExpression tree = new Conditional(new Constant(1), Conditional.Comparison.Equals, new Variable("x"), thanBranch, elseBranch);

        assertRoundtrip(tree);
    }

    private FunctionCall fnCall(SymbolicExpression callee, double arg) {
        ArrayList<SymbolicExpression> arguments = new ArrayList<>();
        arguments.add(new Constant(arg));
        return new FunctionCall(callee, arguments);
    }

    @Test
    void canParseNested() throws IOException {
        SymbolicExpression parsed = parser.parseLine("bar(1)(2)(3)(4)");
        SymbolicExpression bar = new Variable("bar");
        assertEquals(fnCall(fnCall(fnCall(fnCall(bar, 1), 2), 3), 4), parsed);
    }

    @Test
    void dontAcceptEmptyFunctionBodies() {
        String function = "function func(a, b, q)\n end";
        assertThrowsExactly(IllegalExpressionException.class, () -> parser.parse(new Scanner(function)));
    }
}
