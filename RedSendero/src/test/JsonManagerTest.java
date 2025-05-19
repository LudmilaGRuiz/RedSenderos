package test;
import org.junit.jupiter.api.Test;

import model.*;
import utilidades.JsonManager;

import static org.junit.jupiter.api.Assertions.*;

public class JsonManagerTest {
    @Test
    void testGuardarYLeerGrafo() {
        // Crear un grafo de ejemplo
        Grafo grafo = new Grafo();
        Estacion estacion1 = new Estacion("Estacion1", 0, 0);
        Estacion estacion2 = new Estacion("Estacion2", 1, 1);
        grafo.agregarEstacion(estacion1);
        grafo.agregarEstacion(estacion2);
        grafo.agregarSendero(estacion1, estacion2, 5);

        // Guardar el grafo en un archivo
        String archivo = "ArchivosGuardados/grafoJUnit.json";
        try {
            JsonManager.guardarGrafo(grafo, archivo);
        } catch (Exception e) {
            fail("Error al guardar el grafo: " + e.getMessage());
        }

        // Leer el grafo desde el archivo
        Grafo grafoLeido = null;
        try {
            grafoLeido = JsonManager.cargarGrafo(archivo);
        } catch (Exception e) {
            fail("Error al leer el grafo: " + e.getMessage());
        }

        // Comprobar que el grafo leído es igual al original
        assertNotNull(grafoLeido, "El grafo leído no debe ser nulo");
        assertEquals(grafo.getEstaciones().size(), grafoLeido.getEstaciones().size(), "El número de estaciones debe ser igual");
        assertEquals(grafo.getSenderos().size(), grafoLeido.getSenderos().size(), "El número de senderos debe ser igual");
    }
}
