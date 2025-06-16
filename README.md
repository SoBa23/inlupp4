# Symbolic Calculator

## Overview
This project implements a symbolic calculator capable of parsing, evaluating, and handling various mathematical expressions and commands, such as `Quit`, `Vars`, and `Clear`.

## Language Features

### Scopes
Scopes are written using curly braces. Variables declared inside a scope are not visible outside of it. Example:

```
{ 5 = x }
```

### Conditionals
Conditionals follow the syntax `if <lhs> <op> <rhs> { <if body> } else { <else body> }` where `<op>` is one of `<`, `<=`, `>`, `>=` or `==`.

### Functions
Functions are introduced with the keyword `function` and terminated by `end`.
Parameters are given inside parentheses after the function name. The body consists
of a sequence of expressions where the last expression is the return value.

```
function max(x, y)
  if x < y { y } else { x }
end

max(5, 7)      # => 7
```


Functions may be recursive and can be called before they are defined.

## Compilation and Execution

### Running Tests
The project includes a small set of system tests that can be executed without
Maven:

```
make all
make system-tests
```

For full unit tests Maven is required; see the section below on how to set up a
local Maven installation.

### Running the Calculator
1. To compile all the files with **`Calculator.java`**:

   make all

2. To run the calculator:

   make run

## Maven Support
Some targets in the `makefile` rely on Maven (e.g. `compile-tests` and `junit-tests`). The container used for automated testing does not have Maven pre-installed and does not allow `apt-get` to install it. If you have Maven installed on your machine you can set it up locally in this repository using the helper script `scripts/setup-maven.sh`.

```bash
# from the project root
./scripts/setup-maven.sh
```

This downloads a local Maven distribution into `tools/maven` and prints instructions for updating your `PATH`. Once Maven is available, the Maven-based make targets will work normally:

```bash
make compile-tests
make junit-tests
```
