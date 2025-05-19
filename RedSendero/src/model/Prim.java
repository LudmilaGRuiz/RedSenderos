package model;

import java.util.*;

public class Prim {

	public static List<Sendero> prim(List<Estacion> estaciones, List<Sendero> senderos, Estacion inicio) {
    	Map<Estacion, List<Sendero>> grafo = construirGrafoAdyacencia(estaciones, senderos);
        Set<Estacion> visitados = new HashSet<>();
        PriorityQueue<Sendero> cola = new PriorityQueue<>();
        List<Sendero> agm = new ArrayList<>();

        visitados.add(inicio);
        cola.addAll(grafo.get(inicio));

        while (!cola.isEmpty() && visitados.size() < estaciones.size()) {
            Sendero actual = cola.poll();
            Estacion destino = actual.getFin();
            if (visitados.contains(destino)) continue;

            visitados.add(destino);
            agm.add(actual);

            for (Sendero siguiente : grafo.get(destino)) {
                if (!visitados.contains(siguiente.getFin())) {
                    cola.add(siguiente);
                }
            }
        }

        return agm;
    }

    private static Map<Estacion, List<Sendero>> construirGrafoAdyacencia (List<Estacion> estaciones, List<Sendero> senderos) {
        Map<Estacion, List<Sendero>> grafo = new HashMap<>();
        for (Estacion estacion : estaciones) {
            grafo.put(estacion, new ArrayList<>());
        }
        for (Sendero Sendero : senderos) {
            grafo.get(Sendero.getInicio()).add(Sendero);
        }
        return grafo;
    }
}

