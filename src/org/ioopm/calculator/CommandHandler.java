package org.ioopm.calculator;

import org.ioopm.calculator.ast.*;

/**
 * Handles the execution of commands in the calculator (e.g., vars, clear, quit).
 */
public class CommandHandler {

    public static void handleCommand(SymbolicExpression command, Environment env) {
        if (command instanceof Quit) {
            // The main program prints the final exit message so
            // we simply return here without producing output.
            return;
        } else if (command instanceof Vars) {
            System.out.println(env.toString());
        } else if (command instanceof Clear) {
            env.clear();
            System.out.println("All variables cleared.");
        } else if (command instanceof End) {
            // End is handled by the main program when parsing functions
            return;
        } else {
            throw new RuntimeException("Unhandled command: " + command);
        }
    }
}

