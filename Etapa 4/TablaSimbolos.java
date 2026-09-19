import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
	private Map<String, Simbolo> tabla;
	private TablaSimbolos padre;
	
	// Constructor de la clase, representa un ambiente/scope (Si es el global, padre es null)
	public TablaSimbolos(TablaSimbolos padre) {
		this.tabla = new HashMap<>();
		this.padre = padre;
	}
	
	public TablaSimbolos getPadre() { return padre; }
	
	// Inserta un nuevo símbolo en el alcance actual. Si ya existe retorna false
	public boolean insertar(Simbolo simbolo) {
		if (tabla.containsKey(simbolo.getNombre())) { return false; }
		tabla.put(simbolo.getNombre(), simbolo);
		return true;
	}
	
	// Método de búsqueda del símbolo. Busca de manera local, en caso de no encontrar, busca en el padre
	public Simbolo buscar(String nombre) {
		// Búsqueda alcance local
		if (tabla.containsKey(nombre)) { return tabla.get(nombre); }
		
		// Búsqueda alcance padre
		if (padre != null) { return padre.buscar(nombre); }
		
		// En caso de no existir la variable, retorna null
		return null;
	}
}