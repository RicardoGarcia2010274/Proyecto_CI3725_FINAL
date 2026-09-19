import java.util.List;

// Guarda información relevante sobre el robot
public class RobotRuntime {
	public String nombre;
	public Tipo tipo;
	public boolean activo;
	public Object valorAlmacenado;
	public List<Comportamiento> comportamientos;
	
	// Coordenadas en la matriz
	public int x = 0;
	public int y = 0;
	
	// Constructor
	public RobotRuntime(String nombre, Tipo tipo, List<Comportamiento> comportamientos) {
		this.nombre = nombre;
		this.tipo = tipo;
		this.activo = false;
		this.valorAlmacenado = null;
		this.comportamientos = comportamientos;
	}
}