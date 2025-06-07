package org.ioopm.calculator.ast;

public class Constant extends Atom {
    private double value;

    public Constant(double value) {
        this.value = value;
    }
    
    @Override
    public boolean isConstant(){
        return true;
    }

    @Override
    public double getValue(){
        return value;
    }

    @Override
    public String toString(){
        if(value == (int)value){
            // för att det bara ska bli t.ex 2 istället för 2.0
            return String.valueOf((int)value);
        }
        return String.valueOf(value);
    }

    // equal function
    public boolean equals(Object other) {
        if (other instanceof Constant) {
            return this.equals((Constant) other);
        } else {
            return false;
        }
    }
    
    private boolean equals(Constant other) {
        /// access a private field of other!
        return this.value == other.value;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}
