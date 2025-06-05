package org.ioopm.calculator.ast;

import java.util.List;

public class FunctionDeclaration extends SymbolicExpression {
    private final String name;
    private final List<String> parameters;
    private final SymbolicExpression body;

    public FunctionDeclaration(String name, List<String> parameters, SymbolicExpression body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public SymbolicExpression getBody() {
        return body;
    }

    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return "function " + name + "(" + String.join(", ", parameters) + ") { ... }";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FunctionDeclaration)) return false;
        FunctionDeclaration that = (FunctionDeclaration) o;
        return this.name.equals(that.name) &&
               this.parameters.equals(that.parameters) &&
               this.body.equals(that.body);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + parameters.hashCode() + body.hashCode();
    }
}
