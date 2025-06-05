# Symbolic Calculator

## Overview
This project implements a symbolic calculator capable of parsing, evaluating, and handling various mathematical expressions and commands, such as `Quit`, `Vars`, and `Clear`.

## Compilation and Execution

### Running Tests
1. To compile the files with **`Test.java`**, use the following command:

   make calculator

2. To run the tests:

   make test

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
