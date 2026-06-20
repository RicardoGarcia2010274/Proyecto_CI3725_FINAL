import java.util.ArrayList;

// Implementación de clase para las instrucciones válidas en el lenguaje BOT
// Incluye tanto las instrucciones de controlador como las de robot
public abstract class Instruccion extends AST {
}

class Declaracion extends Instruccion {
    private String tipo;
    private ArrayList<Identificador> identificadores;
    private ArrayList<Comportamiento> comportamientos;

    public Declaracion(String tipo, ArrayList<Identificador> identificadores, ArrayList<Comportamiento> comportamientos) {
        this.tipo = tipo;
        this.identificadores = identificadores;
        this.comportamientos = comportamientos;
    }

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

    public IncorpAlcance(ArrayList<Declaracion> d, Instruccion i) {
        this.d = d;
        this.i = i;
    }

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

    public Activacion(ArrayList<Identificador> identificadores) {
        this.identificadores = identificadores;
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

    public Desactivacion(ArrayList<Identificador> identificadores) {
        this.identificadores = identificadores;
    }

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

    public Avance(ArrayList<Identificador> identificadores) {
        this.identificadores = identificadores;
    }

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

    public Secuenciacion(Instruccion i1, Instruccion i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

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

    public Entrada(Identificador id) {
        this.id = id;
    }

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

    public Salida(Expresion e) {
        this.e = e;
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

    public Movimiento(String direccion, Expresion e) {
        this.direccion = direccion;
        this.e = e;
    }

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

    public Soltado(Expresion e) {
        this.e = e;
    }

    @Override
    public void imprimir(int nivel) {
        imprimirIndentacion(nivel);
        System.out.println("SOLTADO");
        e.imprimir(nivel + 1);
    }
}

class Coleccion extends Instruccion {
    private Identificador id;

    public Coleccion(Identificador id) {
        this.id = id;
    }

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

    public Almacenamiento(Expresion e) {
        this.e = e;
    }

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

    public IteracionIndet(Expresion e, Instruccion i) {
        this.e = e;
        this.i = i;
    }

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

    public Condicional(Expresion e, Instruccion i1, Instruccion i2) {
        this.e = e;
        this.i1 = i1;
        this.i2 = i2;
    }

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