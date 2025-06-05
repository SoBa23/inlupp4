package org.ioopm.calculator.ast;


/**
 * Represents variable assignments in the calculator.
 * Assignments bind a value or expression to a variable.
 */
public class Assignment extends Binary {
    
    public Assignment(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public String getName() {
        return "=";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public String toString() {
        return getLhs() + " " + getName() + " " + getRhs();
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}