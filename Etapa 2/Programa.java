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
        imprimirIndentacion(nivel);
        System.out.println("PROGRAMA BOT");

        if (declaraciones != null && !declaraciones.isEmpty()) {
            imprimirIndentacion(nivel + 1);
            System.out.println("DECLARACIONES:");
            for (Declaracion decl : declaraciones) {
                decl.imprimir(nivel + 2);
            }
        }

        if (instruccion != null) {
            imprimirIndentacion(nivel + 1);
            System.out.println("EXECUTE:");
            instruccion.imprimir(nivel + 2);
        }
    }
}