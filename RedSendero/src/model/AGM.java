package model;

import java.util.*;

public class AGM {
	public static List<Sendero> prim(Grafo grafo, Estacion inicio) {
		Map<Estacion, List<Sendero>> grafoAdyacencia = grafo.getAdyacencia();
        Set<Estacion> visitados = new HashSet<>();
        PriorityQueue<Sendero> cola = new PriorityQueue<>();
        List<Sendero> agm = new ArrayList<>();
        
        visitados.add(inicio);
        cola.addAll(grafoAdyacencia.get(inicio));

        while (!cola.isEmpty() && visitados.size() < grafo.getEstaciones().size()) {
            Sendero actual = cola.poll();
            
            Estacion destino = actual.getFin();
            if (visitados.contains(destino)) continue;

            visitados.add(destino);
            agm.add(actual);

            for (Sendero siguiente : grafoAdyacencia.get(destino)) 
                if (!visitados.contains(siguiente.getFin())) 
                    cola.add(siguiente);
        }
        return agm;
    }
}

