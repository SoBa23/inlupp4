package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EnvironmentTest {
    
    @Test
    public void testPutAndGet() {
        Environment env = new Environment();
        env.put(new Variable("x"), new Constant(42));
        assertEquals(new Constant(42), env.get(new Variable("x")));
    }

    @Test
    public void testAlphabeticalOrder() {
        Environment env = new Environment();
        env.put(new Variable("z"), new Constant(3));
        env.put(new Variable("a"), new Constant(1));
        env.put(new Variable("m"), new Constant(2));

        String expected = "Variables: a = 1.0, m = 2.0, z = 3.0";
        assertEquals(expected, env.toString());
    }

    @Test
    public void testEmptyEnvironment() {
        Environment env = new Environment();
        String expected = "No variables defined";

        assertEquals(expected, env.toString());
    }
}
