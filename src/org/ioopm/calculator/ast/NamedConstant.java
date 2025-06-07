package org.ioopm.calculator.ast;

/**
 * Represents named constants in the calculator (e.g., pi, e).
 * Named constants are immutable and cannot be reassigned.
 */
public class NamedConstant extends Atom {
    private final double value;
    private final String identifier;

    /**
     * Constructs a NamedConstant with the specified identifier and value.
     *
     * @param identifier the name of the constant
     * @param value the value of the constant
     */
    public NamedConstant(String identifier, double value){
        this.identifier = identifier;
        this.value = value;
    }

    /**
     * Checks if this expression is a constant.
     *
     * @return true, as this is a constant
     */
    @Override
    public boolean isConstant(){
        return true;
    }

    /**
     * Retrieves the value of the constant.
     *
     * @return the value of the constant
     */
    @Override
    public double getValue(){
        return value;
    }

    /**
     * Returns the identifier of the constant as a string.
     *
     * @return the identifier of the constant
     */
    @Override
    public String toString(){
        return identifier;
    }

    /**
     * Checks if this NamedConstant is equal to another object.
     *
     * @param other the object to compare with
     * @return true if the other object is a NamedConstant with the same
     *         identifier and value, false otherwise
     */
    public boolean equals(Object other) {
        if (other instanceof NamedConstant) {
            return this.equals((NamedConstant) other);
        } else {
            return false;
        }
    }

    /**
     * Checks if this NamedConstant is equal to another NamedConstant.
     *
     * @param other the NamedConstant to compare with
     * @return true if the other NamedConstant has the same identifier and value
     */
    private boolean equals(NamedConstant other) {
        return this.value == other.value && this.identifier.equals(other.identifier);
    }

    
    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }
}
