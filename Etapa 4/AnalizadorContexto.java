import java.util.ArrayList;
import java.util.List;

public class AnalizadorContexto {
	private List<String> errores;
	private TablaSimbolos tablaActual;
	private Tipo tipoBotActual = Tipo.ERROR;
	private boolean dentroDeComportamiento;
	
	// Constructor
	public AnalizadorContexto() {
		this.errores = new ArrayList<>();
		this.tablaActual = new TablaSimbolos(null);
		this.dentroDeComportamiento = false;
	}
	
	public List<String> getErrores() { return this.errores; }
	
	public boolean tieneErrores() { return !this.errores.isEmpty(); }
	
	private void reportarError(String msg) {
		errores.add("Error de contexto: " + msg);
	}
	
	public void analizar(AST raiz) {
		if (raiz instanceof Programa) {
			Programa prog = (Programa) raiz;
			
			// Procesa declaraciones del alcance global
			if (prog.getDeclaraciones() != null) {
				for (Declaracion dec : prog.getDeclaraciones()) {
					analizarDeclaracion(dec);
				}
			}
			
			// Procesa el bloque principal de instrucciones
			if (prog.getInstruccion() != null) {
				analizarInstruccion(prog.getInstruccion());
			}
		}
	}
	
	private void analizarDeclaracion(Declaracion dec) {
		Tipo tipo = Tipo.fromString(dec.getTipo());
		
		// Registra cada robot declarado en el alcance actual
		if (dec.getIdentificadores() != null) {
			for (Identificador id : dec.getIdentificadores()) {
				Simbolo nuevoSimbolo = new Simbolo(id.getName(), tipo, true, id.linea, id.columna);
				
				// Intenta insertar en la tabla actual
				boolean insertado = tablaActual.insertar(nuevoSimbolo);
				if (!insertado) {
					reportarError("Redeclaración de la variable '" + id.getName() + "' en el mismo alcance (línea " + id.linea + ", col " + id.columna + ").");
				}
			}
		}
		
		// Revisa cada comportamiento definido para este tipo de bot
		if (dec.getComportamientos() != null) {
			for (Comportamiento comp : dec.getComportamientos()) {
				analizarComportamiento(comp, tipo);
			}
		}
	}
	
	private void analizarInstruccion(Instruccion inst) {
		if (inst == null) return;
		
		// Caso: Nuevo alcance
		if (inst instanceof IncorpAlcance) {
			IncorpAlcance inc = (IncorpAlcance) inst;
			
			// Se crea la tabla para el nuevo alcance
			tablaActual = new TablaSimbolos(tablaActual);
			
			if (inc.getDeclaraciones() != null) {
				for (Declaracion dec : inc.getDeclaraciones()) {
					analizarDeclaracion(dec);
				}
			}
			
			// Analiza el bloque de instrucciones dentro del alcance
			if (inc.getInstruccion() != null) {
				analizarInstruccion(inc.getInstruccion());
			}
			
			// Nos devolvemos al alcance anterior
			tablaActual = tablaActual.getPadre();
		// Caso: secuenciación de instrucciones
		} else if (inst instanceof Secuenciacion) {	
			Secuenciacion sec = (Secuenciacion) inst;
			analizarInstruccion(sec.getI1());
			analizarInstruccion(sec.getI2());
		// Casos para Activación, Desactivación y Avance
		} else if (inst instanceof Activacion) {
			verificarListaIdentificadores(((Activacion) inst).getIdentificadores());
		} else if (inst instanceof Desactivacion) {
			verificarListaIdentificadores(((Desactivacion) inst).getIdentificadores());
		} else if (inst instanceof Avance) {
			verificarListaIdentificadores(((Avance) inst).getIdentificadores());
			
		// Caso: Lectura o colección que se guarda en un identificador
		} else if (inst instanceof Entrada) {
			Entrada e = (Entrada) inst;
			if (e.getId() != null) {
				Simbolo simbolo = new Simbolo(e.getId().getName(), Tipo.UNKNOWN, false, e.getId().linea, e.getId().columna);
				if (!tablaActual.insertar(simbolo)) {
					reportarError("Redeclaración de la variable '" + e.getId().getName() + "' en el mismo alcance (línea " + e.getId().linea + ", col " + e.getId().columna + ").");
				}
			}
		} else if (inst instanceof Coleccion) {
			Coleccion c = (Coleccion) inst;
			if (c.getId() != null) {
				Simbolo simbolo = new Simbolo(c.getId().getName(), Tipo.UNKNOWN, false, c.getId().linea, c.getId().columna);
				if (!tablaActual.insertar(simbolo)) {
					reportarError("Redeclaración de la variable '" + c.getId().getName() + "' en el mismo alcance (línea " + c.getId().linea + ", col " + c.getId().columna + ").");
				}
			}
		// Caso: Condicionales
		} else if (inst instanceof Condicional) {
			Condicional c = (Condicional) inst;
			Tipo t = evaluarExpresion(c.getExpresion());
			if (t != Tipo.BOOL && t != Tipo.UNKNOWN && t != Tipo.ERROR) {
				reportarError("La guardia del 'if' debe ser de tipo bool (se obtuvo " + t + ") (línea " + c.linea + ", col " + c.columna + ").");
			}
			analizarInstruccion(c.getI1());
			if (c.getI2() != null) {
				analizarInstruccion(c.getI2());
			}
		// Caso iteración while
		} else if (inst instanceof IteracionIndet) {
			IteracionIndet it = (IteracionIndet) inst;
			Tipo t = evaluarExpresion(it.getExpresion());
			if (t != Tipo.BOOL && t != Tipo.UNKNOWN && t != Tipo.ERROR) {
				reportarError("La condición del 'while' debe ser de tipo bool (se obtuvo " + t + ") (línea " + it.linea + ", col " + it.columna + ").");
			}
			analizarInstruccion(it.getInstruccion());
		// Caso 'store e'
		} else if (inst instanceof Almacenamiento) {
			Almacenamiento al = (Almacenamiento) inst;
			Tipo t = evaluarExpresion(al.getExpresion());
			
			// store guarda el valor en el robot actual, por lo que su tipo debe coincidir
			if (dentroDeComportamiento && t != tipoBotActual && t != Tipo.UNKNOWN && t != Tipo.ERROR) {
				reportarError("Tipo incompatible en 'store'. Se esperaba " + tipoBotActual + " pero se obtuvo " + t + " (línea " + al.linea + ", col " + al.columna + ").");
			}
		// Caso 'drop e'
		} else if (inst instanceof Soltado) {
			evaluarExpresion(((Soltado) inst).getExpresion());
		// Caso movimiento (left, right, up, down)
		} else if (inst instanceof Movimiento) {
			Movimiento m = (Movimiento) inst;
			if (m.getExpresion() != null) {
				Tipo t = evaluarExpresion(m.getExpresion());
				if (t != Tipo.INT && t != Tipo.UNKNOWN && t != Tipo.ERROR) {
					reportarError("El argumento de movimiento debe ser de tipo int (se obtuvo " + t + ") (línea " + m.linea + ", col " + m.columna + ").");
				}
			}
		//
		}
	}
	
	private void verificarListaIdentificadores(List<Identificador> ids) {
		if (ids == null) return;
		for (Identificador id : ids) {
			verificarExisteIdentificador(id);
		}
	}
	
	private void verificarExisteIdentificador(Identificador id) {
		if (id.getName().equals("me")) {
			reportarError("Identificador especial 'me' no permitido en esta instrucción (línea " + id.linea + ", col " + id.columna + ").");
			return;
		}
		
		Simbolo simbolo = tablaActual.buscar(id.getName());
		if (simbolo == null) {
			reportarError("Robot '" + id.getName() + "' no declarado (línea " + id.linea + ", col " + id.columna + ").");
		}
	}
	
	private void analizarComportamiento(Comportamiento comp, Tipo tipoBot) {
		// Guarda el estado previo
		boolean prevEstado = this.dentroDeComportamiento;
		Tipo prevTipoBot = this.tipoBotActual;
		
		this.dentroDeComportamiento = true;
		this.tipoBotActual = tipoBot;
		
		// Cada comportamiento tiene su propia alcance local para variables con 'as'
		tablaActual = new TablaSimbolos(tablaActual);
		
		// Si la condición de comportamiento tiene una expresión, se revisa
		if (comp.getCondicion() instanceof CondExpresion) {
			CondExpresion ce = (CondExpresion) comp.getCondicion();
			Tipo tCond = evaluarExpresion(ce.getExpresion());
			if (tCond != Tipo.BOOL && tCond != Tipo.ERROR) {
				reportarError("La condición en 'on' debe ser de tipo bool (se obtuvo " + tCond + ") (línea " + ce.getExpresion().linea + ", col " + ce.getExpresion().columna + ").");
			}
		}
		
		// Revisa las instrucciones internas del robot
		if (comp.getInstruccion() != null) {
			analizarInstruccion(comp.getInstruccion());
		}
		
		// Restaura el estado previo
		tablaActual = tablaActual.getPadre();
		this.dentroDeComportamiento = prevEstado;
		this.tipoBotActual = prevTipoBot;
	}
	
	// Analiza expresiones completas de manera recursiva
	private void analizarExpresion(Expresion exp) {
		if (exp == null) return;
		
		// Verifica identificadores
		if (exp instanceof Identificador) {
			Identificador id = (Identificador) exp;
			if (id.getName().equals("me")) {
				if (!dentroDeComportamiento) {
					reportarError("Utilización de la palabra reservada 'me' fuera de un comportamiento (línea " + id.linea + ", col " + id.columna + ").");
				}
			} else {
				verificarExisteIdentificador(id);
			}
			
		// Verifica expresiones aritméticas
		} else if (exp instanceof ExpBinArit) {
			ExpBinArit b = (ExpBinArit) exp;
			analizarExpresion(b.getIzq());
			analizarExpresion(b.getDer());
			
		// Verifica expresiones booleanas
		} else if (exp instanceof ExpBinBool) {
			ExpBinBool b = (ExpBinBool) exp;
			analizarExpresion(b.getIzq());
			analizarExpresion(b.getDer());
			
		// Verifica expresiones unarias
		} else if (exp instanceof ExpUnaria) {
			analizarExpresion(((ExpUnaria) exp).getE());
		}
	}
	
	private Tipo evaluarExpresion(Expresion exp) {
		if (exp == null) return Tipo.ERROR;
		
		// Literales base
		if (exp instanceof LiteralEntero) return Tipo.INT;
		if (exp instanceof LiteralBool) return Tipo.BOOL;
		if (exp instanceof LiteralCaracter) return Tipo.CHAR;
		
		// Identificadores y variable reservada 'me'
		if (exp instanceof Identificador) {
			Identificador id = (Identificador) exp;
			
			if (id.getName().equals("me")) {
				if (!dentroDeComportamiento) {
					reportarError("Utilización de la palabra reservada 'me' fuera de un comportamiento (línea " + id.linea + ", col " + id.columna + ").");
					return Tipo.ERROR;
				}
				return tipoBotActual;
			}
			
			Simbolo s = tablaActual.buscar(id.getName());
			if (s == null) {
				reportarError("Variable '" + id.getName() + "' no declarada (línea " + id.linea + ", col " + id.columna + ").");
				return Tipo.ERROR;
			}
			return s.getTipo();
		}
		
		// Expresiones aritméticas: +, -, *, /, % (INT)
		if (exp instanceof ExpBinArit) {
			ExpBinArit b = (ExpBinArit) exp;
			Tipo t1 = evaluarExpresion(b.getIzq());
			Tipo t2 = evaluarExpresion(b.getDer());
			
			if (t1 == Tipo.ERROR || t2 == Tipo.ERROR) return Tipo.ERROR;
			
			// Si alguna es UNKNOWN se asume válida estáticamente
			if ((t1 != Tipo.INT && t1 != Tipo.UNKNOWN) || (t2 != Tipo.INT && t2 != Tipo.UNKNOWN)) {
				reportarError("Operación aritmética requiere operandos de tipo int (línea " + b.linea + ", col " + b.columna + ").");
				return Tipo.ERROR;
			}
			return Tipo.INT;
		}
		
		// Expresiones booleanas: /\, \/ (BOOL)
		if (exp instanceof ExpBinBool) {
			ExpBinBool b = (ExpBinBool) exp;
			Tipo t1 = evaluarExpresion(b.getIzq());
			Tipo t2 = evaluarExpresion(b.getDer());
			
			if (t1 == Tipo.ERROR || t2 == Tipo.ERROR) return Tipo.ERROR;
			
			// Si alguna es UNKNOWN se asume válida estáticamente
			if ((t1 != Tipo.BOOL && t1 != Tipo.UNKNOWN) || (t2 != Tipo.BOOL && t2 != Tipo.UNKNOWN)) {
				reportarError("Operación lógica requiere operandos de tipo bool (línea " + b.linea + ", col " + b.columna + ").");
				return Tipo.ERROR;
			}
			return Tipo.BOOL;
		}
		
		// Expresiones relacionales: =, /=, <, >=, >, >=
		if (exp instanceof ExpBinRelacional) {
			ExpBinRelacional r = (ExpBinRelacional) exp;
			Tipo t1 = evaluarExpresion(r.getIzq());
			Tipo t2 = evaluarExpresion(r.getDer());
			
			if (t1 == Tipo.ERROR || t2 == Tipo.ERROR) return Tipo.ERROR;
			
			// Si alguna es UNKNOWN, se asume válido
			if (t1 != Tipo.UNKNOWN && t2 != Tipo.UNKNOWN) {
				String op = r.getOp();
				
				// Igualdad y desigualdad aceptan tipos iguales entre sí
				if (op.contains("Igual") || op.contains("Desigual")) {
					if (t1 != t2) {
						reportarError("Comparación de igualdad entre tipos incompatibles (" + t1 + " y " + t2 + ") (línea " + r.linea + ", col " + r.columna + ").");
						return Tipo.ERROR;
					}
				// Operaciones de orden solo aplican a enteros
				} else if (t1 != Tipo.INT || t2 != Tipo.INT) {
					reportarError("Operación relacional de orden requiere operandos de tipo int (línea " + r.linea + ", col " + r.columna + ").");
					return Tipo.ERROR;
				}
			}
			
			return Tipo.BOOL;
		}
		
		// Expresiones unarias: - y ~
		if (exp instanceof ExpUnaria) {
			ExpUnaria u = (ExpUnaria) exp;
			Tipo t = evaluarExpresion(u.getE());
			
			if (t == Tipo.ERROR) return Tipo.ERROR;
			
			// Caso menos unario
			if (u.getSigno().equals("-")) {
				if (t != Tipo.INT && t != Tipo.UNKNOWN) {
					reportarError("Menos unario requiere un operando de tipo int (línea " + u.linea + ", col " + u.columna + ").");
					return Tipo.ERROR;
				}
				return Tipo.INT;
				
			// Caso negación lógica
			} else {
				if (t != Tipo.BOOL && t != Tipo.UNKNOWN) {
					reportarError("Negación lógica requiere un operando de tipo bool (línea " + u.linea + ", col " + u.columna + ").");
					return Tipo.ERROR;
				}
				return Tipo.BOOL;
			}
		}
		
		return Tipo.ERROR;
	}
}