package test;

import model.Estacion;
import model.Sendero;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SenderoTest {
    @Test
    void testSenderoComparacion() {
        // Crear estaciones
        Estacion estacionA = new Estacion("A", 0, 0);
        Estacion estacionB = new Estacion("B", 1, 1);

        // Crear senderos
        Sendero sendero1 = new Sendero(estacionA, estacionB, 5);
        Sendero sendero2 = new Sendero(estacionB, estacionA, 3);

        // Comparar senderos
        assertTrue(sendero1.compareTo(sendero2) > 0, "El sendero1 debería ser mayor que el sendero2");
        assertTrue(sendero2.compareTo(sendero1) < 0, "El sendero2 debería ser menor que el sendero1");
    }
}
