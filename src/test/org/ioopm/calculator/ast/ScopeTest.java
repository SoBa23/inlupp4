package test.org.ioopm.calculator.ast;

import static org.junit.Assert.assertEquals;

import org.ioopm.calculator.ast.Addition;
import org.ioopm.calculator.ast.Assignment;
import org.ioopm.calculator.ast.Constant;
import org.ioopm.calculator.ast.EvaluationVisitor;
import org.ioopm.calculator.ast.Scope;
import org.ioopm.calculator.ast.ScopedEnvironment;
import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;
import org.junit.Test;

public class ScopeTest {
    
    @Test
    public void testScopeEvaluation() {
        ScopedEnvironment env = new ScopedEnvironment();
        EvaluationVisitor evaluator = new EvaluationVisitor();

        SymbolicExpression expr = new Addition(
            new Scope(new Assignment(new Constant(1), new Variable("x"))),
            new Scope(new Assignment(new Constant(1), new Variable("x")))
            );

            SymbolicExpression result = evaluator.evaluate(expr, env);
            assertEquals(new Constant(2.0), result);
    }
}
