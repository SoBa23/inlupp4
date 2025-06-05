package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UnaryTest {
    @Test
    public void testNegation() {
        SymbolicExpression expr = new Negation(new Constant(5));
        assertEquals("-(5.0)", expr.toString());
    }

    @Test
    public void testEval() {
        SymbolicExpression expr = new Negation(new Constant(5));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(-5), result);
    }

    @Test
    public void testEquals() {
        Negation n1 = new Negation(new Constant(5));
        Negation n2 = new Negation(new Constant(5));
        assertTrue(n1.equals(n2));
    }
}
