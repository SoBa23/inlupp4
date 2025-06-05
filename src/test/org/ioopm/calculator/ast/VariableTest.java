package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class VariableTest {

    @Test
    public void testGetName() {
        Variable v = new Variable("x");
        assertEquals("x", v.getName());
    }

    @Test
    public void testIsConstant() {
        Variable v = new Variable("x");
        assertFalse(v.isConstant());
    }

    @Test
    public void testEquals() {
        Variable v1 = new Variable("x");
        Variable v2 = new Variable("x");
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testToString() {
        Variable v = new Variable("x");
        assertEquals("x", v.toString());
    }

    @Test
    public void testEval() {
        Environment env = new Environment();
        env.put(new Variable("x"), new Constant(42));
        Variable v = new Variable("x");
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(v, env);
        assertEquals(new Constant(42), result);
    }
}
