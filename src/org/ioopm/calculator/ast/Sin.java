package org.ioopm.calculator.ast;

public class Sin extends Unary {
    
    public Sin(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "sin(" + getOperand() + ")";
    }

    @Override
    public String getName() {
        return "sin";
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
