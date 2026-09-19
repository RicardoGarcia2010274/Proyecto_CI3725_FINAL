import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Mantiene un diccionario con los robots durante la ejecución
public class Interprete {
	// Memoria principal
	private Map<String, RobotRuntime> robotsEnEjecucion;
	
	// Diccionario temporal para variables locales (Creadas con 'as')
	private Map<String, Object> variablesLocales;
	
	// Matriz infinita del mundo (Llave "x,y", Valor: Object)
	private Map<String, Object> matrizMundo;
	
	private RobotRuntime robotActual = null;
	
	// Constructor
	public Interprete() {
		this.robotsEnEjecucion = new HashMap<>();
		this.variablesLocales = new HashMap<>();
		this.matrizMundo = new HashMap<>();
	}
	
	// Llamado a la función que ejecuta el interpretador
	public void interpretar(Programa prog) {
		// Inicialización: registra todos los robots declarados
		if (prog.getDeclaraciones() != null) {
			for (Declaracion dec : prog.getDeclaraciones()) {
				Tipo tipo = Tipo.fromString(dec.getTipo());
				for (Identificador id : dec.getIdentificadores()) {
					robotsEnEjecucion.put(id.getName(), new RobotRuntime(id.getName(), tipo, dec.getComportamientos()));
				}
			}
		}
		
		// Ejecución: corre la instrucción principal (execute)
		if ((prog.getInstruccion()) != null) {
			correr(prog.getInstruccion());
		}
	}
	
	private void correr(Instruccion inst) {
		if (inst == null) return;
		
		// Secuenciación
		if (inst instanceof Secuenciacion) {
			Secuenciacion sec = (Secuenciacion) inst;
			correr(sec.getI1());
			correr(sec.getI2());
		}
		
		// Activación de robots
		else if (inst instanceof Activacion) {
			Activacion act = (Activacion) inst;
			for (Identificador id : act.getIdentificadores()) {
				RobotRuntime bot = robotsEnEjecucion.get(id.getName());
				// Verifica activación ilegal
				if (bot.activo) {
					errorDinamico("Activación ilegal: El robot '" + bot.nombre + "' ya se encuentra activo.");
				}
				bot.activo = true;
			}
		}
		
		// Desactivación de robots
		else if (inst instanceof Desactivacion) {
			Desactivacion deact = (Desactivacion) inst;
			for (Identificador id : deact.getIdentificadores()) {
				RobotRuntime bot = robotsEnEjecucion.get(id.getName());
				
				// Verifica desactivación ilegal
				if (!bot.activo) {
					errorDinamico("Desactivación ilegal: el robot '" + bot.nombre + "' ya se encuentra inactivo.");
				}
				bot.activo = false;
			}
		}
		
		// Condicional
		else if (inst instanceof Condicional) {
			Condicional cond = (Condicional) inst;
			boolean guardia = (Boolean) evaluar(cond.getExpresion());
			if (guardia) {
				correr(cond.getI1());
			} else if (cond.getI2() != null) {
				correr(cond.getI2());
			}
		}
		
		// Bucle While
		else if (inst instanceof IteracionIndet) {
			IteracionIndet iter = (IteracionIndet) inst;
			while ((boolean) evaluar(iter.getExpresion())) {
				correr(iter.getInstruccion());
			}
		}
		
		// Incorporación de alcance (Nuevos bloques create ... execute)
		else if (inst instanceof IncorpAlcance) {
			IncorpAlcance inc = (IncorpAlcance) inst;
			
			// Se guardan los robots actuales, ya que podrían ser ocultados por shadowing
			Map<String, RobotRuntime> robotsOcultos = new HashMap<>();
			
			if (inc.getDeclaraciones() != null) {
				for (Declaracion dec : inc.getDeclaraciones()) {
					Tipo tipo = Tipo.fromString(dec.getTipo());
					for (Identificador id : dec.getIdentificadores()) {
						String nombre = id.getName();
						if (robotsEnEjecucion.containsKey(nombre)) {
							robotsOcultos.put(nombre, robotsEnEjecucion.get(nombre));
						}
						// Se registra el nuevo robot para el alcance actual
						robotsEnEjecucion.put(nombre, new RobotRuntime(nombre, tipo, dec.getComportamientos()));
					}
				}
			}
			
			// Se ejecutan las instrucciones del bloque
			correr(inc.getInstruccion());
			
			// Se restaura el estado del entorno
			if (inc.getDeclaraciones() != null) {
				for (Declaracion dec : inc.getDeclaraciones()) {
					for (Identificador id : dec.getIdentificadores()) {
						String nombre = id.getName();
						if (robotsOcultos.containsKey(nombre)) {
							robotsEnEjecucion.put(nombre, robotsOcultos.get(nombre));
						} else {
							robotsEnEjecucion.remove(nombre);
						}
					}
				}
			}
		}
		
		// Avance (Comportamientos del robot)
		else if (inst instanceof Avance) {
			Avance av = (Avance) inst;
			for (Identificador id : av.getIdentificadores()) {
				RobotRuntime bot = robotsEnEjecucion.get(id.getName());
				if (!bot.activo) continue; // Si está inactivo, no hace nada al avanzar
				
				boolean comportamientoEjecutado = false;
				Comportamiento compDefault = null;
				
				// Configuramos este robot como el actual para que funcione la palabra 'me'
				RobotRuntime prevBot = this.robotActual;
				this.robotActual = bot;
				
				for (Comportamiento comp : bot.comportamientos) {
					// Si el comportamiento es default, lo guardamos por si ningún otro aplica
					if (comp.getCondicion() instanceof CondDefault) {
						compDefault = comp;
					}
					// Si el comportamiento tiene una condición booleana explícita
					else if (comp.getCondicion() instanceof CondExpresion) {
						CondExpresion ce = (CondExpresion) comp.getCondicion();
						if ((Boolean) evaluar(ce.getExpresion())) {
							correr(comp.getInstruccion());
							comportamientoEjecutado = true;
							break;
						}
					}
				}
				
				// Verificación comportamiento inexistente
				if (!comportamientoEjecutado) {
					if (compDefault != null) {
						correr(compDefault.getInstruccion());
					} else {
						errorDinamico("Comportamiento inexistente: Ninguna guardia se cumplió para el robot '" + bot.nombre + "' y no hay default.");
					}
				}
				
				// Se restaura el contexto anterior
				this.robotActual = prevBot;
			}
		}
		
		// Almacenamiento (store)
		else if (inst instanceof Almacenamiento) {
			Object val = evaluar(((Almacenamiento) inst).getExpresion());
			
			// Verificación almacenamiento inadecuado
			if (!validarCompatibilidad(val, robotActual.tipo)) {
				errorDinamico("Almacenamiento inadecuado: El valor no coincide con el tipo del robot (" + robotActual.tipo + ").");
			}
			robotActual.valorAlmacenado = val;
		}
		
		// Entrada (read)
		else if (inst instanceof Entrada) {
			Entrada ent = (Entrada) inst;
			Scanner sc = new Scanner(System.in);
			System.out.print("Entrada para robot " + robotActual.nombre + " (" + robotActual.tipo + "): ");
			String input = sc.nextLine();
			
			Object parsedVal = parsearEntrada(input, robotActual.tipo);
			
			// Verificación de lectura
			if (parsedVal == null) {
				errorDinamico("Lectura inadecuada: Se recibió una respuesta inconsistente con el tipo " + robotActual.tipo + ".");
			}
			
			if (ent.getId() != null) {
				variablesLocales.put(ent.getId().getName(), parsedVal); // Si es 'read as X'
			} else {
				robotActual.valorAlmacenado = parsedVal; // Si es solo 'read'
			}
		}
		
		// Soltado (drop)
		else if (inst instanceof Soltado) {
			Object val = evaluar(((Soltado) inst).getExpresion());
			
			// Verificación soltado inadecuado
			if (!validarCompatibilidad(val, robotActual.tipo)) {
				errorDinamico("Soltado inadecuado: Se intentó soltar en la matriz un valor inconsistente con el robot (" + robotActual.tipo + ").");
			}
			
			// Guarda el valor en la coordenada actual del robot
			String coordenada = robotActual.x + "," + robotActual.y;
			matrizMundo.put(coordenada, val);
		}
		
		// Colección (collect)
		else if (inst instanceof Coleccion) {
			Coleccion col = (Coleccion) inst;
			String coordenada = robotActual.x + "," + robotActual.y;
			
			Object valorMatriz = matrizMundo.get(coordenada);
			
			// Verificación colección inadecuada
			if (valorMatriz == null) {
				errorDinamico("Colección inadecuada: No hay ningún valor en la celda actual (" + coordenada + ").");
			}
			
			if (!validarCompatibilidad(valorMatriz, robotActual.tipo)) {
				errorDinamico("Colección inadecuada: El valor en la matriz no coincide con el tipo del robot (" + robotActual.tipo + ").");
			}
			
			if (col.getId() != null) {
				variablesLocales.put(col.getId().getName(), valorMatriz);
			} else {
				robotActual.valorAlmacenado = valorMatriz;
			}
			
			// Limpia la celda tras recoger el objeto
			matrizMundo.remove(coordenada);
		}
		
		// Movimientos (up, down, left, right)
		else if (inst instanceof Movimiento) {
			Movimiento mov = (Movimiento) inst;
			int pasos = 1; // Por defecto es 1 si no hay expresión
			
			if (mov.getExpresion() != null) {
				pasos = (Integer) evaluar(mov.getExpresion());
			}
			
			String dir = mov.getDireccion();
			
			if (dir.equals("up")) robotActual.y += pasos;
			else if (dir.equals("down")) robotActual.y -= pasos;
			else if (dir.equals("right")) robotActual.x += pasos;
			else if (dir.equals("left")) robotActual.x -= pasos;
		}
	}
	
	private Object evaluar(Expresion exp) {
		if (exp == null) return null;
		
		// Literales
		if (exp instanceof LiteralEntero) return ((LiteralEntero) exp).getNum();
		if (exp instanceof LiteralBool) return ((LiteralBool) exp).getValor();
		if (exp instanceof LiteralCaracter) return ((LiteralCaracter) exp).getCaracter();
		
		// Identificadores (Variables locales 'as' o la palabra 'me')
		if (exp instanceof Identificador) {
			String nombre = ((Identificador) exp).getName();
			if (nombre.equals("me")) {
				// El analizador estático ya garantizó que esto solo corre dentro de un comportamiento
				return (robotActual != null) ? robotActual.valorAlmacenado : 0;
			}
			// Retorna el estado del robot si se llama desde el controlador
			if (robotsEnEjecucion.containsKey(nombre)) {
				Object val = robotsEnEjecucion.get(nombre).valorAlmacenado;
				return (val != null) ? val : 0;
			}
			return variablesLocales.get(nombre);
		}
		
		// Expresiones aritméticas
		if (exp instanceof ExpBinArit) {
			ExpBinArit b = (ExpBinArit) exp;
			int izq = (Integer) evaluar(b.getIzq());
			int der = (Integer) evaluar(b.getDer());
			String op = b.getOp();
			
			if (op.contains("Suma")) return izq + der;
			if (op.contains("Resta")) return izq - der;
			if (op.contains("Multiplicacion")) return izq * der;
			
			// Verificación división entre 0
			if (op.contains("Division") || op.contains("Modulo")) {
				if (der == 0) {
					errorDinamico("Intento de división por cero.");
				}
				if (op.contains("Division")) return izq / der;
				if (op.contains("Modulo")) return izq % der;
			}
		}
		
		// Expresiones lógicas
		if (exp instanceof ExpBinBool) {
			ExpBinBool b = (ExpBinBool) exp;
			boolean izq = (Boolean) evaluar(b.getIzq());
			boolean der = (Boolean) evaluar(b.getDer());
			String op = b.getOp();
			
			if (op.contains("Conjuncion")) return izq && der;
			if (op.contains("Disyuncion")) return izq || der;
		}
		
		// Expresiones relacionales
		if (exp instanceof ExpBinRelacional) {
			ExpBinRelacional r = (ExpBinRelacional) exp;
			Object izq = evaluar(r.getIzq());
			Object der = evaluar(r.getDer());
			String op = r.getOp();
			
			if (op.contains("Igual")) return izq.equals(der);
			if (op.contains("Desigual")) return !izq.equals(der);
			
			int i = (Integer) izq;
			int d = (Integer) der;
			
			if (op.contains("Mayor que")) return i > d;
			if (op.contains("Menor que")) return i < d;
			if (op.contains("Mayor igual")) return i >= d;
			if (op.contains("Menor igual")) return i <= d;
		}
		// Expresiones unarias
		if (exp instanceof ExpUnaria) {
			ExpUnaria u = (ExpUnaria) exp;
			Object val = evaluar(u.getE());
			
			if (u.getSigno().equals("-")) return -((Integer) val);
			if (u.getSigno().equals("Negacion")) return !((Boolean) val);
		}
		
		return null;
	}
	
	// Valida si un objeto de Java coincide con un tipo de BOT
	private boolean validarCompatibilidad(Object valor, Tipo tipoBuscado) {
		if (tipoBuscado == Tipo.INT && valor instanceof Integer) return true;
		if (tipoBuscado == Tipo.BOOL && valor instanceof Boolean) return true;
		if (tipoBuscado == Tipo.CHAR && valor instanceof Character) return true;
		return false;
	}
	
	// Intenta castear la entrada al tipo esperado por el robot
	private Object parsearEntrada(String input, Tipo tipo) {
		try {
			input = input.trim();
			if (tipo == Tipo.INT) {
				return Integer.parseInt(input);
			}
			
			if (tipo == Tipo.BOOL) {
				if (input.equalsIgnoreCase("true")) return true;
				if (input.equalsIgnoreCase("false")) return false;
				return null;
			}
			
			if (tipo == Tipo.CHAR) {
				if (input.length() == 1) return input.charAt(0);
				return null;
			}
		} catch (NumberFormatException e) {
			return null; // Falló el casteo a Int
		}
		
		return null;
	}
	
	private void errorDinamico(String mensaje) {
		System.err.println("Error dinámico: " + mensaje);
		System.exit(1);
	}
}