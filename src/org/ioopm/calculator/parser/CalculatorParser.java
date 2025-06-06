package org.ioopm.calculator.parser;

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;

import java.util.*;

import org.ioopm.calculator.ast.Conditional;
import org.ioopm.calculator.ast.Scope;
import org.ioopm.calculator.ast.SymbolicExpression;
import org.ioopm.calculator.ast.Addition;
import org.ioopm.calculator.ast.Subtraction;
import org.ioopm.calculator.ast.Multiplication;
import org.ioopm.calculator.ast.Division;
import org.ioopm.calculator.ast.Environment;
import org.ioopm.calculator.ast.Assignment;
import org.ioopm.calculator.ast.NamedConstant;
import org.ioopm.calculator.ast.Variable;
import org.ioopm.calculator.ast.Constant;
import org.ioopm.calculator.ast.Quit;
import org.ioopm.calculator.ast.Clear;
import org.ioopm.calculator.ast.Vars;
import org.ioopm.calculator.ast.FunctionCall;
import org.ioopm.calculator.ast.FunctionDeclaration;
import org.ioopm.calculator.ast.Negation;
import org.ioopm.calculator.ast.Sin;
import org.ioopm.calculator.ast.Cos;
import org.ioopm.calculator.ast.Log;
import org.ioopm.calculator.ast.Exp;
import org.ioopm.calculator.ast.Constants;
import org.ioopm.calculator.*;
import org.ioopm.calculator.ast.End;

/**
 * Represents the parsing of strings into valid expressions defined in the AST.
 */
public class CalculatorParser {
    private StreamTokenizer st;
    private Environment vars;
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
    private final ArrayList<String> unallowedVars = new ArrayList<String>(Arrays.asList(
        "Quit",
        "Vars",
        "Clear",
        "end",
        "function",
        "if",
        "else"));

    /**
     * Used to parse the inputted string by the Calculator program
     * @param inputString the string used to parse
     * @param vars the Environment in which the variables exist
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     */
    public SymbolicExpression parse(String inputString, Environment vars) throws IOException {
        this.st = new StreamTokenizer(new StringReader(inputString)); // reads from inputString via stringreader.
        this.vars = vars;
        this.st.ordinaryChar('-');
        this.st.ordinaryChar('/');
        this.st.ordinaryChar('<');
        this.st.ordinaryChar('>'); 
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
    @SuppressWarnings("static-access")
    private SymbolicExpression statement() throws IOException {
        SymbolicExpression result;
        this.st.nextToken(); //kollar pÃ¥ nÃ¤sta token som ligger pÃ¥ strÃ¶mmen
        if (this.st.ttype == this.st.TT_EOF) {
            throw new SyntaxErrorException("Error: Expected an expression");
        }

        if (this.st.ttype == this.st.TT_WORD) { // vilken typ det senaste tecken vi lÃ¤ste in hade.
            if (this.st.sval.equals("Quit") || this.st.sval.equals("Vars") || this.st.sval.equals("Clear") || this.st.sval.equals("end")) { // sval = string Variable
                result = command();
            } else if (this.st.sval.equals("function")) {
                result = functionDeclaration();
            } else {
                result = assignment(); // gÃ¥r vidare med uttrycket.
            }
        } else {
            result = assignment(); // om inte == word, gÃ¥ till assignment Ã¤ndÃ¥ (kan vara tt_number)
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
        } else if (this.st.sval.equals("end")) {
            return End.instance();
        } else {
            return Vars.instance();
        }
    }

    private SymbolicExpression functionDeclaration() throws IOException{
        this.st.nextToken();
        if (this.st.ttype != this.st.TT_WORD) {
            throw new SyntaxErrorException("Expected function name after 'function'");
        }
        String functionName = this.st.sval;
    
        this.st.nextToken();
        if (this.st.ttype != '(') {
            throw new SyntaxErrorException("Expected '(' after function name");
        }
    
        List<Variable> parameters = new ArrayList<>();
        this.st.nextToken();
        while (this.st.ttype != ')') {
            if(this.st.ttype == this.st.TT_WORD){
                parameters.add(new Variable(this.st.sval));
                this.st.nextToken();
                if (this.st.ttype == ',') {
                    this.st.nextToken(); 
                } else if (this.st.ttype == ')') {
                    break;
                } else {
                    throw new SyntaxErrorException("Expected ',' or ')' in parameter list");
                }
            }else {
                throw new SyntaxErrorException("Expected a String as parameter");
            }
        }
    
        List<SymbolicExpression> body = new ArrayList<>();
    
        return new FunctionDeclaration(functionName, body, parameters);
    }

    /**
     * Checks wether the token read is an assignment between 2 expression and 
     * descend into the right hand side of '='
     * @return a SymbolicExpression to be evaluated
     * @throws IOException by nextToken() if it reads invalid input
     * @throws SyntaxErrorException if the token parsed cannot be turned into a valid expression,
     *         the variable on rhs of '=' is a number or invalid variable
     */
    @SuppressWarnings("static-access")
    private SymbolicExpression assignment() throws IOException, IllegalAssignmentException {
        SymbolicExpression result = expression();
        this.st.nextToken();
        while (this.st.ttype == ASSIGNMENT) {
            this.st.nextToken();
            if (this.st.ttype == this.st.TT_NUMBER) {
                throw new SyntaxErrorException("Error: Numbers cannot be used as a variable name");
            } else if (this.st.ttype != this.st.TT_WORD) {
                throw new SyntaxErrorException("Error: Not a valid assignment of a variable"); //this handles faulty inputs after the equal sign eg. 1 = (x etc
            } else {
                if (this.st.sval.equals("ans")) {
                    throw new SyntaxErrorException("Error: ans cannot be redefined");
                }
                SymbolicExpression key = identifier();
                result = new Assignment(result, key);
            }
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
        SymbolicExpression result;

        if (this.unallowedVars.contains(this.st.sval)) {
            throw new IllegalExpressionException("Error: cannot redefine " + this.st.sval);
        }

        if (Constants.namedConstants.containsKey(this.st.sval)) {
            result = new NamedConstant(st.sval, Constants.namedConstants.get(st.sval));
        } else {
            result = new Variable(this.st.sval);
        }
        return result;
    }

    public SymbolicExpression functionCall() throws IOException{
        String identifier = this.st.sval;
        this.st.nextToken();
        if (this.st.ttype != '(') {
            throw new SyntaxErrorException("Expected '(' after function name");
        }
    
        List<SymbolicExpression> arguments = new ArrayList<>();

        this.st.nextToken(); 
        while (this.st.ttype != ')') {
            SymbolicExpression expr = expression(); 
            arguments.add(expr);
            this.st.nextToken();
            if (this.st.ttype == ',') {
                this.st.nextToken(); 
            } else if (this.st.ttype == ')') {
                break;
            } else {
                throw new SyntaxErrorException("Expected ',' or ')' in parameter list");
            }
        }
    
        return new FunctionCall(identifier, arguments);
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
        while (this.st.ttype == ADDITION || this.st.ttype == SUBTRACTION) {
            int operation = st.ttype;
            this.st.nextToken();
            if (operation == ADDITION) {
                result = new Addition(result, term());
            } else {
                result = new Subtraction(result, term());
            }
            this.st.nextToken();
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
        while (this.st.ttype == MULTIPLY || this.st.ttype == DIVISION) {
            int operation = st.ttype;
            this.st.nextToken();

            if (operation == MULTIPLY) {
                result = new Multiplication(result, primary());
            } else {
                result = new Division(result, primary());
            }
            this.st.nextToken();
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
    @SuppressWarnings("static-access")
    private SymbolicExpression primary() throws IOException {
        SymbolicExpression result;
        if (this.st.ttype == '(') {
            this.st.nextToken();
            result = assignment();
            /// This captures unbalanced parentheses!
            if (this.st.nextToken() != ')') {
                throw new SyntaxErrorException("expected ')'");
            }
        } else if (this.st.ttype == '{'){
            this.st.nextToken();
            SymbolicExpression expr = assignment();
            /// This captures unbalanced parentheses!
            if (this.st.nextToken() != '}') {
                throw new SyntaxErrorException("expected '}'");
            }
            result = new Scope(expr);

        } else if (this.st.ttype == NEGATION) {
            result = unary();
        } else if (this.st.ttype == this.st.TT_WORD) {
            if(st.sval.equals("if")){
                result = conditional();

            }else if (st.sval.equals(SIN) ||
                st.sval.equals(COS) ||
                st.sval.equals(EXP) ||
                st.sval.equals(NEG) ||
                st.sval.equals(LOG)) {

                result = unary();
            } else {
                if(vars.getFunctions().containsKey(this.st.sval)){
                    result = functionCall();
                }else{
                    result = identifier();
                }
            }
        } else {
            this.st.pushBack();
            result = number();
        }
        return result;
    }

    /**
     * handles parsing a conditional
     * @return A Condional
     * 
     * @throws IOException
     */
    private SymbolicExpression conditional() throws IOException{
        this.st.nextToken();
        SymbolicExpression lhs = expression();

        this.st.nextToken();
        String operator;
        if (this.st.ttype == '<') {
            this.st.nextToken();
            if (this.st.ttype == '=') {
                operator = "<=";
            } else {
                this.st.pushBack(); 
                operator = "<";
            }
        } else if (this.st.ttype == '>') {
            this.st.nextToken();
            if (this.st.ttype == '=') {
                operator = ">=";
            } else {
                this.st.pushBack(); 
                operator = ">";
            }
        } else if (this.st.ttype == '=') {
            this.st.nextToken();
            if (this.st.ttype == '=') {
                operator = "==";
            } else {
                throw new SyntaxErrorException("Unexpected '='. Did you mean '=='?");
            }
        } else {
            throw new SyntaxErrorException("Expected a comparison operator");
        }
        this.st.nextToken();
        SymbolicExpression rhs = expression();
        
        // if branch, could have just called primary so the it becomes a scope duplicated code
        if (this.st.nextToken() != '{') {
            throw new SyntaxErrorException("Expected '{' after condition");
        }
        this.st.nextToken();
        SymbolicExpression ifExpr = assignment();
        if (this.st.nextToken() != '}') {
            throw new SyntaxErrorException("Expected '}' after if-branch");
        }
        Scope ifBranch = new Scope(ifExpr);


        if (this.st.nextToken() != StreamTokenizer.TT_WORD || !this.st.sval.equals("else")) {
            throw new SyntaxErrorException("Expected 'else' after if-branch");
        }

        if (this.st.nextToken() != '{') {
            throw new SyntaxErrorException("Expected '{' after else");
        }
        this.st.nextToken();
        SymbolicExpression elseExpr = assignment();
        if (this.st.nextToken() != '}') {
            throw new SyntaxErrorException("Expected '}' after else-branch");
        }
        Scope elseBranch = new Scope(elseExpr);
    
        return new Conditional(lhs, rhs, operator, ifBranch, elseBranch);
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
}
