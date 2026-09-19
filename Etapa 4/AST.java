// Implementacion de un nodo Arbol Sintactico Absracto
public abstract class AST {
	public int linea = 1;
	public int columna = 1;
	
	// Constructores
	public AST() {} // Este existe únicamente para la retrocompatibilidad con el resto del código ya hecho en entregas anteriores
	
	public AST(int linea, int columna) {
		this.linea = linea;
		this.columna = columna;
	}

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