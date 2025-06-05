package org.ioopm.calculator.ast;

import java.util.List;

public class FunctionCall extends SymbolicExpression {
    private final String functionName;
    private final List<SymbolicExpression> arguments;

    public FunctionCall(String functionName, List<SymbolicExpression> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<SymbolicExpression> getArguments() {
        return arguments;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return functionName + "(" + arguments.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FunctionCall)) return false;
        FunctionCall that = (FunctionCall) o;
        return this.functionName.equals(that.functionName) &&
               this.arguments.equals(that.arguments);
    }

    @Override
    public int hashCode() {
        return functionName.hashCode() + arguments.hashCode();
    }
}
