package org.ioopm.calculator.ast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * The Environment class manages variable bindings for the calculator.
 * Acts as a hashmap between Variable objects and their corresponding SymbolicExpressions.
 */
public class Environment extends HashMap<Variable, SymbolicExpression> {
    private FuncEnvironment functions;
    
    public Environment() {
        super();
    }

    public Environment(Environment other) {
        super(other);
    }

    public FuncEnvironment getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "No variables defined";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Variables: ");

        TreeSet<Variable> vars = new TreeSet<>(this.keySet());
        Iterator<Variable> iter = vars.iterator();

        while (iter.hasNext()) {
            Variable v = iter.next();
            sb.append(v.getName());
            sb.append(" = ");
            sb.append(this.get(v));

            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public SymbolicExpression get(Variable key) {
        return super.get(key);
    }

    public void putFunction(String name, FunctionDeclaration fd) {
        this.put(new Variable(name), fd);
    }

    public FunctionDeclaration getFunction(String functionName) {
        SymbolicExpression expr = this.get(new Variable(functionName));
        if (expr instanceof FunctionDeclaration) {
            return (FunctionDeclaration) expr;
        }
        return null;
    }
}
