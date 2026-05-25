//Estas son las libreriaas que utilizamos
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

//este es el archivo principal
public class Main {

  public static void main(String[] args) throws Exception {

    // Ruta del archivo que voy a leer, debe estar en la carpeta src
	
    String ruta_archivo = args[1];
		
		//estructura try catch, que sirve para que el programa no explote si hay un fallo al leer el archivo 
    try {
      
			// guardamos el contenido del archivo en una variable llamada contenido_archivo 
      String contenido_archivo = Files.readString(Path.of(ruta_archivo));

			//esta parte del codigo es la encargada de interpretar el contenido del archivo
      Reader strRdr = new StringReader(contenido_archivo);
      Lexer lexer = new Lexer(strRdr);
      
			//generamos un objeto de tipo Token
      Token tk;

			//salida guarda un string con los tokens que interpreto el lexer sin errores 
      String salida = ""; //salida sin errores 
			String errores =""; //salida con errores
			
			//bucle principal, donde leemos todo el contenido del archivo test.txt
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