package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class NamedConstantTest {
    @Test
    public void testToString() {
        NamedConstant pi = new NamedConstant("pi", Math.PI);
        assertEquals("pi", pi.toString());
    }

    @Test
    public void testEval() {
        NamedConstant pi = new NamedConstant("pi", Math.PI);
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(pi, env);
        assertEquals(new Constant(Math.PI), result);
    }
}