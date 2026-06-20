import java_cup.runtime.Symbol;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

// Codigo que ejecuta el programa principal, se encarga de leer el archivo de entrada e
// instanciar tanto al lexer como al parser y ejecutar el análisis.
public class Main {

    public static void main(String[] args) {
        
        if (args.length == 0) {
            System.out.println("Error: Especifique un archivo .bot como argumento.");
            return;
        }

        String archivo = args[0];
            
        try {
            FileReader fileReader = new FileReader(args[0]);
            LexerCup lexer = new LexerCup(fileReader);            
            Parser parserInstance = new Parser(lexer);
            
            boolean tieneErroresLexicos = false;
            Symbol token;
            
            // Verificamos el archivo una vez para verificar exclusivamente léxico
            while ((token = lexer.next_token()).sym != ParserSym.EOF) {
            }
            
            if (lexer.hashErrors()) {
                // Si hubo errores abortamos inmediatamente sin invocar al Parser
                System.exit(1);
            }
            
            // Si no hubo errores léxicos, reiniciamos el lector para el Analizador Sintáctico
            fileReader = new FileReader(args[0]);
            lexer = new LexerCup(fileReader);
            parserInstance = new Parser(lexer);
            
            // Ejecutamos el parser e imprimimimos el AST
            AST arbol = (AST) parserInstance.parse().value;
            if (arbol != null) {
                arbol.imprimir(0);
            }
            
        } catch (Exception e) {
            System.exit(1);
        }
    }
}