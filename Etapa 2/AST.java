public abstract class AST {

    public abstract void imprimir(int nivel);

    protected void imprimirIndentacion(int nivel) {
        for (int i = 0; i < nivel; i++) {
            System.out.print("  ");
        }
    }
}