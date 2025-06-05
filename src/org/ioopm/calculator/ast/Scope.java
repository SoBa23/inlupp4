package org.ioopm.calculator.ast;


public class Scope extends SymbolicExpression {
    private SymbolicExpression body;

    public Scope(SymbolicExpression body) {
        this.body = body;
    }

    public SymbolicExpression getBody() {
        return body;
    }

    @Override
    public String getName() {
        return "scope";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String toString() {
        return "{ " + body.toString() + " }";
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

}
