JFLEX = java -jar jflex.jar
CUP = java -jar java-cup-11b.jar
JC = javac
JFLAGS = -cp ".:java-cup-11b-runtime.jar" -d .

SRC = *.java

all: compile

LexerCup.java: LexerCup.flex
	$(JFLEX) $<

Parser.java: Parser.cup
	$(CUP) $<

compile: LexerCup.java Parser.java ParserSym.java
	$(JC) $(JFLAGS) $(SRC)

clean:
	rm -f LexerCup.java Parser.java ParserSym.java *.class

.PHONY: all compile clean
