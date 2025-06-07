package org.ioopm.calculator.ast;

import java.util.ArrayDeque;
import java.util.Deque;


public class ScopedEnvironment extends Environment {
    private Deque<Environment> scopeStack = new ArrayDeque<>();

    public ScopedEnvironment() {
        scopeStack.push(new Environment());
    }

    public void pushEnvironment() {
        scopeStack.push(new Environment());
    }

    public void popEnvironment() {
        if (scopeStack.size() > 1) {
            scopeStack.pop();
        } else {
            throw new IllegalStateException("Cannot pop the global scope");
        }
    }

    @Override
    public SymbolicExpression get(Variable key) {
        for (Environment env : scopeStack) {
            if (env.containsKey(key)) {
                return env.get(key);
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Variable)) {
            return false;
        }
        Variable var = (Variable) key;
        for (Environment env : scopeStack) {
            if (env.containsKey(var)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SymbolicExpression put(Variable key, SymbolicExpression value) {
        return scopeStack.peek().put(key, value);
    }

    @Override
    public void clear() {
        // Clear variables in all scopes to mimic clearing the environment
        for (Environment env : scopeStack) {
            env.clear();
        }
    }

    @Override
    public String toString() {
        // Present the variables visible in the current (top) scope
        // in the same style as the plain Environment implementation
        return scopeStack.peek().toString();
    }

    public Environment topEnvironment() {
        return scopeStack.peek();
    }

    @Override
    public FuncEnvironment getFunctions() {
        return topEnvironment().getFunctions();
    }

    @Override
    public void putFunction(String name, FunctionDeclaration fd) {
        scopeStack.peek().putFunction(name, fd);
    }

    @Override
    public FunctionDeclaration getFunction(String functionName) {
        for (Environment env : scopeStack) {
            FunctionDeclaration fd = env.getFunction(functionName);
            if (fd != null) {
                return fd;
            }
        }
        return null;
    }
}
