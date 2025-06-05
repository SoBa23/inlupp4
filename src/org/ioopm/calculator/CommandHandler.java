package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;

/**
 * Handles the execution of commands in the calculator (e.g., vars, clear, quit).
 */
public class CommandHandler {

    public static void handleCommand(SymbolicExpression command, Environment env) {
        if (command instanceof Quit) {
            System.out.println("Exiting...");
        } else if (command instanceof Vars) {
            System.out.println(env.toString());
        } else if (command instanceof Clear) {
            env.clear();
            System.out.println("All variables cleared.");
        } else {
            throw new RuntimeException("Unhandled command: " + command);
        }
    }
}

