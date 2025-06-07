package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CosTest {
    @Test
    public void testToString() {
        Cos expr = new Cos(new Constant(0));
        assertEquals("cos(0.0)", expr.toString());
    }

    @Test
    public void testEval() {
        Cos expr = new Cos(new Constant(0));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(1.0), result);
    }

    @Test
    public void testEquals() {
        Cos expr1 = new Cos(new Constant(0));
        Cos expr2 = new Cos(new Constant(0));
        assertTrue(expr1.equals(expr2));
    }
}