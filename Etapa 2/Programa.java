import java.util.ArrayList;

public class Programa extends AST {
    private ArrayList<Declaracion> declaraciones;
    private Instruccion cuerpoControlador;

    public Programa(ArrayList<Declaracion> declaraciones, Instruccion cuerpoControlador) {
        this.declaraciones = declaraciones;
        this.cuerpoControlador = cuerpoControlador;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("PROGRAMA BOT");

        if (declaraciones != null && !declaraciones.isEmpty()) {
            imprimirIndentacion(nivel + 1);
            System.out.println("SECCION DECLARACIONES:");
            for (Declaracion decl : declaraciones) {
                decl.imprimir(nivel + 2);
            }
        }

        if (cuerpoControlador != null) {
            imprimirIndentacion(nivel + 1);
            System.out.println("CUERPO PRINCIPAL (EXECUTE):");
            cuerpoControlador.imprimir(nivel + 2);
        }
    }
}