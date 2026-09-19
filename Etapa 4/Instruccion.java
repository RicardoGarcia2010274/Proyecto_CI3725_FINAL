import java.util.ArrayList;

// Implementación de clase para las instrucciones válidas en el lenguaje BOT
// Incluye tanto las instrucciones de controlador como las de robot
public abstract class Instruccion extends AST {
	public int linea, columna;
}

class Declaracion extends Instruccion {
    private String tipo;
    private ArrayList<Identificador> identificadores;
    private ArrayList<Comportamiento> comportamientos;
	
	// Constructores
	public Declaracion(String tipo, ArrayList<Identificador> identificadores, ArrayList<Comportamiento> comportamientos) {
		this(tipo, identificadores, comportamientos, 1, 1);
	}
    public Declaracion(String tipo, ArrayList<Identificador> identificadores, ArrayList<Comportamiento> comportamientos, int linea, int columna) {
        this.tipo = tipo;
        this.identificadores = identificadores;
        this.comportamientos = comportamientos;
		this.linea = linea;
		this.columna = columna;
    }
	
	public String getTipo() { return this.tipo; }
	public ArrayList<Identificador> getIdentificadores() { return this.identificadores; }
	public ArrayList<Comportamiento> getComportamientos() { return this.comportamientos; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- DECLARACION " + tipo + ":");
        if (identificadores != null) {
            for (Identificador id : identificadores) {
                if (id != null) {
                    id.imprimir(nivel + 1);
                } 
            }
        }
        if (comportamientos != null) {
            for (Comportamiento comp : comportamientos) {
                comp.imprimir(nivel + 1);
            }
        }
    }
}

class IncorpAlcance extends Instruccion {
    private ArrayList<Declaracion> d; // Cambiado de 'Declaracion' a 'ArrayList<Declaracion>'
    private Instruccion i;
	
	// Constructores
	public IncorpAlcance(ArrayList<Declaracion> d, Instruccion i) {
		this(d, i, 1, 1);
	}
	
    public IncorpAlcance(ArrayList<Declaracion> d, Instruccion i, int linea, int columna) {
        this.d = d;
        this.i = i;
		this.linea = linea;
		this.columna = columna;
    }
	
	public ArrayList<Declaracion> getDeclaraciones() { return this.d; }
	public Instruccion getInstruccion() { return this.i; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("- INCORPORACION DE ALCANCE");
        if (d != null) {
            for (Declaracion dec : d) dec.imprimir(nivel + 1);
        }
        if (i != null) {
            i.imprimir(nivel + 1);
        }
    }
}

class Activacion extends Instruccion {
    private ArrayList<Identificador> identificadores;
	
	// Constructores
	public Activacion(ArrayList<Identificador> identificadores) {
		this(identificadores, 1, 1);
	}
    public Activacion(ArrayList<Identificador> identificadores, int linea, int columna) {
		this.identificadores = identificadores;
		this.linea = linea;
		this.columna = columna;
    }
	
	public ArrayList<Identificador> getIdentificadores() {
		return this.identificadores;
	}

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("ACTIVACION");
        if (identificadores != null) {
            for (Identificador id : identificadores) {
                if (id != null) {
                    id.imprimir(nivel + 1);
                } 
            }
        }
    }
}

class Desactivacion extends Instruccion {
    private ArrayList<Identificador> identificadores;
	
	// Constructores
	public Desactivacion(ArrayList<Identificador> identificadores) {
		this(identificadores, 1, 1);
	}
    public Desactivacion(ArrayList<Identificador> identificadores, int linea, int columna) {
        this.identificadores = identificadores;
		this.linea = linea;
		this.columna = columna;
    }
	
	public ArrayList<Identificador> getIdentificadores() { return this.identificadores; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("DESACTIVACION");
        if (identificadores != null) {
            for (Identificador id : identificadores) {
                if (id != null) {
                    id.imprimir(nivel + 1);
                }
            }
        }
    }
}

class Avance extends Instruccion {
    private ArrayList<Identificador> identificadores;
	
	// Constructores
	public Avance(ArrayList<Identificador> identificadores) {
		this(identificadores, 1, 1);
	}
    public Avance(ArrayList<Identificador> identificadores, int linea, int columna) {
        this.identificadores = identificadores;
		this.linea = linea;
		this.columna = columna;
    }
	
	public ArrayList<Identificador> getIdentificadores() { return this.identificadores; }

    @Override
    public void imprimir(int nivel) {
        imprimir(nivel, true);
    }

    @Override
    public void imprimir(int nivel, boolean indentarPrimeraLinea) {
        if (indentarPrimeraLinea) {
            imprimirIndentacion(nivel);
        }
        System.out.println("AVANCE");
        
        if (identificadores != null) {
            for (Identificador id : identificadores) {
                if (id != null) id.imprimir(nivel + 1);
            }
        }
    }
}

class Secuenciacion extends Instruccion {
    private Instruccion i1;
    private Instruccion i2;
	
	// Constructores
	public Secuenciacion(Instruccion i1, Instruccion i2) {
		this(i1, i2, 1, 1);
	}
    public Secuenciacion(Instruccion i1, Instruccion i2, int linea, int columna) {
        this.i1 = i1;
        this.i2 = i2;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Instruccion getI1() { return this.i1; }
	public Instruccion getI2() { return this.i2; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("SECUENCIACION");
        if (i1 != null) i1.imprimir(nivel + 1);
        if (i2 != null) i2.imprimir(nivel + 1);
}
}

class Entrada extends Instruccion {
    private Identificador id;

	// Constructores
	public Entrada(Identificador id) {
		this(id, 1, 1);
	}
    public Entrada(Identificador id, int linea, int columna) {
        this.id = id;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Identificador getId() { return this.id; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("ENTRADA");
        if (id != null) {
            id.imprimir(nivel + 1);
        }
    }
}

class Salida extends Instruccion {
    private Expresion e;
	
	// Constructores
	public Salida(Expresion e) {
		this(e, 1, 1);
	}
    public Salida(Expresion e, int linea, int columna) {
        this.e = e;
		this.linea = linea;
		this.columna = columna;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("SALIDA");
        
        if (e != null) {
            e.imprimir(nivel + 1);
        }
    }
}

class Movimiento extends Instruccion {
    private String direccion;
    private Expresion e;
	
	// Constructores
	public Movimiento(String direccion, Expresion e) {
		this(direccion, e, 1, 1);
	}
    public Movimiento(String direccion, Expresion e, int linea, int columna) {
        this.direccion = direccion;
        this.e = e;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Expresion getExpresion() { return this.e; }
	public String getDireccion() { return this.direccion; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("MOVIMIENTO:" + direccion);
        if (e != null) {
            e.imprimir(nivel + 1);
        }
}
}

class Soltado extends Instruccion {
    private Expresion e;
	
	// Constructores
	public Soltado(Expresion e) {
		this(e, 1, 1);
	}
    public Soltado(Expresion e, int linea, int columna) {
        this.e = e;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Expresion getExpresion() { return this.e; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("SOLTADO");
        e.imprimir(nivel + 1);
    }
}

class Coleccion extends Instruccion {
    private Identificador id;

	// Constructores
	public Coleccion(Identificador id) {
		this(id, 1, 1);
	}
    public Coleccion(Identificador id, int linea, int columna) {
        this.id = id;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Identificador getId() { return this.id; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("COLECCION");
        if (id != null) {
            id.imprimir(nivel + 1);
        }
    }
}

class Almacenamiento extends Instruccion {
    private Expresion e;

	// Constructores
	public Almacenamiento(Expresion e) {
		this(e, 1, 1);
	}
    public Almacenamiento(Expresion e, int linea, int columna) {
        this.e = e;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Expresion getExpresion() { return this.e; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("ALMACENAMIENTO");
        e.imprimir(nivel + 1);
    }
}

class IteracionIndet extends Instruccion {
    private Expresion e;
    private Instruccion i;
	
	// Constructores
	public IteracionIndet(Expresion e, Instruccion i) {
		this(e, i, 1, 1);
	}
    public IteracionIndet(Expresion e, Instruccion i, int linea, int columna) {
        this.e = e;
        this.i = i;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Expresion getExpresion() { return this.e; }
	public Instruccion getInstruccion() { return this.i; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("ITERACION INDETERMINADA");
        
        if (e != null) {
            imprimirIndentacion(nivel + 1);
            System.out.print("- guardia: ");
            e.imprimir(nivel + 1, false); 
        }
        
        if (i != null) {
            imprimirIndentacion(nivel + 1);
            System.out.print("- exito: ");
            i.imprimir(nivel + 1, false);
        }
    }
}

class Condicional extends Instruccion {
    private Expresion e;    
    private Instruccion i1;
    private Instruccion i2;
	
	// Constructores
	public Condicional(Expresion e, Instruccion i1, Instruccion i2) {
		this(e, i1, i2, 1, 1);
	}
    public Condicional(Expresion e, Instruccion i1, Instruccion i2, int linea, int columna) {
        this.e = e;
        this.i1 = i1;
        this.i2 = i2;
		this.linea = linea;
		this.columna = columna;
    }
	
	public Expresion getExpresion() { return this.e; }
	public Instruccion getI1() { return this.i1; }
	public Instruccion getI2() { return this.i2; }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("CONDICIONAL");
        
        if (e != null) {
            imprimirIndentacion(nivel + 1);
            System.out.print("- guardia: ");
            e.imprimir(nivel + 1, false); 
        }
        
        if (i1 != null) {
            imprimirIndentacion(nivel + 1);
            System.out.print("- exito: ");
            i1.imprimir(nivel + 1, false);
        }
        
        if (i2 != null) {
            imprimirIndentacion(nivel + 1);
            System.out.print("- fracaso: ");
            i2.imprimir(nivel + 1, false);
        }
    }
}