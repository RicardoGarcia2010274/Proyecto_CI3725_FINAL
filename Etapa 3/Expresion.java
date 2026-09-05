// Implementación de clase para las expresiones válidas en el lenguaje BOT
public abstract class Expresion extends AST {
	public int linea = 1;
	public int columna = 1;
}

class ExpBinArit extends Expresion {
    private String op;
    private Expresion izq;
    private Expresion der;

	// Constructores
	public ExpBinArit(String op, Expresion izq, Expresion der) {
        this(op, izq, der, 1, 1);
    }

    public ExpBinArit(String op, Expresion izq, Expresion der, int linea, int columna) {
        this.op = op;
        this.izq = izq;
        this.der = der;
		this.linea = linea;
		this.columna = columna;
    }
	
	public String getOp() { return this.op; }
	public Expresion getIzq() { return this.izq; }
	public Expresion getDer() { return this.der; }
	
    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("EXP_ARITMETICA");
        imprimirIndentacion(nivel + 1);
        System.out.println("- operacion: " + op);
        imprimirIndentacion(nivel + 1);
        System.out.println("- operador izquierdo: ");
        if (izq != null) izq.imprimir(nivel + 2);
        imprimirIndentacion(nivel + 1);
        System.out.println("- operador derecho: ");
        if (der != null) der.imprimir(nivel + 2);
    }
}

class ExpBinBool extends Expresion {
    private String op;
    private Expresion izq;
    private Expresion der;

	// Constructores
	public ExpBinBool(String op, Expresion izq, Expresion der) {
        this(op, izq, der, 1, 1);
    }
	
    public ExpBinBool(String op, Expresion izq, Expresion der, int linea, int columna) {
        this.op = op;
        this.izq = izq;
        this.der = der;
		this.linea = linea;
		this.columna = columna;
    }
	
	public String getOp() { return this.op; }
	public Expresion getIzq() { return this.izq; }
	public Expresion getDer() { return this.der; }

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

	// Constructores
    public ExpBinRelacional(String op, Expresion izq, Expresion der) {
        this(op, izq, der, 1, 1);
    }
	
    public ExpBinRelacional(String op, Expresion izq, Expresion der, int linea, int columna) {
        this.op = op;
        this.izq = izq;
        this.der = der;
		this.linea = linea;
		this.columna = columna;
    }
	
	public String getOp() { return this.op; }
	public Expresion getIzq() { return this.izq; }
	public Expresion getDer() { return this.der; }

    @Override
    public void imprimir(int nivel) {
        imprimir(nivel, true);
    }

    @Override
    public void imprimir(int nivel, boolean indentarPrimeraLinea) {
        if (indentarPrimeraLinea) {
            imprimirIndentacion(nivel);
        }
        System.out.println("BIN_RELACIONAL");
        
        imprimirIndentacion(nivel + 1);
        System.out.println("- operacion: " + op);
        
        imprimirIndentacion(nivel + 1);
        System.out.println("- operador izquierdo:");
        if (izq != null) izq.imprimir(nivel + 2);
        
        imprimirIndentacion(nivel + 1);
        System.out.println("- operador derecho:");
        if (der != null) der.imprimir(nivel + 2);
    }
}

class ExpUnaria extends Expresion {
    private String signo;

    private Expresion e;
	
	// Constructores
    public ExpUnaria(String signo, Expresion e) {
        this(signo, e, 1, 1);
    }
	
	public ExpUnaria(String signo, Expresion e, int linea, int columna) {
        this.signo = signo;
        this.e = e;
		this.linea = linea;
		this.columna = columna;
    }
	
	public String getSigno() { return this.signo; }
	public Expresion getE() { return this.e; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("EXP_UNARIA"+"("+ signo + ")");
        if (e != null) {
            e.imprimir(nivel + 1);
        }
    }
}

class LiteralBool extends Expresion {
    private Boolean valor;
	
	public LiteralBool(Boolean valor) {
        this(valor, 1, 1);
    }
    public LiteralBool(Boolean valor, int linea, int columna) {
        this.valor = valor;
		this.linea = linea;
		this.columna = columna;
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
        this(num, 1, 1);
    }
    public LiteralEntero(Integer num, int linea, int columna) {
        this.num = num;
		this.linea = linea;
		this.columna = columna;
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
        this(caracter, 1, 1);
    }
    public LiteralCaracter(String caracter, int linea, int columna) {
        this.caracter = (String) caracter;
		this.linea = linea;
		this.columna = columna;
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
        this(name, 1, 1);
    }

    public Identificador(String name, int linea, int columna) {
        this.name = name;
		this.linea = linea;
		this.columna = columna;
    }
	
	public String getName() { return this.name; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- var: " + name);
    }
}