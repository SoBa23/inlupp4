package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BinaryTest {
    @Test
    public void testGetLhsRhs() {
        Binary expr = new Addition(new Constant(1), new Constant(2));
        assertEquals(new Constant(1), expr.getLhs());
        assertEquals(new Constant(2), expr.getRhs());
    }

    @Test
    public void testEquals() {
        Binary expr1 = new Addition(new Constant(1), new Constant(2));
        Binary expr2 = new Addition(new Constant(1), new Constant(2));
        assertTrue(expr1.equals(expr2));
    }
}
