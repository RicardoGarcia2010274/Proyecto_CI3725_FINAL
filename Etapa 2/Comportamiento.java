// Implementacion de clase destinada a gestionar los comportamientos en el lenguaje BOT
public class Comportamiento extends AST {
    private Condicion condicion;
    private Instruccion instruccion;

    public Comportamiento(Condicion condicion, Instruccion instruccion) {
        this.condicion = condicion;
        this.instruccion = instruccion;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- COMPORTAMIENTO: on");
        condicion.imprimir(nivel + 1);
        if (instruccion != null) {
            instruccion.imprimir(nivel + 1);
        }
    }
}