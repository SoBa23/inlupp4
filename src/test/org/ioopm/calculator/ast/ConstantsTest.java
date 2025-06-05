package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConstantsTest {
    @Test
    public void testPredefinedConstants() {
        assertEquals(Math.PI, Constants.namedConstants.get("pi"), 0.0001);
        assertEquals(Math.E, Constants.namedConstants.get("e"), 0.0001);
    }
}
