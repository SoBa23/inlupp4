package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ExpTest {
    
    @Test
    public void testEval() {
        Exp expr = new Exp(new Constant(1));
        Environment env = new Environment();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        SymbolicExpression result = evaluator.evaluate(expr, env);
        assertEquals(new Constant(Math.E), result);
    }
}
