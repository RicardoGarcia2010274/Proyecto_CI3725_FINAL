//Implementación de clase para la estructura de un programa en lenguaje BOT
import java.util.ArrayList;

public class Programa extends AST {
    private ArrayList<Declaracion> declaraciones;
    private Instruccion instruccion;

    public Programa(ArrayList<Declaracion> declaraciones, Instruccion instruccion) {
        this.declaraciones = declaraciones;
        this.instruccion = instruccion;
    }
	
	public ArrayList<Declaracion> getDeclaraciones() { return this.declaraciones; }
	public Instruccion getInstruccion() { return this.instruccion; }

    @Override
    public void imprimir(int nivel) {
    if (instruccion != null) {
        instruccion.imprimir(nivel);
    }
}
}