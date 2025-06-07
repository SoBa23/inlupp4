package org.ioopm.calculator.ast;

import java.util.HashMap;
import java.util.TreeSet;

public class FuncEnvironment extends HashMap<String, FunctionDeclaration> {
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Functions: \n");
        TreeSet<String> names = new TreeSet<>(this.keySet());
        for (String name : names) {
            sb.append(name);
            sb.append(": ");
            sb.append(this.get(name));
            sb.append("\n");
        }
        return sb.toString();
    }
}
