package org.ioopm.calculator.ast;

/**
 * Represents the 'Vars' command which lists all variables.
 */
public class Vars extends Command {
    private static final Vars theInstance = new Vars();

    private Vars() {}

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    public String getName() {
        return "vars";
    }

    public String toString() {
        return this.getName();
    }

    public static Vars instance() {
        return theInstance;
    }

    @Override
    public boolean isCommand() {
        return true;
    }
}