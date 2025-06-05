package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LogTest {
    @Test
    public void testToString() {
        Log expr = new Log(new Constant(1));
        assertEquals("log(1.0)", expr.toString());
    }

    @Test
    public void testEval() {
        Log expr = new Log(new Constant(1));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(0.0), result);
    }

    @Test
    public void testEquals() {
        Log expr1 = new Log(new Constant(1));
        Log expr2 = new Log(new Constant(1));
        assertTrue(expr1.equals(expr2));
    }
}
