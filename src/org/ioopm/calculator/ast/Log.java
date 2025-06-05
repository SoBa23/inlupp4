package org.ioopm.calculator.ast;

public class Log extends Unary {
    
    public Log(SymbolicExpression operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "log(" + getOperand() + ")";
    }

    @Override
    public String getName() {
        return "log";
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
