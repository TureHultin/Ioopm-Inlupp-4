JUNIT = ./junit.jar

# Packages
CALC_PACK = org.ioopm.calculator
AST_PACK = $(CALC_PACK).ast
PARSE_PACK = $(CALC_PACK).parser

# Test packages
CALC_TEST_PACK = tests.ioopm.calculator
AST_TEST_PACK = $(CALC_TEST_PACK).ast
PARSE_TEST_PACK = $(CALC_TEST_PACK).parser

# Boiler plate paths
PACKAGE_DIR = org/ioopm/calculator
TEST_PACK_DIR = tests/ioopm/calculator
SRC_DIR = src/main/java
TEST_DIR = src/test/java

# Source code dirs
CALC_SRC_DIR = $(SRC_DIR)
AST_SRC_DIR = $(SRC_DIR)
PARSE_SRC_DIR = $(SRC_DIR)

all: clean build
	make run

ast_src:
	javac -d classes -sourcepath $(AST_SRC_DIR) $(AST_SRC_DIR)/$(PACKAGE_DIR)/ast/*.java 

parser_src: ast_src
	javac -d classes -cp classes -sourcepath $(PARSE_SRC_DIR) $(PARSE_SRC_DIR)/$(PACKAGE_DIR)/parser/*.java 

build: ast_src parser_src
	javac -d classes -cp classes -sourcepath $(CALC_SRC_DIR) $(CALC_SRC_DIR)/$(PACKAGE_DIR)/*.java 

run: build 
	java -cp classes $(CALC_PACK).Calculator 

ast_tests: ast_src
	javac -cp $(JUNIT) -d classes -sourcepath $(TEST_DIR) $(TEST_DIR)/*.java

run_tests: ast_tests
	java -jar  $(JUNIT) -cp classes -c $(AST_TEST_PACK).AstTests

file_test: build
	java -cp classes $(CALC_PACK).Calculator < src/test/resources/inputForTest.txt > src/test/resources/output.txt
	clear
	diff src/test/resources/output.txt src/test/resources/expectedOutputForTest.txt 

clean: 
	rm -rf classes

.PHONY: all clean ast parser build run
