package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class VarsTest {
    @Test
    public void testIsCommand() {
        assertTrue(Vars.instance().isCommand());
    }
}
