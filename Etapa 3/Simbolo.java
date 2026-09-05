// Implementación de la clase que guarda las características de un símbolo, y si es o no es un bot
public class Simbolo {
	private String nombre;
	private Tipo tipo;
	private boolean esBot;
	private int linea;
	private int columna;
	
	// Constructor de la clase
	public Simbolo(String nombre, Tipo tipo, boolean esBot, int linea, int columna) {
		this.nombre = nombre;
		this.tipo = tipo;
		this.esBot = esBot;
		this.linea = linea;
		this.columna = columna;
	}
	
	public String getNombre() { return this.nombre; }
	public Tipo getTipo() { return this.tipo; }
	public boolean esBot() { return this.esBot; }
	public int getLinea() { return this.linea; }
	public int getColumna() { return this.columna; }
}