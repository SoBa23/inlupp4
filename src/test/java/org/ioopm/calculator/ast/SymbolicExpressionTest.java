package test.java.org.ioopm.calculator.ast;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.ioopm.calculator.ast.Constant;
import org.ioopm.calculator.ast.SymbolicExpression;

public class SymbolicExpressionTest {
    @Test
    public void testDefaultImplementation() {
        SymbolicExpression expr = new Constant(5);
        assertEquals("5.0", expr.toString());
    }
}
