package test;

import model.AGM;
import model.Estacion;
import model.Grafo;
import model.Sendero;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PrimTest {

    @Test
    void testPrimAGMGeneradoCorrectamente() {
    	Grafo grafo = new Grafo();
        // Estaciones
        Estacion a = new Estacion("A", 0, 0);
        Estacion b = new Estacion("B", 1, 0);
        Estacion c = new Estacion("C", 0, 1);
        Estacion d = new Estacion("D", 1, 1);
        grafo.agregarEstacion(a);
        grafo.agregarEstacion(b);
        grafo.agregarEstacion(c);
        grafo.agregarEstacion(d);

        // Senderos (grafo no dirigido: agregamos ambos sentidos)
        grafo.agregarSendero(a, b, 2);
        grafo.agregarSendero(a, c, 3);        
        grafo.agregarSendero(b, c, 1);   
        grafo.agregarSendero(b, d, 4);
        grafo.agregarSendero(c, d, 5);

        List<Sendero> agm = AGM.prim(grafo, a);

        // Comprobaciones

        // AGM debe tener n - 1 senderos si hay n estaciones
        assertEquals(grafo.getEstaciones().size() - 1, agm.size(), "El AGM debe tener n-1 senderos");

        // Comprobamos que conecta todas las estaciones
        Set<Estacion> conectadas = new HashSet<>();
        conectadas.add(agm.get(0).getInicio());
        for (Sendero s : agm) {
            conectadas.add(s.getInicio());
            conectadas.add(s.getFin());
        }

        assertEquals(grafo.getEstaciones().size(), conectadas.size(), "El AGM debe conectar todas las estaciones");

        // El costo total debería ser el mínimo (1 + 2 + 4 = 7)
        int costoTotal = agm.stream().mapToInt(Sendero::getImpacto).sum();
        assertEquals(7, costoTotal, "El costo total del AGM no es el esperado");
    }
}
