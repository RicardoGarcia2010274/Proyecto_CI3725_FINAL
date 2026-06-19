public abstract class Condicion extends AST {
    public abstract void imprimir(int nivel);
}

class CondActivation extends Condicion {
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- Condicion: activation");
    }
}
class CondDeactivation extends Condicion {
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- Condicion: deactivation");
    }
}
class CondDefault extends Condicion {
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- Condicion: default");
    }
}

class CondExpresion extends Condicion {
    private Expresion expresion;

    public CondExpresion(Expresion expresion) {
        this.expresion = expresion;
    }

    @Override public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- condicion: [Expresion]");
        expresion.imprimir(nivel + 1);
    }
}