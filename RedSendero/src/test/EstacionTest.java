package test;
import model.Estacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EstacionTest {
    @Test
    void testEstacionCreacion() {
        // Crear una estación
        Estacion estacion = new Estacion("A", 0, 0);

        // Comprobar que se creó correctamente
        assertEquals("A", estacion.getNombre(), "El nombre de la estación debería ser 'A'");
        assertEquals(0, estacion.getX(), "La coordenada X debería ser 0");
        assertEquals(0, estacion.getY(), "La coordenada Y debería ser 0");
    }

    @Test
    void testEstacionIgualdad() {
        // Crear estaciones
        Estacion estacion1 = new Estacion("A", 0, 0);
        Estacion estacion2 = new Estacion("A", 1, 1);
        Estacion estacion3 = new Estacion("B", 1, 1);

        // Comprobar igualdad
        assertTrue(estacion1.equals(estacion2), "Las estaciones deberían ser iguales");
        assertFalse(estacion1.equals(estacion3), "Las estaciones no deberían ser iguales");
    }
}
