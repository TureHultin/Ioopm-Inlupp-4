import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.CalculatorParser;
import org.ioopm.calculator.parser.Environment;
import org.ioopm.calculator.parser.IllegalExpressionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
    
}
