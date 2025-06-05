package test.org.ioopm.calculator.ast;

import static org.junit.jupiter.api.Assertions.*;


import org.ioopm.calculator.ast.Conditional;
import org.ioopm.calculator.ast.Constant;
import org.ioopm.calculator.ast.EvaluationVisitor;
import org.ioopm.calculator.ast.ScopedEnvironment;
import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;
import org.junit.Test;

public class ConditionalTest {
    private EvaluationVisitor evaluator = new EvaluationVisitor();
    private ScopedEnvironment env = new ScopedEnvironment();

    @Test
    public void testSimpleConditional() {
        Variable x = new Variable("x");
        env.put(x, new Constant(3));
        Variable y = new Variable("y");
        env.put(y, new Constant(4));

        Conditional cond = new Conditional(
            x, 
            "<", 
            y, 
            new Constant(42), 
            new Constant(4711)
        );

        SymbolicExpression result = evaluator.evaluate(cond, env);
        assertEquals(new Constant(42), result);
    }

    @Test
    public void testConditionalElse() {
        Variable x = new Variable("x");
        env.put(x, new Constant(5));
        Variable y = new Variable("y");
        env.put(y, new Constant(4));

        Conditional cond = new Conditional(
            x, 
            "<", 
            y, 
            new Constant(42), 
            new Constant(4711)
        );

        SymbolicExpression result = evaluator.evaluate(cond, env);
        assertEquals(new Constant(4711), result);
    }

    @Test(expected = RuntimeException.class)
    public void testConditionalWithFreeVariable() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");

        Conditional cond = new Conditional(
            x, 
            "<", 
            y, 
            new Constant(42), 
            new Constant(4711)
        );

        evaluator.evaluate(cond, env);
    }

    @Test
    public void testNestedConditionals() {
        Variable x = new Variable("x");
        env.put(x, new Constant(3));
        Variable y = new Variable("y");
        env.put(y, new Constant(4));
        Conditional inner = new Conditional(
            x, 
            "==",
            y, 
            new Constant(10), 
            new Constant(20)
        );
        

        Conditional outer = new Conditional(
            x, 
            "<", 
            y, 
            inner, 
            new Constant(30)
        );

        SymbolicExpression result = evaluator.evaluate(outer, env);
        assertEquals(new Constant(20), result);
    }
}
