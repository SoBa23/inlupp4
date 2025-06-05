package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MultiplicationTest {
    @Test
    public void testToString() {
        Multiplication expr = new Multiplication(new Constant(2), new Constant(3));
        assertEquals("(2.0 * 3.0)", expr.toString());
    }

    @Test
    public void testEval() {
        Multiplication expr = new Multiplication(new Constant(2), new Constant(3));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(6.0), result);
    }
}