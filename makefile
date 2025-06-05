# Directories
SRC_DIR = src
TEST_DIR = src/test
CLASS_DIR = classes
RESULTS_DIR = test-results
MAVEN = mvn

# Compile all Java files with Calculator.java
all:
	@mkdir -p classes
	javac -d classes -sourcepath src $(SRC_DIR)/org/ioopm/calculator/*.java

# Compile all Java files with Test.java
calculator:
	@mkdir -p classes
	javac -d classes -sourcepath src src/org/ioopm/calculator/Test.java

# Run the program with Calculator.java
run:
	java -cp $(CLASS_DIR) org.ioopm.calculator.Calculator

# Run the program with Test.java
test:
	java -cp classes org.ioopm.calculator.Test

# Clean compiled classes
clean:
	rm -rf $(CLASS_DIR)

# Compile Maven Dependencies and Build
compile-tests:
	@echo "Compiling with Maven..."
	$(MAVEN) compile

# Run Unit and Integration Tests with Maven
junit-tests:
	@echo "Running JUnit Tests with Maven..."
	$(MAVEN) test

# Run System Tests with Input/Output Redirection
system-tests:
	@echo "Running System Tests..."
	@mkdir -p $(RESULTS_DIR)
	java -cp $(CLASS_DIR) org.ioopm.calculator.Calculator < $(SRC_DIR)/test/org/ioopm/calculator/system_tests/input1.txt > $(RESULTS_DIR)/output1.txt
	diff $(RESULTS_DIR)/output1.txt $(SRC_DIR)/test/org/ioopm/calculator/system_tests/expected_output1.txt || echo "Test 1 Failed"
	java -cp $(CLASS_DIR) org.ioopm.calculator.Calculator < $(SRC_DIR)/test/org/ioopm/calculator/system_tests/input2.txt > $(RESULTS_DIR)/output2.txt
	diff $(RESULTS_DIR)/output2.txt $(SRC_DIR)/test/org/ioopm/calculator/system_tests/expected_output2.txt || echo "Test 2 Failed"
	@echo "System Tests Completed"
