JFLEX = java -jar jflex.jar
JC = javac
JFLAGS = -d .
SRC = Lexer.java Main.java

all: compile

Lexer.java: Lexer.flex
	$(JFLEX) $<

compile: Lexer.java
	$(JC) $(JFLAGS) $(SRC)

clean:
	rm -f Lexer.java *.class

.PHONY: all compile clean
