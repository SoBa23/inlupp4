package org.ioopm.calculator.ast;

import java.util.List;
import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;
import org.ioopm.calculator.ast.Visitor;



public class FunctionDeclaration extends SymbolicExpression{
    private String identifier;
    private List<Variable> parameters;
    private List<SymbolicExpression> body;

    public FunctionDeclaration(String identifier, List<SymbolicExpression> body, List<Variable> parameters){
        this.body = body;
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public String getIdentifier(){
        return identifier;
    }

    public List<SymbolicExpression> getBody() {
        return body;
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public boolean isFunctionDec(){
        return true;
    }

    public void addLine(SymbolicExpression expr){
        body.add(expr);
    }


    @Override
    public SymbolicExpression accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FunctionDeclaration)) {
            return false;
        }
        return this.equals((FunctionDeclaration) other);
    }


    private boolean equals(FunctionDeclaration other) {
        return this.body.equals(other.body) && this.identifier.equals(other.identifier) && this.parameters.equals(other.parameters);
    }

    @Override
    public String toString() {
        StringBuilder parameters = new StringBuilder();
        for (int i = 0; i < this.parameters.size(); i++) {
            parameters.append(this.parameters.get(i));
            if (i < this.parameters.size() - 1) {
                parameters.append(", ");
            }
        }

        StringBuilder bodyBuilder = new StringBuilder();
        for (SymbolicExpression expr : this.body) {
            bodyBuilder.append("\n    ").append(expr.toString());
        }

        return "Function " + identifier + "(" + parameters + ")" + " {" + bodyBuilder.toString() + "\n}";
    }
        
}
