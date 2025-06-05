package org.ioopm.calculator.ast;

import java.util.List;

public class Sequence extends SymbolicExpression {
    private final List<SymbolicExpression> expressions;

    public Sequence(List<SymbolicExpression> expressions) {
        this.expressions = expressions;
    }

    public List<SymbolicExpression> getExpressions() {
        return expressions;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ ");
        for (SymbolicExpression expr : expressions) {
            sb.append(expr.toString()).append("; ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sequence)) return false;
        Sequence that = (Sequence) o;
        return this.expressions.equals(that.expressions);
    }

    @Override
    public int hashCode() {
        return expressions.hashCode();
    }
}
