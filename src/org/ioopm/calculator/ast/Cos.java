package org.ioopm.calculator.ast;

public class Cos extends Unary {
    
    public Cos(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "cos(" + getOperand() + ")";
    }

    @Override
    public String getName() {
        return "cos";
    }

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}
