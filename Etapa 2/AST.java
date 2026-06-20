// Implementacion de un nodo Arbol Sintactico Absracto
public abstract class AST {

    public abstract void imprimir(int nivel);

    public void imprimir(int nivel, boolean indentarPrimeraLinea) {
        if (indentarPrimeraLinea) {
            imprimirIndentacion(nivel);
        }
        imprimirSinIndentacionInicial(nivel);
    }

    protected void imprimirSinIndentacionInicial(int nivel) {
        imprimir(nivel);
    }

    protected void imprimirIndentacion(int nivel) {
        for (int i = 0; i < nivel; i++) {
            System.out.print("  ");
        }
    }
}