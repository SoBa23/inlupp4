package test.java.org.ioopm.calculator.ast;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class FunctionTest {
    private final CalculatorParser parser = new CalculatorParser();
    private final EvaluationVisitor evaluator = new EvaluationVisitor();

    @Test
    public void testFunctionCallAndArgumentCheck() throws Exception {
        ScopedEnvironment env = new ScopedEnvironment();
        // define function max
        FunctionDeclaration decl = (FunctionDeclaration) parser.parse("function max(x, y)", env);
        decl.addLine(parser.parse("if x < y { y } else { x }", env));
        parser.parse("end", env); // consume end
        evaluator.evaluate(decl, env);

        SymbolicExpression call = parser.parse("max(5, 7)", env);
        SymbolicExpression result = evaluator.evaluate(call, env);
        assertEquals(new Constant(7), result);

        SymbolicExpression tooFew = parser.parse("max(5)", env);
        assertThrows(FunctionCallException.class, () -> evaluator.evaluate(tooFew, env));

        SymbolicExpression tooMany = parser.parse("max(5, 7, 9)", env);
        assertThrows(FunctionCallException.class, () -> evaluator.evaluate(tooMany, env));
    }

    @Test
    public void testRecursiveFunction() throws Exception {
        ScopedEnvironment env = new ScopedEnvironment();
        FunctionDeclaration fact = (FunctionDeclaration) parser.parse("function factorial(n)", env);
        fact.addLine(parser.parse("n - 1 = m", env));
        fact.addLine(parser.parse("if n > 1 { factorial(m) * n } else { 1 }", env));
        parser.parse("end", env);
        evaluator.evaluate(fact, env);

        SymbolicExpression call = parser.parse("factorial(5)", env);
        SymbolicExpression result = evaluator.evaluate(call, env);
        assertEquals(new Constant(120), result);
    }
}
