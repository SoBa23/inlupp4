package org.ioopm.calculator.parser;

import org.ioopm.calculator.ast.*;

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.sql.SQLSyntaxErrorException;
import java.io.IOException;

import java.util.*;

/**
 * Represents the parsing of strings into valid expressions defined in the AST.
 */
public class CalculatorParser {
    private StreamTokenizer st;
    private Environment vars;
    private ScopedEnvironment env;
    private static char MULTIPLY = '*';
    private static char ADDITION = '+';
    private static char SUBTRACTION = '-';
    private static char DIVISION = '/';
    private static String NEG = "Neg";
    private static char NEGATION = '-';
    private static String SIN = "Sin";
    private static String COS = "Cos";
    private static String LOG = "Log";
    private static String EXP = "Exp";
    private static char ASSIGNMENT = '=';

    // unallowerdVars is used to check if variabel name that we
    // want to assign new meaning to is a valid name eg 3 = Quit
    // or 10 + x = L is not allowed
    private final ArrayList < String > unallowedVars = new ArrayList < String > (Arrays.asList("Quit",
        "Vars",
        "Clear"));

    /**
     * Used to parse the inputted string by the Calculator program
     * @param inputString the string used to parse
     * @param vars the Environment in which the variables exist
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    public SymbolicExpression parse(String inputString, ScopedEnvironment env) throws IOException {
        this.st = new StreamTokenizer(new StringReader(inputString)); // reads from inputString via stringreader.
        this.env = env;
        this.st.ordinaryChar('-');
        this.st.ordinaryChar('/');
        this.st.eolIsSignificant(true);
        SymbolicExpression result = statement(); // calls to statement
        return result; // the final result
    }

    /**
     * Checks wether the token read is a command or an assignment
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression
     */
    private SymbolicExpression statement() throws IOException {
        SymbolicExpression result;
        this.st.nextToken(); // Look at the next token on the stream
        if (this.st.ttype == this.st.TT_EOF) {
            throw new SyntaxErrorException("Error: Expected an expression");
        }

        if (this.st.ttype == this.st.TT_WORD) {
            if (this.st.sval.equals("Quit") || this.st.sval.equals("Vars") || this.st.sval.equals("Clear")) {
                result = command();
            } else {
                // The first token of the expression has already been read
                // so we let the assignment parser continue from it without
                // pushing it back onto the stream.
                result = assignment();
            }
        } else {
            // Likewise for non-word tokens we simply continue parsing the
            // assignment/expression from the current token.
            result = assignment();
        }

        if (this.st.nextToken() != this.st.TT_EOF) { // token should be an end of stream token if we are done
            if (this.st.ttype == this.st.TT_WORD) {
                throw new SyntaxErrorException("Error: Unexpected '" + this.st.sval + "'");
            } else {
                throw new SyntaxErrorException("Error: Unexpected '" + String.valueOf((char) this.st.ttype) + "'");
            }
        }
        return result;
    }


    /**
     * Checks what kind of command that should be returned
     * @return an instance of Quit, Clear or Vars depending on the token parsed
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression command() throws IOException {
        if (this.st.sval.equals("Quit")) {
            return Quit.instance();
        } else if (this.st.sval.equals("Clear")) {
            return Clear.instance();
        } else if (this.st.sval.equals("Vars")) {
            return Vars.instance();
        } else {
            throw new SyntaxErrorException("Unknown command: " + this.st.sval);
        }
    }


    /**
     * Checks wether the token read is an assignment between 2 expression and 
     * descend into the right hand side of '='
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression,
     *         the variable on rhs of '=' is a number or invalid variable
     */
    private SymbolicExpression assignment() throws IOException {
        SymbolicExpression result = expression();
        this.st.nextToken();

        while (this.st.ttype == '=') {
            this.st.nextToken();

            // Right-hand side should be an identifier representing the variable
            if (this.st.ttype == this.st.TT_NUMBER) {
                throw new SyntaxErrorException("Error: Numbers cannot be used as variable names");
            } else if (this.st.ttype != this.st.TT_WORD) {
                throw new SyntaxErrorException("Error: Invalid assignment syntax");
            }

            SymbolicExpression key = identifier();

            if (key instanceof NamedConstant) {
                throw new RuntimeException("Error: Assignment to a named constant '" + key + "'");
            }

            if (!(key instanceof Variable)) {
                throw new SyntaxErrorException("Error: Right-hand side of assignment must be a variable");
            }

            result = new Assignment(result, key);
            this.st.nextToken();
        }
    
        this.st.pushBack();
        return result;
    }

    /**
     * Check if valid identifier for variable and return that if so
     * @return a SymbolicExpression that is either a named constant or a new variable
     * @throws IOException by nextToken() if it reads invalid input
     * @throws IllegalExpressionException if you try to redefine a string that isn't allowed
     */
    private SymbolicExpression identifier() throws IOException {
        if (this.unallowedVars.contains(this.st.sval)) {
            throw new IllegalExpressionException("Error: cannot redefine " + this.st.sval);
        }
    
        if (Constants.namedConstants.containsKey(this.st.sval)) {
            return new NamedConstant(st.sval, Constants.namedConstants.get(st.sval));
        } else {
            // Kontrollera om variabeln finns i nuvarande scope
            SymbolicExpression value = this.env.get(new Variable(this.st.sval));
            if (value != null) {
                return value;
            }
            return new Variable(this.st.sval); // Annars skapa ny variabel
        }
    }


    /**
     * Checks wether the token read is an addition or subtraction
     * and then continue on with the right hand side of operator
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression expression() throws IOException {
        SymbolicExpression result = term();

        this.st.nextToken();
        // Stop parsing if we encounter the end of a scope. The token is pushed
        // back so the surrounding code can handle the closing brace.
        if (this.st.ttype == '}') {
            this.st.pushBack();
            return result;
        }

        while (this.st.ttype == ADDITION || this.st.ttype == SUBTRACTION) {
            int operation = st.ttype;
            this.st.nextToken();
            if (operation == ADDITION) {
                result = new Addition(result, term());
            } else {
                result = new Subtraction(result, term());
            }

            this.st.nextToken();

            if (this.st.ttype == '}') {
                // Do not consume the closing brace; leave it for the caller.
                break;
            }
        }
        this.st.pushBack();
        return result;
    }

    /**
     * Checks wether the token read is an Multiplication or
     * Division and then continue on with the right hand side of
     * operator
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression term() throws IOException {
        SymbolicExpression result = primary();

        this.st.nextToken();

        if (this.st.ttype == '}') {
            this.st.pushBack();
            return result;
        }

        while (this.st.ttype == MULTIPLY || this.st.ttype == DIVISION) {
            int operation = st.ttype;
            this.st.nextToken();

            if (operation == MULTIPLY) {
                result = new Multiplication(result, primary());
            } else {
                result = new Division(result, primary());
            }
            this.st.nextToken();

            if (this.st.ttype == '}') {
                break;
            }
        }
        this.st.pushBack();
        return result;
    }

    /**
     * Checks wether the token read is a parantheses and then
     * continue on with the expression inside of it or if the
     * operation is an unary operation and then continue on with
     * the right hand side of that operator else if it's a
     * number/identifier
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression,
     *         missing right parantheses
     */
    private SymbolicExpression primary() throws IOException {
        SymbolicExpression result;
        if(st.ttype == '{') {
            return scope();
        }
        if (this.st.ttype == this.st.TT_WORD && this.st.sval.equals("if")) {
            return conditional();
        }
        if (this.st.ttype == this.st.TT_WORD && this.st.sval.equals("def")) {
            return functionDeclaration();
        }
        if (this.st.ttype == '(') {
            this.st.nextToken();
            result = assignment();
            /// This captures unbalanced parentheses!
            if (this.st.nextToken() != ')') {
                throw new SyntaxErrorException("expected ')'");
            }
        } else if (this.st.ttype == this.st.TT_WORD && this.st.sval.equals("call")) {
            result = functionCall();

        } else if (this.st.ttype == NEGATION) {
            result = unary();
        } else if (this.st.ttype == this.st.TT_WORD) {
            if (st.sval.equals(SIN) ||
                st.sval.equals(COS) ||
                st.sval.equals(EXP) ||
                st.sval.equals(NEG) ||
                st.sval.equals(LOG)) {

                result = unary();
            } else {
                result = identifier();
            }
        } else {
            this.st.pushBack();
            result = number();
        }
        return result;
    }
    

    /**
     * Checks what type of Unary operation the token read is and
     * then continues with the expression that the operator holds
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    private SymbolicExpression unary() throws IOException {
        SymbolicExpression result;
        int operationNeg = st.ttype;
        String operation = st.sval;
        this.st.nextToken();
        if (operationNeg == NEGATION || operation.equals(NEG)) {
            result = new Negation(primary());
        } else if (operation.equals(SIN)) {
            result = new Sin(primary());
        } else if (operation.equals(COS)) {
            result = new Cos(primary());
        } else if (operation.equals(LOG)) {
            result = new Log(primary());
        } else {
            result = new Exp(primary());
        }
        return result;
    }
    
    /**
     * Checks if the token read is a number - should always be a number in this method
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression,
     *         expected a number which is not present
     */
    private SymbolicExpression number() throws IOException {
        this.st.nextToken();
        if (this.st.ttype == this.st.TT_NUMBER) {
            return new Constant(this.st.nval);
        } else {
            throw new SyntaxErrorException("Error: Expected number");
        }
    }

    private SymbolicExpression scope() throws IOException {
        ScopedEnvironment env = new ScopedEnvironment();
        env.pushEnvironment(); // Starta nytt scope
    
        List<SymbolicExpression> expressions = new ArrayList<>();
    
        while (true) {
            this.st.nextToken();

            if (this.st.ttype == this.st.TT_EOF) {
                throw new SyntaxErrorException("Error: Unexpected end of input inside scope");
            }

            if (this.st.ttype == '}') { // Slutet av scopet
                this.st.pushBack(); // Let the caller see the closing brace
                break;
            }
            this.st.pushBack();
            expressions.add(assignment()); // Lägg till uttryck i scopet
        }
    
        env.popEnvironment(); // Avsluta scopet
        return new Scope(new Sequence(expressions)); // Returnera ett Scope med en sekvens av uttryck
    }
    
    
    private SymbolicExpression conditional() throws IOException {
        this.st.nextToken();
        SymbolicExpression lhs = primary();
        if (!(new HashSet<String>(Arrays.asList("<", ">", "<=", ">=", "==")).contains(this.st.sval))) {
            throw new SyntaxErrorException("Error: Invalid comparison operator");
        }
        String operator = this.st.sval;
        this.st.nextToken();
        SymbolicExpression rhs = primary();

        if (this.st.nextToken() != '{') {
            throw new SyntaxErrorException("Error: Expected '{' after condition");
        }
        SymbolicExpression ifBranch = scope();

        if (!this.st.sval.equals("else")) {
            throw new SyntaxErrorException("Error: Expected 'else' after if branch");
        }
        this.st.nextToken();

        if (this.st.nextToken() != '{') {
            throw new SyntaxErrorException("Error: Expected '{' after else");
        }
        SymbolicExpression elseBranch = scope();

        return new Conditional(lhs, operator, rhs, ifBranch, elseBranch);
    }

    private SymbolicExpression functionDeclaration() throws IOException {
        this.st.nextToken();
        if (this.st.ttype != StreamTokenizer.TT_WORD) {
            throw new SyntaxErrorException("Error: Expected function name");
        }
        String functionName = this.st.sval;

        this.st.nextToken();
        if (this.st.ttype != '(') {
            throw new SyntaxErrorException("Error: Expected '(' after function name");
        }
        List<String> parameters = new ArrayList<>();
        while (this.st.nextToken() != ')') {
            if (this.st.ttype != StreamTokenizer.TT_WORD) {
                throw new SyntaxErrorException("Error: Expected parameter name");
            }
            parameters.add(this.st.sval);
            if (this.st.nextToken() != ',' && this.st.ttype != ')') {
                throw new SyntaxErrorException("Error: Expected ',' or ')' in parameter list");
            }
        }

        List<SymbolicExpression> body = new ArrayList<>();
        while (true) {
            this.st.nextToken();
            if (this.st.ttype == StreamTokenizer.TT_WORD && this.st.sval.equals("end")) {
                break;
            }
            this.st.pushBack();
            body.add(statement());
        }
        return new FunctionDeclaration(functionName, parameters, new Sequence(body));
    }

    private SymbolicExpression functionCall() throws IOException {
        this.st.nextToken(); // Läs namnet på funktionen
        if (this.st.ttype != this.st.TT_WORD) {
            throw new SyntaxErrorException("Error: Expected a function name");
        }
        String functionName = this.st.sval;

        // Läs argumenten
        this.st.nextToken();
        if (this.st.ttype != '(') {
            throw new SyntaxErrorException("Error: Expected '(' after function name");
        }
        List<SymbolicExpression> arguments = new ArrayList<>();
        while (this.st.nextToken() != ')') {
            this.st.pushBack();
            arguments.add(expression());
            this.st.nextToken();
            if (this.st.ttype != ',' && this.st.ttype != ')') {
                throw new SyntaxErrorException("Error: Expected ',' or ')' in argument list");
            }
        }

        return new FunctionCall(functionName, arguments);
    }
}
