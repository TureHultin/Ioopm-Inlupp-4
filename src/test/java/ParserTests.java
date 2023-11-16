import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.CalculatorParser;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTests {
    CalculatorParser parser;

    @BeforeEach
    void initParser() {
        parser = new CalculatorParser();
    }

    @Test
    void constant() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Constant(12);
        String string = tree.toString();
        SymbolicExpression parseResult = parser.parse(string, new Environment());
        assertEquals(tree, parseResult);
    }


    @Test
    void arithmatic() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Multiplication(
                new Addition(new Constant(12), new Variable("Q")),
                new Constant(100));
        String string = tree.toString();
        SymbolicExpression parseResult = parser.parse(string, new Environment());
        assertEquals(tree, parseResult);
    }

    @Test
    void funkyParens() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Subtraction(
                new Constant(100),
                new Addition(new Constant(12), new Variable("Q"))
        );

        String string = tree.toString();
        SymbolicExpression parseResult = parser.parse(string, new Environment());
        assertEquals(tree, parseResult);
    }

    @Test
    void nestedNegation() throws IOException, IllegalExpressionException {
        SymbolicExpression tree = new Negation(new Negation(new Negation(new Variable("x"))));

        String string = tree.toString();
        SymbolicExpression parseResult = parser.parse(string, new Environment());
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
        SymbolicExpression parseResult = parser.parse(string, new Environment());
        assertEquals(tree, parseResult);

    }

}
