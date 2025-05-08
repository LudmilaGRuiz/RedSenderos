package model;

import java.util.ArrayList;
import java.util.List;

public class Grafo {
	private List<Estacion> estaciones;
	private List<Sendero> senderos;

	public Grafo() {
		estaciones = new ArrayList<>();
		senderos = new ArrayList<>();
	}

	// Agrega estación
	public void agregarEstacion(Estacion e) {
		estaciones.add(e);
	}

	// Agrega sendero (usa objetos Estacion directamente)
	public void agregarSendero(Estacion inicio, Estacion fin, int impacto) {
		senderos.add(new Sendero(inicio, fin, impacto));
	}

	// Getters
	public List<Estacion> getEstaciones() {
		return estaciones;
	}

	public List<Sendero> getSenderos() {
		return senderos;
	}
}