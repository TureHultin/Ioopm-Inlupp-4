package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;
import org.ioopm.calculator.parser.IllegalExpressionException;

/**
 * A mathematical expression or command
 */
public abstract class SymbolicExpression {
    public final static int MaxPriority = 0x7FFFFFFF;

    /**
     * @return if the node is a command
     */
    public boolean isConstant() {
        return false;
    }

    /**
     * Gets the value a constant
     *
     * @return the value of the constant
     * @throws RuntimeException if the node isn't a Constant
     */
    public double getValue() {
        throw new RuntimeException("Can't get value form non-Constant");
    }

    /**
     * Gets the priority for placing parenthesis when printing
     * i.e. a node with high priority containing
     * low priority branches places parenthesis around the contained node
     *
     * @return the numeric priority
     */
    public abstract int getPriority();

    /**
     * @return if the node is a command
     */
    public boolean isCommand() {
        return false;
    }

    /**
     * Gets the name or operator symbol of the nod
     *
     * @return the "name"
     */
    public String getName() {
        throw new RuntimeException("getName() called on expression with no operator");
    }

    /**
     * Simplifies the expression as much as possible
     *
     * @return the simplified expression
     * @throws IllegalExpressionException when eval-ing a faulty Assignment
     * @throws RuntimeException           when eval-ing a command
     */
    // public abstract SymbolicExpression eval(Environment vars) throws IllegalExpressionException;
    public abstract <T> T accept(Visitor<T> v);
}


