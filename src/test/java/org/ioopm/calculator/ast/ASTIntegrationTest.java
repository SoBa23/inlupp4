package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ASTIntegrationTest {

    @Test
    public void testAdditionAndMultiplication() {
        SymbolicExpression expr = new Multiplication(
            new Addition(new Constant(5), new Constant(3)),
            new Constant(2)
        );
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(16), result);
    }

    @Test
    public void testVariableEvaluation() {
        Environment env = new Environment();
        env.put(new Variable("x"), new Constant(4));

        SymbolicExpression expr = new Addition(
            new Variable("x"),
            new Constant(3)
        );
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(7), result);
    }

    @Test
    public void testUnaryNegation() {
        SymbolicExpression expr = new Negation(new Constant(5));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(-5), result);
    }

    @Test           
    public void testNamedConstants() {
        SymbolicExpression expr = new Addition(
            new NamedConstant("pi", Math.PI),
            new Constant(2)
        );
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(Math.PI + 2), result);
    }
}
