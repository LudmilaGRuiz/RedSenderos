package model;

import java.util.*;

public class Prim {
	private static HashMap<Estacion, List<Sendero>> grafo;

	public static List<Sendero> prim(List<Estacion> estaciones, List<Sendero> senderos, Estacion inicio) {
    	grafo = new HashMap<>();
        for (Estacion estacion : estaciones) {
            grafo.put(estacion, new ArrayList<>());
        }
        for (Sendero Sendero : senderos) {
            grafo.get(Sendero.getInicio()).add(Sendero);
            grafo.get(Sendero.getFin()).add(new Sendero(Sendero.getFin(), Sendero.getInicio(), Sendero.getImpacto())); // no dirigido
        }

        Set<Estacion> visitados = new HashSet<>();
        PriorityQueue<Sendero> cola = new PriorityQueue<>();
        List<Sendero> agm = new ArrayList<>();

        visitados.add(inicio);
        cola.addAll(grafo.get(inicio));

        while (!cola.isEmpty() && visitados.size() < estaciones.size()) {
            Sendero sendero = cola.poll();
            if (visitados.contains(sendero.getFin())) continue;

            visitados.add(sendero.getFin());
            agm.add(sendero);

            for (Sendero siguiente : grafo.get(sendero.getFin())) {
                if (!visitados.contains(siguiente.getFin())) {
                    cola.add(siguiente);
                }
            }
        }

        return agm;
    }
	
    protected HashMap<Estacion, List<Sendero>> getGrafo() {
		return grafo;
	}
}

