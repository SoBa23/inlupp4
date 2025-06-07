package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AtomTest {
    @Test
    public void testIsConstant() {
        Atom atom = new Constant(42);
        assertTrue(atom.isConstant());
    }

    @Test
    public void testEquals() {
        Atom atom1 = new Constant(42);
        Atom atom2 = new Constant(42);
        assertTrue(atom1.equals(atom2));
    }
}
