package org.ioopm.calculator.ast.visitor;

import org.ioopm.calculator.ast.*;

public interface Visitor<T> {
    public T visit(Addition n);

    public T visit(Assignment n);

    public T visit(Clear n);

    public T visit(Constant n);

    public T visit(Cos n);

    public T visit(Division n);

    public T visit(Exp n);

    public T visit(Log n);

    public T visit(Multiplication n);

    public T visit(Negation n);

    public T visit(Quit n);

    public T visit(Sin n);

    public T visit(Subtraction n);

    public T visit(Variable n);

    public T visit(Vars n);

    public T visit(Scope n);
}
