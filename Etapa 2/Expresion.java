public abstract class Expresion extends AST {
}

class ExpBinArit extends Expresion {
    private String op;
    private Expresion izq;
    private Expresion der;

    public ExpBinArit(String op, Expresion izq, Expresion der) {
        this.op = op;
        this.izq = izq;
        this.der = der;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("EXP_ARITMETICA: ");
        imprimirIndentacion(nivel + 1);
        System.out.println("- operacion: " + op);
        imprimirIndentacion(nivel + 1);
        System.out.println("- expresion izquierda: ");
        if (izq != null) izq.imprimir(nivel + 2);
        imprimirIndentacion(nivel + 1);
        System.out.println("- expresion derecha: ");
        if (der != null) der.imprimir(nivel + 2);
    }
}

class ExpBinBool extends Expresion {
    private String op;
    private Expresion izq;
    private Expresion der;

    public ExpBinBool(String op, Expresion izq, Expresion der) {
        this.op = op;
        this.izq = izq;
        this.der = der;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("EXP_BOOLEANA: ");
        imprimirIndentacion(nivel + 1);
        System.out.println("- operacion: " + op);
        imprimirIndentacion(nivel + 1);
        System.out.println("- expresion izquierda: ");
        if (izq != null) izq.imprimir(nivel + 2);
        imprimirIndentacion(nivel + 1);
        System.out.println("- expresion derecha: ");
        if (der != null) der.imprimir(nivel + 2);
    }
}

class ExpBinRelacional extends Expresion {
    private String op;
    private Expresion izq;
    private Expresion der;

    public ExpBinRelacional(String op, Expresion izq, Expresion der) {
        this.op = op;
        this.izq = izq;
        this.der = der;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("EXP_RELACIONAL: ");
        imprimirIndentacion(nivel + 1);
        System.out.println("- operacion: " + op);
        imprimirIndentacion(nivel + 1);
        System.out.println("- expresion izquierda: ");
        if (izq != null) izq.imprimir(nivel + 2);
        imprimirIndentacion(nivel + 1);
        System.out.println("- expresion derecha: ");
        if (der != null) der.imprimir(nivel + 2);
    }
}

class ExpUnaria extends Expresion {
    private String signo;

    private Expresion e;

    public ExpUnaria(String signo, Expresion e) {
        this.signo = signo;
        this.e = e;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("EXP_UNARIA: " + signo);
        if (e != null) e.imprimir(nivel + 1);
    }
}

class LiteralBool extends Expresion {
    private Boolean valor;

    public LiteralBool(Boolean valor) {
        this.valor = valor;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- literal booleano: " + valor);
    }
}

class LiteralEntero extends Expresion {
    private Integer num;

    public LiteralEntero(Integer num) {
        this.num = num;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- literal entero: " + num);
    }
}

class LiteralCaracter extends Expresion {
    private String caracter;
    public LiteralCaracter(String caracter) {
        this.caracter = (String) caracter;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- literal caracter: " + caracter);
    }
}

class Identificador extends Expresion {
    private String name;

    public Identificador(String name) {
        this.name = name;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- var: " + name);
    }
}