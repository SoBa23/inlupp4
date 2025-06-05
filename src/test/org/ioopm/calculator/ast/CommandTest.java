package test.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CommandTest {
    @Test
    public void testIsCommand() {
        Command command = new Quit();
        assertTrue(command.isCommand());
    }

    @Test
    public void testGetName() {
        Command command = new Quit();
        assertEquals("quit", command.getName());
    }
}
