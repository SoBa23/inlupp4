package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class QuitTest {
    @Test
    public void testIsCommand() {
        assertTrue(Quit.instance().isCommand());
    }
}