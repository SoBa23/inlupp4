package org.ioopm.calculator.ast;

public class Conditional extends SymbolicExpression {
    private final SymbolicExpression conditionLhs;
    private final SymbolicExpression conditionRhs;
    private final String operator;
    private final SymbolicExpression ifScope;
    private final SymbolicExpression elseScope;

    public Conditional(SymbolicExpression conditionLhs, String operator, SymbolicExpression conditionRhs,
                       SymbolicExpression ifScope, SymbolicExpression elseScope) {
        this.conditionLhs = conditionLhs;
        this.operator = operator;
        this.conditionRhs = conditionRhs;
        this.ifScope = ifScope;
        this.elseScope = elseScope;
    }

    public SymbolicExpression getConditionLhs() {
        return conditionLhs;
    }

    public SymbolicExpression getConditionRhs() {
        return conditionRhs;
    }

    public String getOperator() {
        return operator;
    }

    public SymbolicExpression getIfScope() {
        return ifScope;
    }

    public SymbolicExpression getElseScope() {
        return elseScope;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public String toString() {
        return "if " + conditionLhs + " " + operator + " " + conditionRhs + " " +
               ifScope + " else " + elseScope;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}
