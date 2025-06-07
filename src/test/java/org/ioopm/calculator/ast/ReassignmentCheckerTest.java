package test.java.org.ioopm.calculator.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ioopm.calculator.ast.Addition;
import org.ioopm.calculator.ast.Assignment;
import org.ioopm.calculator.ast.Constant;
import org.ioopm.calculator.ast.ReassignmentChecker;
import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;
import org.junit.Test;

public class ReassignmentCheckerTest {
    
    @Test
    public void testValidAssignment() {
        ReassignmentChecker checker = new ReassignmentChecker();
        SymbolicExpression expr = new Addition(
            new Assignment(new Constant(5), new Variable("x")),
            new Assignment(new Constant(6), new Variable("y"))
            );
        
            assertTrue(checker.check(expr));
    }

    @Test
    public void testReassignment() {
        ReassignmentChecker checker = new ReassignmentChecker();
        SymbolicExpression expr = new Addition(
            new Assignment(new Constant(5), new Variable("x")),
            new Assignment(new Constant(6), new Variable("x"))
            );

        assertFalse(checker.check(expr));
        assertEquals(1, checker.getReassignedVariables().size());
    }
}
