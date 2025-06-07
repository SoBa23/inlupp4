package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ClearTest {
    @Test
    public void testIsCommand() {
        Clear clear = Clear.instance();
        assertTrue(clear.isCommand());
    }

    @Test
    public void testSingleton() {
        assertSame(Clear.instance(), Clear.instance());
    }
}