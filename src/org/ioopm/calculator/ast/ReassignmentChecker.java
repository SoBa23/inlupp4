package org.ioopm.calculator.ast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class ReassignmentChecker implements Visitor {
    private Set<String> assignedVariables = new HashSet<>();
    private Set<String> reassignedVariables = new HashSet<>();
    private Deque<Set<String>> scopeStack = new ArrayDeque<>();

    public ReassignmentChecker() {
        scopeStack.push(new HashSet<>());
    }

    public boolean check(SymbolicExpression topLevel) {
        assignedVariables.clear();
        reassignedVariables.clear();
        scopeStack.push(new HashSet<>());
        topLevel.accept(this);
        return reassignedVariables.isEmpty();
    }

    public Set<String> getReassignedVariables() {
        return reassignedVariables;
    }

    @Override
    public SymbolicExpression visit(Addition a) {
        a.getLhs().accept(this);
        a.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Subtraction s) {
        s.getLhs().accept(this);
        s.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Multiplication m) {
        m.getLhs().accept(this);
        m.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Division d) {
        d.getLhs().accept(this);
        d.getRhs().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Assignment a) {
        Variable var = (Variable) a.getRhs(); // Right-hand side is the variable being assigned
        Set<String> currentScope = scopeStack.peek();

        // Check if the variable is already assigned in the current scope
        if (currentScope.contains(var.getName())) {
            reassignedVariables.add(var.getName());
        } else {
            currentScope.add(var.getName()); // Track the variable in the current scope
        }

        // Continue visiting sub-expressions
        a.getLhs().accept(this);
        a.getRhs().accept(this);
        return null;
    }
    

    @Override
    public SymbolicExpression visit(Constant c) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Variable v) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Negation n) {
        n.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Sin s) {
        s.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Cos c) {
        c.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Log l) {
        l.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Exp e) {
        e.getOperand().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Quit q) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Clear c) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Vars v) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Scope s) {
        scopeStack.push(new HashSet<>()); // Enter a new scope
        s.getBody().accept(this); // Visit the body of the scope
        scopeStack.pop(); // Exit the scope
        return null;
    }

    @Override
    public SymbolicExpression visit(Conditional n) {
        // Check condition lhs and rhs
        n.getConditionLhs().accept(this);
        n.getConditionRhs().accept(this);

        // Check "if" scope
        scopeStack.push(new HashSet<>());
        n.getIfScope().accept(this);
        scopeStack.pop();

        // Check "else" scope
        scopeStack.push(new HashSet<>());
        n.getElseScope().accept(this);
        scopeStack.pop();

        return null;
    }

    @Override
    public SymbolicExpression visit(FunctionCall f) {
        for (SymbolicExpression arg : f.getArguments()) {
            arg.accept(this);
        }
        return null;
    }

    @Override
    public SymbolicExpression visit(FunctionDeclaration fd) {
        // Kontrollera funktionskroppen
        Set<String> previousAssignments = new HashSet<>(assignedVariables);
        assignedVariables.clear();
        for (SymbolicExpression expr : fd.getBody()) {
            expr.accept(this);
        }
        assignedVariables = previousAssignments; // Återställ variabler utanför funktionen
        return fd;
    }

    @Override
    public SymbolicExpression visit(Sequence seq) {
        for (SymbolicExpression expr : seq.getExpressions()) {
            expr.accept(this); // Kontrollera varje uttryck
        }
        return seq;
    }

    @Override
    public SymbolicExpression visit(NamedConstant nc) {
        return null;
    }
}
