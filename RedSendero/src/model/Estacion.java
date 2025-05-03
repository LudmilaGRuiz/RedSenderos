package model;

public class Estacion {
    private String nombre;
    private int x, y;  // Coordenadas para dibujar en el panel

    public Estacion(String nombre, int x, int y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    // Getters
    public String getNombre() { 
    	return nombre; 
    	}
    
    public int getX() {
    	return x;
    }
    
    public int getY() { 
    	return y;
    }
}