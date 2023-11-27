package org.ioopm.calculator.parser;


import org.ioopm.calculator.ast.*;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 * Represents the parsing of strings into valid expressions defined in the AST.
 */
public class CalculatorParser {
    private StreamTokenizer st;
    private static char MULTIPLY = '*';
    private static char ADDITION = '+';
    private static char SUBTRACTION = '-';
    private static char DIVISION = '/';
    private static String NEG = "Neg";
    private static char NEGATION = '-';
    private static String SIN = "Sin";
    private static String COS = "Cos";
    private static String LOG = "Log";
    private static String EXP = "Exp";
    private static char ASSIGNMENT = '=';
    private final static String IF = "if";
    private final static String ELSE = "else";
    private final static String FUNCTION = "function";
    private final static String END = "end";
    private boolean isFunctionParsing = false;
    // unallowerdVars is used to check if variabel name that we
    // want to assign new meaning to is a valid name eg 3 = Quit
    // or 10 + x = L is not allowed
    private final ArrayList<String> unallowedVars = new ArrayList<String>(Arrays.asList("Quit",
            "Vars",
            "Clear",
            SIN,
            COS,
            LOG,
            EXP,
            IF,
            ELSE,
            END,
            FUNCTION));

    /**
     * Used to parse the inputted string by the Calculator program
     *
     * @param scanner where to get the line(s) used to parse
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    public SymbolicExpression parse(Scanner scanner) throws IOException, IllegalExpressionException {
        SymbolicExpression statement = parseLine(scanner.nextLine());
        if (isFunctionParsing()) {
            FunctionDeclaration declaration = (FunctionDeclaration) statement;

            Sequence body = functionBody(scanner);
            declaration.setBody(body);
            return declaration;
        }

        return statement;
    }

    private Sequence functionBody(Scanner scanner) throws IOException {
        Sequence body = new Sequence();

        while (isFunctionParsing()) {
            SymbolicExpression bodyStmt = parseLine(scanner.nextLine());

            if (bodyStmt instanceof FunctionDeclaration nestedDecl) {
                throw new SyntaxErrorException("Error: Can't nest function declaration, trying to nest " + nestedDecl.getName() + " in other function");
            }

            if (!(bodyStmt.equals(End.instance()))) {
                body.add(bodyStmt);
            }
        }
        if (body.getStatements().isEmpty()) {
            throw new IllegalExpressionException("Error: function has no statements");
        }
        return body;
    }

    /**
     * Used to parse the inputted string by the Calculator program
     *
     * @param inputString the string used to parse
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    public SymbolicExpression parseLine(String inputString) throws IOException, IllegalExpressionException {
        this.st = new StreamTokenizer(new StringReader(inputString)); // reads from inputString via stringreader.
        this.st.ordinaryChar('-');
        this.st.ordinaryChar('/');
        this.st.eolIsSignificant(true);
        SymbolicExpression result = statement(); // calls to statement
        return result; // the final result
    }

    /**
     * Checks wether the token read is a command or an assignment
     *
     * @return a SymbolicExpression to be evaluated
     * @throws IOException          by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression
     */
    private SymbolicExpression statement() throws IOException, IllegalExpressionException {
        SymbolicExpression result;
        this.st.nextToken(); //kollar på nästa token som ligger på strömmen
        if (this.st.ttype == this.st.TT_EOF) {
            throw new SyntaxErrorException("Error: Expected an expression");
        }

        if (this.st.ttype == this.st.TT_WORD) { // vilken typ det senaste tecken vi läste in hade.
            if (this.st.sval.equals(FUNCTION)) {
                result = functionDeclaration();
                this.isFunctionParsing = true;
            } else if (this.st.sval.equals("Quit") || this.st.sval.equals("Vars") || this.st.sval.equals("Clear")) { // sval = string Variable
                result = command();
            } else if (this.st.sval.equals("end")) {
                if (!isFunctionParsing) {
                    throw new SyntaxErrorException("Error: end must have a corresponding function declaration");
                }
                isFunctionParsing = false;
                result = command();
            } else {
                result = assignment(); // går vidare med uttrycket.
            }
        } else {
            result = assignment(); // om inte == word, gå till assignment ändå (kan vara tt_number)
        }

        if (this.st.nextToken() != this.st.TT_EOF) { // token should be an end of stream token if we are done
            if (this.st.ttype == this.st.TT_WORD) {
                throw new SyntaxErrorException("Error: Unexpected '" + this.st.sval + "'");
            } else {
                throw new SyntaxErrorException("Error: Unexpected '" + String.valueOf((char) this.st.ttype) + "'");
            }
        }
        return result;
    }

    private String foundToken() {
        return switch (this.st.ttype) {
            case StreamTokenizer.TT_WORD -> "WORD: " + this.st.sval;
            case StreamTokenizer.TT_EOF -> "End of file";
            case StreamTokenizer.TT_EOL -> "End of line";
            case StreamTokenizer.TT_NUMBER -> String.valueOf(this.st.nval);
            default -> String.valueOf((char) this.st.ttype);
        };

    }

    private String nonConstantIdentifier() throws IOException {
        SymbolicExpression nameExpression = identifier();
        if (nameExpression instanceof Variable var) {
            return var.getName();
        } else {
            throw new SyntaxErrorException("Error: Constant '" + nameExpression + "' can't be used as a function name");
        }

    }

    /**
     * Parses a function declaration
     *
     * @return an instance of Quit, Clear or Vars depending on the token parsed
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression functionDeclaration() throws IOException, IllegalExpressionException {
        this.st.nextToken();

        String name = nonConstantIdentifier();

        if (this.st.nextToken() != '(') {
            throw new SyntaxErrorException("expected '(' found " + foundToken());
        }

        ArrayList<String> parameters = new ArrayList<>();

        boolean hasComma = true;
        while (hasComma && this.st.nextToken() != ')') {
            parameters.add(nonConstantIdentifier());
            hasComma = this.st.nextToken() == ',';
        }

        Collection<DuplicatedParametersException.DuplicatedParameter> duplicated =
                DuplicatedParametersException.calculateDuplicated(parameters);

        if (!duplicated.isEmpty()) {
            throw new DuplicatedParametersException(name, duplicated);
        }

        return new FunctionDeclaration(name, parameters);
    }


    /**
     * Checks what kind of command that should be returned
     *
     * @return an instance of Quit, Clear, Vars or End depending on the token parsed
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression command() throws IOException, IllegalExpressionException {
        if (this.st.sval.equals("Quit")) {
            return Quit.instance();
        } else if (this.st.sval.equals("Clear")) {
            return Clear.instance();
        } else if (this.st.sval.equals("end")) {
            return End.instance();
        } else {
            return Vars.instance();
        }
    }


    /**
     * Checks whether the token read is an assignment between 2 expression and
     * descend into the right hand side of '='
     *
     * @return a SymbolicExpression to be evaluated
     * @throws IOException          by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression,
     *                              the variable on rhs of '=' is a number or invalid variable
     */
    private SymbolicExpression assignment() throws IOException, IllegalExpressionException {
        SymbolicExpression result = expression();
        this.st.nextToken();
        while (this.st.ttype == ASSIGNMENT) {
            this.st.nextToken();
            if (this.st.ttype == this.st.TT_NUMBER) {
                throw new SyntaxErrorException("Error: Numbers cannot be used as a variable name");
            } else if (this.st.ttype != this.st.TT_WORD) {
                throw new SyntaxErrorException("Error: Not a valid assignment of a variable"); //this handles faulty inputs after the equal sign eg. 1 = (x etc
            } else {
                if (this.st.sval.equals("ans")) {
                    throw new SyntaxErrorException("Error: ans cannot be redefined");
                }
                SymbolicExpression key = identifier();
                result = new Assignment(result, key);
            }
            this.st.nextToken();
        }
        this.st.pushBack();
        return result;
    }

    /**
     * Check if valid identifier for variable and return that if so
     *
     * @return a SymbolicExpression that is either a named constant or a new variable
     * @throws IOException                by nextToken() if it reads invalid input
     * @throws IllegalExpressionException if you try to redefine a string that isn't allowed
     */
    private SymbolicExpression identifier() throws IOException, IllegalExpressionException {
        SymbolicExpression result;
        if (this.st.ttype != StreamTokenizer.TT_WORD) {
            throw new SyntaxErrorException("Error: Expected identifier, found " + foundToken());
        }
        if (this.unallowedVars.contains(this.st.sval)) {
            throw new IllegalExpressionException("Error: cannot redefine " + this.st.sval);
        }

        if (Constants.namedConstants.containsKey(this.st.sval)) {
            result = new NamedConstant(st.sval, Constants.namedConstants.get(st.sval));
        } else {
            result = new Variable(this.st.sval);
        }
        return result;
    }


    /**
     * Checks wether the token read is an addition or subtraction
     * and then continue on with the right hand side of operator
     *
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression expression() throws IOException, IllegalExpressionException {
        SymbolicExpression result = term();
        this.st.nextToken();
        while (this.st.ttype == ADDITION || this.st.ttype == SUBTRACTION) {
            int operation = st.ttype;
            this.st.nextToken();
            if (operation == ADDITION) {
                result = new Addition(result, term());
            } else {
                result = new Subtraction(result, term());
            }
            this.st.nextToken();
        }
        this.st.pushBack();
        return result;
    }

    /**
     * Checks wether the token read is an Multiplication or
     * Division and then continue on with the right hand side of
     * operator
     *
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression term() throws IOException, IllegalExpressionException {
        SymbolicExpression result = primary();
        this.st.nextToken();
        while (this.st.ttype == MULTIPLY || this.st.ttype == DIVISION) {
            int operation = st.ttype;
            this.st.nextToken();

            if (operation == MULTIPLY) {
                result = new Multiplication(result, primary());
            } else {
                result = new Division(result, primary());
            }
            this.st.nextToken();
        }
        this.st.pushBack();
        return result;
    }

    /**
     * Checks wether the token read is a parantheses and then
     * continue on with the expression inside of it or if the
     * operation is an unary operation and then continue on with
     * the right hand side of that operator else if it's a
     * number/identifier
     *
     * @return a SymbolicExpression to be evaluated
     * @throws IOException          by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression,
     *                              missing right parantheses
     */
    private SymbolicExpression primary() throws IOException, IllegalExpressionException {
        SymbolicExpression result;
        if (this.st.ttype == '(') {
            this.st.nextToken();
            result = assignment();
            /// This captures unbalanced parentheses!
            if (this.st.nextToken() != ')') {
                throw new SyntaxErrorException("Error: Expected ')' found " + foundToken());
            }
        } else if (this.st.ttype == '{') {
            result = scope();
        } else if (this.st.ttype == NEGATION) {
            result = unary();
        } else if (this.st.ttype == this.st.TT_WORD) {
            if (st.sval.equals(IF)) {
                this.st.nextToken();
                SymbolicExpression lhs = expression();
                this.st.nextToken();
                Conditional.Comparison comparison = comparison();

                SymbolicExpression rhs = expression();
                this.st.nextToken();

                Scope thanBranch = scope();
                this.st.nextToken();
                if (this.st.ttype != this.st.TT_WORD || !this.st.sval.equals(ELSE)) {
                    throw new SyntaxErrorException("Error: Expected else found " + foundToken());
                }
                this.st.nextToken();

                Scope elseBranch = scope();

                result = new Conditional(lhs, comparison, rhs, thanBranch, elseBranch);

            } else if (st.sval.equals(SIN) ||
                    st.sval.equals(COS) ||
                    st.sval.equals(EXP) ||
                    st.sval.equals(NEG) ||
                    st.sval.equals(LOG)) {

                result = unary();
            } else {
                result = identifier();
                // We only allow you to call identifiers or function calls
                if (this.st.nextToken() == '(') {
                    while (this.st.ttype == '(') {
                        result = new FunctionCall(result, argumentList());
                        this.st.nextToken(); // Consume ')'
                    }
                }
                st.pushBack();
            }
        } else {
            this.st.pushBack();
            result = number();
        }


        return result;
    }

    private Conditional.Comparison comparison() throws IOException {
        Conditional.Comparison initial = switch (this.st.ttype) {
            case '>' -> Conditional.Comparison.GreaterThan;
            case '<' -> Conditional.Comparison.LessThan;
            case '=' -> Conditional.Comparison.Equals;
            default ->
                    throw new SyntaxErrorException("Error: Expected one of '<' '<=' '>' '>=' '==' found " + foundToken());
        };
        this.st.nextToken();
        if (this.st.ttype == '=') {
            this.st.nextToken();
            return switch (initial) {
                case Equals -> Conditional.Comparison.Equals;
                case LessThan -> Conditional.Comparison.LessOrEqualThan;
                case GreaterThan -> Conditional.Comparison.GreaterOrEqualThan;
                default -> throw new IllegalStateException("Unexpected value: " + initial);
            };
        }

        return initial;
    }

    private Scope scope() throws IOException {
        if (this.st.ttype != '{') {
            throw new SyntaxErrorException("Error: Expected '{' found " + foundToken());
        }

        this.st.nextToken();
        final Scope result = new Scope(assignment());
        /// This captures unbalanced curly brackets!
        if (this.st.nextToken() != '}') {
            throw new SyntaxErrorException("Error: expected '}' found " + foundToken());
        }

        return result;
    }

    private ArrayList<SymbolicExpression> argumentList() throws IOException {
        if (this.st.ttype != '(') {
            throw new RuntimeException("Internal Parser Error: Expected '(' found " + foundToken());
        }
        this.st.nextToken();

        ArrayList<SymbolicExpression> arguments = new ArrayList<>();

        boolean hasComma = true;
        while (hasComma && this.st.ttype != ')') {
            arguments.add(assignment());
            this.st.nextToken(); // done with the assignment;
            hasComma = this.st.ttype == ',';

            if (hasComma) {
                this.st.nextToken();
            }
        }

        if (this.st.ttype != ')') {
            throw new RuntimeException("Parser Error: Consumed to much");
        }


        return arguments;
    }

    /**
     * Checks what type of Unary operation the token read is and
     * then continues with the expression that the operator holds
     *
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression unary() throws IOException, IllegalExpressionException {
        SymbolicExpression result;
        int operationNeg = st.ttype;
        String operation = st.sval;
        this.st.nextToken();
        if (operationNeg == NEGATION || operation.equals(NEG)) {
            result = new Negation(primary());
        } else if (operation.equals(SIN)) {
            result = new Sin(primary());
        } else if (operation.equals(COS)) {
            result = new Cos(primary());
        } else if (operation.equals(LOG)) {
            result = new Log(primary());
        } else {
            result = new Exp(primary());
        }
        return result;
    }

    /**
     * Checks if the token read is a number - should always be a number in this method
     *
     * @return a SymbolicExpression to be evaluated
     * @throws IOException          by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression,
     *                              expected a number which is not present
     */
    private SymbolicExpression number() throws IOException, IllegalExpressionException {
        this.st.nextToken();
        if (this.st.ttype == this.st.TT_NUMBER) {
            return new Constant(this.st.nval);
        } else {
            throw new SyntaxErrorException("Error: Expected number found " + foundToken());
        }
    }

    public boolean isFunctionParsing() {
        return isFunctionParsing;
    }
}