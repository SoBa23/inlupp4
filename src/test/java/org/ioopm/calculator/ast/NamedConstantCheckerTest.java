package test.java.org.ioopm.calculator.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ioopm.calculator.ast.Assignment;
import org.ioopm.calculator.ast.Constant;
import org.ioopm.calculator.ast.NamedConstant;
import org.ioopm.calculator.ast.NamedConstantChecker;
import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Variable;
import org.junit.Test;

public class NamedConstantCheckerTest {
    
    @Test
    public void testValidAssignments() {
        NamedConstantChecker checker = new NamedConstantChecker();
        SymbolicExpression expr = new Assignment(new Constant(5), new Variable("x"));

        assertTrue(checker.check(expr));
    }

    @Test
    public void testInvalidAssignment() {
        NamedConstantChecker checker = new NamedConstantChecker();
        SymbolicExpression expr = new Assignment(new NamedConstant("pi", Math.PI), new Variable("x"));

        assertFalse(checker.check(expr));
        assertEquals(1, checker.getIllegalAssignments().size());
    }
}
