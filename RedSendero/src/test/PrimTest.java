package test;

import model.Estacion;
import model.Prim;
import model.Sendero;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PrimTest {

    @Test
    void testPrimAGMGeneradoCorrectamente() {
        // Estaciones
        Estacion a = new Estacion("A", 0, 0);
        Estacion b = new Estacion("B", 1, 0);
        Estacion c = new Estacion("C", 0, 1);
        Estacion d = new Estacion("D", 1, 1);

        List<Estacion> estaciones = List.of(a, b, c, d);

        // Senderos (grafo no dirigido: agregamos ambos sentidos)
        List<Sendero> senderos = new ArrayList<>();
        senderos.add(new Sendero(a, b, 2));
        senderos.add(new Sendero(b, a, 2));
        senderos.add(new Sendero(a, c, 3));
        senderos.add(new Sendero(c, a, 3));
        senderos.add(new Sendero(b, c, 1));
        senderos.add(new Sendero(c, b, 1));
        senderos.add(new Sendero(b, d, 4));
        senderos.add(new Sendero(d, b, 4));
        senderos.add(new Sendero(c, d, 5));
        senderos.add(new Sendero(d, c, 5));

        List<Sendero> agm = Prim.prim(estaciones, senderos, a);

        // Comprobaciones

        // AGM debe tener n - 1 senderos si hay n estaciones
        assertEquals(estaciones.size() - 1, agm.size(), "El AGM debe tener n-1 senderos");

        // Comprobamos que conecta todas las estaciones
        Set<Estacion> conectadas = new HashSet<>();
        conectadas.add(agm.get(0).getInicio());
        for (Sendero s : agm) {
            conectadas.add(s.getInicio());
            conectadas.add(s.getFin());
        }

        assertEquals(estaciones.size(), conectadas.size(), "El AGM debe conectar todas las estaciones");

        // El costo total debería ser el mínimo (1 + 2 + 4 = 7)
        int costoTotal = agm.stream().mapToInt(Sendero::getImpacto).sum();
        assertEquals(7, costoTotal, "El costo total del AGM no es el esperado");
    }
}
