//Librerías que se utilizaron
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/* Archivo principal, encargado de leer el archivo de extensión .bot, 
interpretar el contenido del mismo e imprimir la salida de la forma correcta
dependiendo de si hay errores o no.
*/
public class Main {

 public static void main(String[] args) {
	
	if (args.length == 0) {
		System.out.println("Especifique un archivo.bot");
		return;
	}

    // Nombre del archivo de extensión .bot a leer, debe estar en la carpeta "Etapa 1" 
	// y ser dado como argumento en la linea de comandos
	
    String ruta_archivo = args[0];
		
		//Estructura try catch para que el programa maneje correctamente los fallos al leer el archivo. 
    try {
      
      String contenido_archivo = Files.readString(Path.of(ruta_archivo));

			// Enlace con la herramienta JFlex a través de la clase Lexer.
			// Es la parte que interpreta el archivo
      Reader strRdr = new StringReader(contenido_archivo);
      Lexer lexer = new Lexer(strRdr);
      
			//generamos un objeto de tipo Token
      Token tk;

			// salida es una variable que guarda un string con todos los tokens que interpretó el
			// lexer sin errores y en orden
      String salida = "";
			String errores =""; // Variable que guarda los errores hallados
			
			// Bucle principal, donde se lee todo el contenido del archivo de entrada
      while ((tk = lexer.yylex()) != null){
				
				if (tk.tipo() == TokenConstants.TkFinal){
					break;
				} else if (tk.tipo() == TokenConstants.TkError){
					String new_error = "Error: Caracter inesperado \"" + tk.lexema()+ "\" en la fila " +tk.fila()+ ", columna " +tk.columna() +"\n";
					errores = errores + new_error;
				} else {
					String new_token = tk + " ";
					salida = salida + new_token;
					}
				}
      
			if (errores.length() == 0){
				System.out.println(salida);
			} else {
				System.err.println(errores);
			}
      
    } catch (IOException e) {
      System.err.println("Ocurrio un error al leer el archivo: "+ e.getMessage());
    }

  }
}
