package test.org.ioopm.calculator.parser;

import org.ioopm.calculator.Calculator;
import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.*;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.StreamTokenizer;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

public class ParserTest {
    private final CalculatorParser parser = new CalculatorParser();

    /**
     * Test parsing of constants.
     */
    @Test
    public void testParseConstant() throws IOException {
        SymbolicExpression original = new Constant(42);
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(expression, parsed.toString());
    }

    /**
     * Test parsing of variables.
     */
    @Test
    public void testParseVariable() throws IOException {
        SymbolicExpression original = new Variable("x");
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(expression, parsed.toString());
    }

    /**
     * Test parsing of named constants.
     */
    @Test
    public void testParseNamedConstant() throws IOException {
        SymbolicExpression original = new NamedConstant("pi", Math.PI);
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(expression, parsed.toString());
    }

    /**
     * Test parsing of unary negation.
     */
    @Test
    public void testParseNegation() throws IOException {
        SymbolicExpression original = new Negation(new Constant(3));
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(expression, parsed.toString());
    }

    /**
     * Test parsing of unary sin.
     */
    @Test
    public void testParseSin() throws IOException {
        SymbolicExpression original = new Sin(new Constant(Math.PI / 2));
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(expression, parsed.toString());
    }

    /**
     * Test parsing of binary addition.
     */
    @Test
    public void testParseAddition() throws IOException {
        SymbolicExpression original = new Addition(new Constant(5), new Constant(3));
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(original, parsed);
    }

    /**
     * Test parsing of binary subtraction.
     */
    @Test
    public void testParseSubtraction() throws IOException {
        SymbolicExpression original = new Subtraction(new Constant(10), new Constant(4));
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(original, parsed);
    }

    /**
     * Test parsing of binary multiplication.
     */
    @Test
    public void testParseMultiplication() throws IOException {
        SymbolicExpression original = new Multiplication(new Constant(2), new Constant(3));
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(original, parsed);
    }

    /**
     * Test parsing of binary division.
     */
    @Test
    public void testParseDivision() throws IOException {
        SymbolicExpression original = new Division(new Constant(6), new Constant(3));
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(original, parsed);
    }

    /**
     * Test parsing of nested expressions.
     */
    @Test
    public void testParseNestedExpressions() throws IOException {
        SymbolicExpression original = new Multiplication(
            new Addition(new Constant(3), new Variable("x")),
            new Subtraction(new Variable("y"), new Constant(4))
        );
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(original, parsed);
    }

    /**
     * Test parsing of assignment.
     */
    @Test
    public void testParseAssignment() throws IOException {
        SymbolicExpression original = new Assignment(new Constant(42), new Variable("x"));
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(original, parsed);
    }

    /**
     * Test parsing of command: Vars.
     */
    @Test
    public void testParseVarsCommand() throws IOException {
        SymbolicExpression original = Vars.instance();
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(expression, parsed.toString());
    }

    /**
     * Test parsing of command: Quit.
     */
    @Test
    public void testParseQuitCommand() throws IOException {
        SymbolicExpression original = Quit.instance();
        String expression = original.toString();
        SymbolicExpression parsed = parser.parse(expression, new ScopedEnvironment());
        assertEquals(expression, parsed.toString());
    }

    /**
     * Test parsing invalid syntax.
     */
    @Test
    public void testInvalidSyntax() {
        assertThrows(SyntaxErrorException.class, () -> {
            try {
                parser.parse("(5 +)", new ScopedEnvironment());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Test parsing unbalanced parentheses.
     */
    @Test
    public void testUnbalancedParentheses() {
        assertThrows(SyntaxErrorException.class, () -> {
            try {
                parser.parse("(5 + 3", new ScopedEnvironment());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Test parsing illegal assignment.
     */
    @Test
    public void testIllegalAssignment() {
        assertThrows(IllegalAssignmentException.class, () -> {
            try {
                parser.parse("pi = 5", new ScopedEnvironment());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Test parsing incomplete expression.
     */
    @Test
    public void testIncompleteExpression() {
        assertThrows(SyntaxErrorException.class, () -> {
            try {
                parser.parse("5 +", new ScopedEnvironment());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}