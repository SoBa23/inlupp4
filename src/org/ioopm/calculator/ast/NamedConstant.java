package org.ioopm.calculator.ast;

/**
 * Represents named constants in the calculator (e.g., pi, e).
 * Named constants are immutable and cannot be reassigned.
 */
public class NamedConstant extends Constant {
    private final String name;

    public NamedConstant(String name, double value) {
        super(value);
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}
