package org.ioopm.calculator.ast;

/**
 * Visitor interface for the AST nodes in the Symbolic Calculator.
 * Each method represents a visit operation for a specific AST node type.
 */
public interface Visitor {
    SymbolicExpression visit(Addition n);
    SymbolicExpression visit(Assignment n);
    SymbolicExpression visit(Clear n);
    SymbolicExpression visit(Constant n);
    SymbolicExpression visit(Cos n);
    SymbolicExpression visit(Division n);
    SymbolicExpression visit(Exp n);
    SymbolicExpression visit(Log n);
    SymbolicExpression visit(Multiplication n);
    SymbolicExpression visit(Negation n);
    SymbolicExpression visit(Quit n);
    SymbolicExpression visit(Sin n);
    SymbolicExpression visit(Subtraction n);
    SymbolicExpression visit(Variable n);
    SymbolicExpression visit(Vars n);
    SymbolicExpression visit(Scope n);
    SymbolicExpression visit(Conditional n);
    SymbolicExpression visit(FunctionDeclaration n);
    SymbolicExpression visit(FunctionCall n);
    SymbolicExpression visit(Sequence n);
    SymbolicExpression visit(NamedConstant n);

}
