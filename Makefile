JUNIT = ./junit.jar

# Packages
CALC_PACK = org.ioopm.calculator
AST_PACK = $(CALC_PACK).ast
PARSE_PACK = $(CALC_PACK).parser

# Boiler plate paths
PACKAGE_DIR = org/ioopm/calculator
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

clean: 
	rm -rf classes

.PHONY: all clean ast parser build run
