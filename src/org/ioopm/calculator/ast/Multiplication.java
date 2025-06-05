package org.ioopm.calculator.ast;

public class Multiplication extends Binary {
    
    public Multiplication(SymbolicExpression lhs, SymbolicExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public String getName() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public String toString() {
        return "(" + getLhs() + " * " + getRhs() + ")";
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}