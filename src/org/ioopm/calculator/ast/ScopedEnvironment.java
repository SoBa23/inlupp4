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
    public SymbolicExpression put(Variable key, SymbolicExpression value) {
        return scopeStack.peek().put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ScopedEnvironment:\n");
        for (Environment env : scopeStack) {
            sb.append(env.toString()).append("\n");
        }
        return sb.toString();
    }

    public Environment topEnvironment() {
        return scopeStack.peek();
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
