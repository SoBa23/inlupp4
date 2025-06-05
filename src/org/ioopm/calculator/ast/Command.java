package org.ioopm.calculator.ast;

public abstract class Command extends SymbolicExpression {

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
