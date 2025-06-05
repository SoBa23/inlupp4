package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AdditionTest {
    @Test
    public void testToString() {
        Addition expr = new Addition(new Constant(2), new Constant(3));
        assertEquals("(2.0 + 3.0)", expr.toString());
    }

    @Test
    public void testEval() {
        Addition expr = new Addition(new Constant(2), new Constant(3));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(5), result);
    }

    @Test
    public void testEquals() {
        Addition expr1 = new Addition(new Constant(2), new Constant(3));
        Addition expr2 = new Addition(new Constant(2), new Constant(3));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    public void testPriority() {
        Addition expr = new Addition(new Constant(2), new Constant(3));
        assertEquals(50, expr.getPriority());
    }
}