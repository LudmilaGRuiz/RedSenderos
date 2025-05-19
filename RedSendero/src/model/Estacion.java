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
}