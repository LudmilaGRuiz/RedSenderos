package model;

public class Estacion {
	private String nombre;
	private double x, y; // Coordenadas para dibujar en el panel

	public Estacion(String nombre, double x, double y) {
		this.nombre = nombre;
		this.x = x;
		this.y = y;
	}

	// Getters
	public String getNombre() {
		return nombre;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Estacion otra = (Estacion) obj;
		return nombre.equals(otra.nombre);
	}

	@Override
	public int hashCode() {
		return nombre.hashCode();
	}

}