package org.ioopm.calculator.ast;

import java.util.ArrayList;
import java.util.List;

public class NamedConstantChecker implements Visitor {
    private List<String> illegalAssignments = new ArrayList<>();
    // The checker simply records when an illegal assignment occurs instead of
    // throwing an exception immediately.  This mirrors how the reference tests
    // expect the class to behave.

    public boolean check(SymbolicExpression topLevel) {
        illegalAssignments.clear();
        topLevel.accept(this);
        return illegalAssignments.isEmpty();
    }

    public List<String> getIllegalAssignments() {
        return illegalAssignments;
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
        SymbolicExpression lhs = a.getLhs();
        // Assigning to a NamedConstant is illegal. Instead of throwing an
        // exception directly we record the offence so that `check` simply
        // returns false when such an assignment is encountered.
        if (lhs instanceof NamedConstant) {
            illegalAssignments.add(((NamedConstant) lhs).getName());
            return a;
        }

        a.getRhs().accept(this);
        return a;
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
    public SymbolicExpression visit(End e) {
        return null;
    }

    @Override
    public SymbolicExpression visit(Scope s) {
        s.getBody().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(Conditional c) {
        //check condition lhs and rhs
        c.getConditionLhs().accept(this);
        c.getConditionRhs().accept(this);
        //check the scopes
        c.getIfScope().accept(this);
        c.getElseScope().accept(this);
        return null;
    }

    @Override
    public SymbolicExpression visit(FunctionDeclaration fd) {
        for (SymbolicExpression expr : fd.getBody()) {
            expr.accept(this);
        }
        return fd;
    }

    @Override
    public SymbolicExpression visit(FunctionCall f) {
        for (SymbolicExpression arg : f.getArguments()) {
            arg.accept(this);
        }
        return null;
    }

    @Override
    public SymbolicExpression visit(Sequence seq) {
        for (SymbolicExpression expr : seq.getExpressions()) {
            expr.accept(this); // Kontrollera varje uttryck i sekvensen
        }
        return seq;
    }


    @Override
    public SymbolicExpression visit(NamedConstant nc) {
        // No action needed for named constants in this checker
        return null;
    }
}
