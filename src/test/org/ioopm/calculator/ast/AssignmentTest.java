package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AssignmentTest {
    @Test
    public void testToString() {
        Assignment expr = new Assignment(new Variable("x"), new Constant(5));
        assertEquals("x = 5.0", expr.toString());
    }

    @Test
    public void testEval() {
        Environment env = new Environment();
        Assignment expr = new Assignment(new Constant(5), new Variable("x"));
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(5), result);
    }

    @Test
    public void testEquals() {
        Assignment expr1 = new Assignment(new Variable("x"), new Constant(5));
        Assignment expr2 = new Assignment(new Variable("x"), new Constant(5));
        assertTrue(expr1.equals(expr2));
    }
}
