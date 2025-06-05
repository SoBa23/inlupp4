package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConstantTest {

    @Test
    public void testGetValue() {
        Constant c = new Constant(42.0);
        assertEquals(42.0, c.getValue(), 0.0001);
    }

    @Test
    public void testIsConstant() {
        Constant c = new Constant(42.0);
        assertTrue(c.isConstant());
    }

    @Test
    public void testEquals() {
        Constant c1 = new Constant(42.0);
        Constant c2 = new Constant(42.0);
        assertTrue(c1.equals(c2));
    }

    @Test
    public void testToString() {
        Constant c = new Constant(42.0);
        assertEquals("42.0", c.toString());
    }

    @Test
    public void testEval() {
        Constant c = new Constant(42.0);
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(c, env);
        assertEquals(new Constant(42.0), result);
    }

    @Test
    public void testGetName() {
        Constant c = new Constant(42);
        assertThrows(RuntimeException.class, c::getName); // Should throw an exception
    }
}