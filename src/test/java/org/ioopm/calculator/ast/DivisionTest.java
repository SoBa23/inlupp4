package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DivisionTest {
    @Test
    public void testToString() {
        Division expr = new Division(new Constant(6), new Constant(3));
        assertEquals("(6.0 / 3.0)", expr.toString());
    }

    @Test
    public void testEval() {
        Division expr = new Division(new Constant(6), new Constant(3));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(2.0), result);
    }

    @Test
    public void testEquals() {
        Division expr1 = new Division(new Constant(6), new Constant(3));
        Division expr2 = new Division(new Constant(6), new Constant(3));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    public void testPriority() {
        Division expr = new Division(new Constant(6), new Constant(3));
        assertEquals(100, expr.getPriority());
    }
}
