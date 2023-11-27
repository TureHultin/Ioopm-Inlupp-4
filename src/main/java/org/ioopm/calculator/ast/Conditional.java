package org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.visitor.Visitor;

public class Conditional extends SymbolicExpression {
    public Conditional(SymbolicExpression lhs, Comparison comparison, SymbolicExpression rhs, Scope thenBranch, Scope elseBranch) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.comparison = comparison;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public static boolean compare(Comparison comparison, double lhs, double rhs) {
        return switch (comparison) {
            case Equals -> lhs == rhs;
            case LessThan -> lhs < rhs;
            case LessOrEqualThan -> lhs <= rhs;
            case GreaterThan -> lhs > rhs;
            case GreaterOrEqualThan -> lhs >= rhs;
        };
    }

    public SymbolicExpression getLhs() {
        return lhs;
    }

    public SymbolicExpression getRhs() {
        return rhs;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public Scope getThenBranch() {
        return thenBranch;
    }

    public Scope getElseBranch() {
        return elseBranch;
    }

    public enum Comparison {
        Equals,
        LessThan,
        LessOrEqualThan,
        GreaterThan,
        GreaterOrEqualThan,
    }


    private SymbolicExpression lhs;
    private SymbolicExpression rhs;
    private Comparison comparison;

    private Scope thenBranch;
    private Scope elseBranch;

    static String comparisonToString(Comparison comparison) {
        return switch (comparison) {
            case Equals -> "==";
            case LessThan -> "<";
            case LessOrEqualThan -> "<=";
            case GreaterThan -> ">";
            case GreaterOrEqualThan -> ">=";
        };
    }

    @Override
    public int getPriority() {
        return SymbolicExpression.MaxPriority;
    }

    @Override
    public String toString() {
        return "if " + lhs + " " + comparisonToString(comparison) + " " + rhs + " " + thenBranch + " else " + elseBranch;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Conditional other) {
            return this.lhs.equals(other.lhs)
                    && this.rhs.equals(other.rhs)
                    && this.comparison.equals(other.comparison)
                    && this.thenBranch.equals(other.thenBranch)
                    && this.elseBranch.equals(other.elseBranch);
        }
        return false;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
