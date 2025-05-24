package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.Estacion;
import model.Grafo;

public class GrafoTest {
    @Test
    void testGrafoCreacion() {
        // Crear un grafo
        Grafo grafo = new Grafo();

        // Comprobar que se creó correctamente
        assertNotNull(grafo, "El grafo no debería ser nulo");
        assertTrue(grafo.getEstaciones().isEmpty(), "El grafo debería estar vacío al inicio");
        assertTrue(grafo.getSenderos().isEmpty(), "El grafo debería estar vacío al inicio");
    }

    @Test
    void testAgregarEstacion() {
        // Crear un grafo y una estación
        Grafo grafo = new Grafo();
        Estacion estacion = new Estacion("A", 0, 0);

        // Agregar la estación al grafo
        grafo.agregarEstacion(estacion);

        // Comprobar que la estación fue agregada correctamente
        assertEquals(1, grafo.getEstaciones().size(), "El grafo debería tener una estación");
        assertEquals(estacion, grafo.getEstaciones().get(0), "La estación agregada no es la esperada");
    }

    @Test
    void testAgregarSendero() {
        // Crear un grafo y estaciones
        Grafo grafo = new Grafo();
        Estacion estacion1 = new Estacion("A", 0, 0);
        Estacion estacion2 = new Estacion("B", 1, 1);

        // Agregar estaciones al grafo
        grafo.agregarEstacion(estacion1);
        grafo.agregarEstacion(estacion2);

        // Agregar un sendero entre las estaciones
        int impacto = 5;
        grafo.agregarSendero(estacion1, estacion2, impacto);

        // Comprobar que el sendero fue agregado correctamente
        assertEquals(1, grafo.getSenderos().size(), "El grafo debería tener un sendero");
    }

    @Test
    void testAgregarEstacionDuplicada() {
        // Crear un grafo y una estación
        Grafo grafo = new Grafo();
        Estacion estacion = new Estacion("A", 0, 0);

        // Agregar la estación al grafo
        grafo.agregarEstacion(estacion);

        // Intentar agregar la misma estación nuevamente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            grafo.agregarEstacion(estacion);
        });

        // Comprobar que se lanzó la excepción esperada
        assertEquals("La estación ya existe: A", exception.getMessage());
    }

    @Test
    void testAgregarSenderoEntreEstacionesNoExistentes() {
        // Crear un grafo y estaciones
        Grafo grafo = new Grafo();
        Estacion estacion1 = new Estacion("A", 0, 0);
        Estacion estacion2 = new Estacion("B", 1, 1);

        // Intentar agregar un sendero entre estaciones que no existen en el grafo
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            grafo.agregarSendero(estacion1, estacion2, 5);
        });

        // Comprobar que se lanzó la excepción esperada
        assertEquals("Una o ambas estaciones no existen en el grafo", exception.getMessage());
    }

    @Test
    void testAgregarSendero_MismaEstacion_LanzaExcepcion(expected= IllegalArgumentException.class) {
        // Crear un grafo y estaciones
        Grafo grafo = new Grafo();
        Estacion estacion = new Estacion("A", 0, 0);

        // Agregar estaciones al grafo
        grafo.agregarEstacion(estacion);

        // Agregar un sendero entre las estaciones
        int impacto = 5;
         IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> grafo.agregarSendero(estacionA, estacionA, impacto));

        
        
    }
    


}
