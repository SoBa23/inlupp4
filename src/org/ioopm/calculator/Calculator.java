package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;
import org.ioopm.calculator.parser.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main program for the symbolic calculator.
 * Handles user input, parsing, evaluation, and variable management.
 */
public class Calculator {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ScopedEnvironment env = new ScopedEnvironment();
        CalculatorParser parser = new CalculatorParser();
        EvaluationVisitor evaluator = new EvaluationVisitor();
        NamedConstantChecker namedChecker = new NamedConstantChecker();
        ReassignmentChecker reassignmentChecker = new ReassignmentChecker();

        int expressionsEntered = 0;
        int successfullyEvaluated = 0;
        int fullyEvaluated = 0;

        System.out.println("Welcome to the Symbolic Calculator!");
        System.out.println("Type 'Quit' to exit, 'Clear' to clear variables, or 'Vars' to see all variables.");

        while (true) {
            System.out.print("? ");
            if (!sc.hasNextLine()) {
                break; // End of input (EOF reached)
            }
            String input = sc.nextLine().trim();

            if (input.isEmpty() ) {
                continue;
            }

            try {
                SymbolicExpression expr = parser.parse(input, env);

                if (expr.isCommand()) {
                    CommandHandler.handleCommand(expr, env);
                    if (expr instanceof Quit) {
                        break;
                    }
                } else {
                    if (!namedChecker.check(expr)) {
                        System.out.println("Error, assignments to named constants:");
                        for (String illegal : namedChecker.getIllegalAssignments()) {
                            System.out.println("    " + illegal);
                        }
                        continue;
                    }

                    if (!reassignmentChecker.check(expr)) {
                        System.out.println("Error, the following variables are reassigned:");
                        for (String variable : reassignmentChecker.getReassignedVariables()) {
                            System.out.println("    " + variable);
                        }
                        continue;
                    }

                    expressionsEntered++;
                    SymbolicExpression result = evaluator.evaluate(expr, env);
                    successfullyEvaluated++;

                    if (result.isConstant()) {
                        fullyEvaluated++;
                    }

                    env.put(new Variable("ans"), result);
                    System.out.println(result);
                }
            } catch (IOException e) {
                System.out.println("Input Error: " + e.getMessage());
            } catch (IllegalAssignmentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SyntaxErrorException e) {
                System.out.println("Syntax Error: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }

        System.out.println("Exiting...");
        System.out.println("Goodbye!");
        System.out.println("Statistics:");
        System.out.println("    Total Expressions Entered: " + expressionsEntered);
        System.out.println("    Successfully Evaluated: " + successfullyEvaluated);
        System.out.println("    Fully Evaluated: " + fullyEvaluated);

        sc.close();
    }
}
