package org.ioopm.calculator.ast;

import java.util.List;

import org.ioopm.calculator.parser.IllegalAssignmentException;

public class EvaluationVisitor implements Visitor {
    private ScopedEnvironment env = null;

    public SymbolicExpression evaluate(SymbolicExpression topLevel, Environment env) {
        if (env instanceof ScopedEnvironment) {
            this.env = (ScopedEnvironment) env;
        } else {
            this.env = new ScopedEnvironment();
            this.env.putAll(env);
        }
        return topLevel.accept(this);
    }

    @Override
    public SymbolicExpression visit(Addition n) {
        SymbolicExpression left = n.getLhs().accept(this);
        SymbolicExpression right = n.getRhs().accept(this);

        if (left.isConstant() && right.isConstant()) {
            return new Constant(left.getValue() + right.getValue());
        }
        return new Addition(left, right);
    }

    @Override
    public SymbolicExpression visit(Assignment n) {
        SymbolicExpression lhsValue = n.getLhs().accept(this);

        if (!(n.getRhs() instanceof Variable)) {
            throw new RuntimeException("The right-hand side of an assignment must be a variable");
        }
        if (lhsValue instanceof NamedConstant) {
            NamedConstant nc = (NamedConstant) lhsValue;
            throw new IllegalAssignmentException("Error: cannot redefine named constant '" + nc.getName() + "'");
        }
        Variable variable = (Variable) n.getRhs();
        env.put(variable, lhsValue);
        return lhsValue;
    }

    @Override
    public SymbolicExpression visit(Clear n) {
        env.clear();
        return n;
    }

    @Override
    public SymbolicExpression visit(Constant n) {
        return n;
    }

    @Override
    public SymbolicExpression visit(Cos n) {
        SymbolicExpression arg = n.getOperand().accept(this);
        if (arg.isConstant()) {
            return new Constant(Math.cos(arg.getValue()));
        }
        return new Cos(arg);
    }

    @Override
    public SymbolicExpression visit(Division n) {
        SymbolicExpression left = n.getLhs().accept(this);
        SymbolicExpression right = n.getRhs().accept(this);

        if(left.isConstant() && right.isConstant()) {
            if(right.getValue() == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return new Constant(left.getValue() / right.getValue());
        }
        return new Division (left, right);
    }

    @Override
    public SymbolicExpression visit(Exp n) {
        SymbolicExpression arg = n.getOperand().accept(this);

        if(arg.isConstant()) {
            return new Constant(Math.exp(arg.getValue()));
        }
        return new Exp(arg);
    }

    @Override
    public SymbolicExpression visit(Log n) {
        SymbolicExpression arg = n.getOperand().accept(this);

        if (arg.isConstant()) {
            return new Constant(Math.log(arg.getValue()));
        }
        return new Log(arg);
    }

    @Override
    public SymbolicExpression visit(Multiplication n) {
        SymbolicExpression left = n.getLhs().accept(this);
        SymbolicExpression right = n.getRhs().accept(this);

        if (left.isConstant() && right.isConstant()) {
            return new Constant(left.getValue() * right.getValue());
        }
        return new Multiplication(left, right);
    }

    @Override
    public SymbolicExpression visit(Negation n) {
        SymbolicExpression expr = n.getOperand().accept(this);

        if(expr.isConstant()) {
            return new Constant(-expr.getValue());
        }
        return new Negation(expr);
    }

    @Override
    public SymbolicExpression visit(Quit n) {
        System.exit(0);
        return n;
    }

    @Override
    public SymbolicExpression visit(Sin n) {
        SymbolicExpression arg = n.getOperand().accept(this);
        if (arg.isConstant()) {
            return new Constant(Math.sin(arg.getValue()));
        }
        return new Sin(arg);
    }

    @Override
    public SymbolicExpression visit(Subtraction n) {
        SymbolicExpression left = n.getLhs().accept(this);
        SymbolicExpression right = n.getRhs().accept(this);

        if (left.isConstant() && right.isConstant()) {
            return new Constant(left.getValue() - right.getValue());
        }
        return new Subtraction(left, right);
    }

    @Override
    public SymbolicExpression visit(Variable n) {
        if (!env.containsKey(n)) {
            // Free variables remain symbolic
            return n;
        }
        return env.get(n);
    }

    @Override
    public SymbolicExpression visit(Vars n) {
        env.forEach((key, value) -> System.out.println(key + " = " + value));
        return n;
    }

    @Override
    public SymbolicExpression visit(End n) {
        return n;
    }

    @Override
    public SymbolicExpression visit(Scope n) {
        env.pushEnvironment();
        SymbolicExpression result = n.getBody().accept(this);
        env.popEnvironment();
        return result;
    }

    @Override
    public SymbolicExpression visit(Conditional n) {
        SymbolicExpression lhs = n.getConditionLhs().accept(this);
        SymbolicExpression rhs = n.getConditionRhs().accept(this);
    
        if (!lhs.isConstant() || !rhs.isConstant()) {
            throw new RuntimeException("Conditionals require concrete values for comparison.");
        }
    
        double lhsValue = lhs.getValue();
        double rhsValue = rhs.getValue();
    
        boolean conditionMet;
        switch (n.getOperator()) {
            case "<":  conditionMet = lhsValue < rhsValue; break;
            case "<=": conditionMet = lhsValue <= rhsValue; break;
            case ">":  conditionMet = lhsValue > rhsValue; break;
            case ">=": conditionMet = lhsValue >= rhsValue; break;
            case "==": conditionMet = lhsValue == rhsValue; break;
            default: throw new RuntimeException("Invalid operator in conditional: " + n.getOperator());
        }
    
        if (conditionMet) {
            return n.getIfScope().accept(this);
        } else {
            return n.getElseScope().accept(this);
        }
    }

    @Override
    public SymbolicExpression visit(FunctionDeclaration fd) {
        env.putFunction(fd.getIdentifier(), fd);
        return fd;
    }

    @Override
    public SymbolicExpression visit(FunctionCall fc) {
        FunctionDeclaration func = env.getFunction(fc.getFunctionName());
        if (func == null) {
            throw new RuntimeException("Error: Undefined function '" + fc.getFunctionName() + "'");
        }

        List<Variable> parameters = func.getParameters();
        List<SymbolicExpression> arguments = fc.getArguments();

        if (parameters.size() != arguments.size()) {
            throw new RuntimeException("Error: Function '" + fc.getFunctionName() +
                    "' called with incorrect number of arguments. Expected " +
                    parameters.size() + ", got " + arguments.size());
        }

        env.pushEnvironment();
        for (int i = 0; i < parameters.size(); i++) {
            env.put(parameters.get(i), arguments.get(i).accept(this));
        }

        SymbolicExpression result = null;
        for (SymbolicExpression expr : func.getBody()) {
            result = expr.accept(this);
        }
        env.popEnvironment();
        return result;
    }

    @Override
    public SymbolicExpression visit(Sequence seq) {
        SymbolicExpression result = null;
        for (SymbolicExpression expr : seq.getExpressions()) {
            result = expr.accept(this);
        }
        return result;
    }

    @Override
    public SymbolicExpression visit(NamedConstant nc) {
        if (env.containsKey(nc)) {
            return env.get(nc);
        } else {
            throw new RuntimeException("Named constant '" + nc.getName() + "' is not defined");
        }
    }

}
