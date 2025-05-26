package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {
	private List<Estacion> estaciones;
	private List<Sendero> senderos;

	public Grafo() {
		estaciones = new ArrayList<>();
		senderos = new ArrayList<>();
	}

	// Agrega estación
	public void agregarEstacion(Estacion e) {
		if (estaciones.contains(e)) {
			throw new IllegalArgumentException("La estación ya existe: " + e.getNombre());
		}
		estaciones.add(e);
	}


	// Agrega sendero (usa objetos Estacion directamente)
	public void agregarSendero(Estacion inicio, Estacion fin, int impacto) {
		if (!estaciones.contains(inicio) || !estaciones.contains(fin)) 
			throw new IllegalArgumentException("Una o ambas estaciones no existen en el grafo");
		if(inicio.equals(fin))
			throw new IllegalArgumentException("No es posible crear un sendero con la misma estacion");

		Sendero s = new Sendero(inicio, fin, impacto);
		senderos.add(s);
		
		Sendero inverso = new Sendero(fin, inicio, impacto);
		senderos.add(inverso);
	}

	// Getters
	public List<Estacion> getEstaciones() {
        return estaciones;
    }

    public List<Sendero> getSenderos() {
        return senderos;
    }

    public Map<Estacion, List<Sendero>> getAdyacencia() {
    	Map<Estacion, List<Sendero>> grafo = new HashMap<>();
        for (Estacion estacion : estaciones) 
            grafo.put(estacion, new ArrayList<>());
        
        for (Sendero sendero : senderos) 
            grafo.get(sendero.getInicio()).add(sendero);
        
        return grafo;
    }
}