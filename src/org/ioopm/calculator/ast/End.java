package org.ioopm.calculator.ast;

/** Command used internally to mark the end of a function definition. */
public class End extends Command {
    private static final End instance = new End();

    private End() {}

    public static End instance() { return instance; }

    @Override
    public String getName() { return "end"; }

    @Override
    public String toString() { return getName(); }

    @Override
    public boolean isCommand() { return true; }

    @Override
    public SymbolicExpression accept(Visitor v) { return v.visit(this); }
}
