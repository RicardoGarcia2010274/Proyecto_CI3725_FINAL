import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/* Archivo principal, encargado de leer el archivo de extensión .bot, 
interpretar el contenido del mismo e imprimir la salida de la forma correcta.
*/
public class Main {

    public static void main(String[] args) {
        
        if (args.length == 0) {
            System.out.println("Error: Especifique un archivo .bot como argumento.");
            return;
        }

        String archivo = args[0];
            
        try {
            // Lectura limpia del archivo de entrada
            String contenido_archivo = Files.readString(Path.of(archivo));

            // Enlace de los flujos: Archivo -> JFlex (LexerCup) -> CUP (Parser)
            Reader strRdr = new StringReader(contenido_archivo);
            LexerCup lexer = new LexerCup(strRdr);
            Parser parser = new Parser(lexer);
          
            // Ejecución del análisis sintáctico y construcción del AST
            AST nodoAST = (AST) parser.parse().value;
            
            // Si el parser no retornó null (parseo exitoso), imprimimos recursivamente
            if (nodoAST != null) {
                nodoAST.imprimir(0);
            }

        } catch (Exception e) {
            System.err.println("Ocurrio un error durante la compilacion: " + e.getMessage());
        }
    }
}