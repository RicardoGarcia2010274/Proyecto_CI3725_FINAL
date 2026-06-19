import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        
        if (args.length == 0) {
            System.out.println("Error: Especifique un archivo .bot como argumento.");
            return;
        }

        String archivo = args[0];
            
        try {
            String contenido_archivo = Files.readString(Path.of(archivo));

            Reader strRdr = new StringReader(contenido_archivo);
            LexerCup lexer = new LexerCup(strRdr);
            Parser parser = new Parser(lexer);
          
            AST nodoAST = (AST) parser.parse().value;
            
            if (nodoAST != null) {
                nodoAST.imprimir(0);
            }

        } catch (Exception e) {
            System.err.println("Ocurrio un error durante la compilacion: " + e.getMessage());
        }
    }
}