import java.util.ArrayList;

public class Programa extends AST {
    private ArrayList<Declaracion> declaraciones;
    private Instruccion instruccion;

    public Programa(ArrayList<Declaracion> declaraciones, Instruccion instruccion) {
        this.declaraciones = declaraciones;
        this.instruccion = instruccion;
    }

    @Override
    public void imprimir(int nivel) {
    if (instruccion != null) {
        instruccion.imprimir(nivel);
    }
}
}