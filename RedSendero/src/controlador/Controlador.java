package controlador;

import javax.swing.*;
import model.*;
import vista.*;

public class Controlador {
    private Grafo grafo;
    private MainWindow vista;

    // Constructor
    public Controlador(Grafo grafo, MainWindow vista) {
        this.grafo = grafo;
        this.vista = vista;
    }

    // Método para agregar estación
    public void agregarEstacion() {
        //	Pedir nombre
        String nombre = JOptionPane.showInputDialog("Nombre de la estación:");
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("El nombre no puede estar vacío");
            return;
        }

        // 	Pedir coordenada X
        String inputX = JOptionPane.showInputDialog("Coordenada X:");
        if (inputX == null || inputX.trim().isEmpty()) {
            mostrarError("La coordenada X no puede estar vacía");
            return;
        }

        //	Pedir coordenada Y
        String inputY = JOptionPane.showInputDialog("Coordenada Y:");
        if (inputY == null || inputY.trim().isEmpty()) {
            mostrarError("La coordenada Y no puede estar vacía");
            return;
        }

        //	Convertir coordenadas a números
        try {
            int x = Integer.parseInt(inputX);
            int y = Integer.parseInt(inputY);

            //	Agregar estación al grafo
            Estacion nuevaEstacion = new Estacion(nombre, x, y);
            grafo.agregarEstacion(nuevaEstacion);

            //	Actualizar la vista
            vista.getPanelMapa().setGrafo(grafo);

        } catch (NumberFormatException e) {
            mostrarError("Las coordenadas deben ser números enteros");
        }
    }

    // Método para agregar sendero
    public void agregarSendero() {
        //	Verificar que haya al menos 2 estaciones
        if (grafo.getEstaciones().size() < 2) {
            mostrarError("Necesitas al menos 2 estaciones para crear un sendero");
            return;
        }

        //	Seleccionar estación de origen
        Estacion inicio = seleccionarEstacion("Seleccione estación origen:");
        if (inicio == null) return; // Si el usuario cancela

        //	Seleccionar estación destino
        Estacion fin = seleccionarEstacion("Seleccione estación destino:");
        if (fin == null) return;

        //	Pedir impacto ambiental
        String inputImpacto = JOptionPane.showInputDialog("Impacto ambiental (1-10):");
        if (inputImpacto == null || inputImpacto.trim().isEmpty()) {
            mostrarError("El impacto no puede estar vacío");
            return;
        }

        try {
            int impacto = Integer.parseInt(inputImpacto);
            if (impacto < 1 || impacto > 10) {
                mostrarError("El impacto debe ser entre 1 y 10");
                return;
            }

            // 5. Agregar sendero al grafo
            grafo.agregarSendero(inicio, fin, impacto);

            // 6. Actualizar la vista
            vista.getPanelMapa().setGrafo(grafo);

        } catch (NumberFormatException e) {
            mostrarError("El impacto debe ser un número válido");
        }
    }

    // Método auxiliar para seleccionar una estación (con lista desplegable)
    private Estacion seleccionarEstacion(String mensaje) {
        // Crear arreglo con los nombres de las estaciones
        String[] nombres = new String[grafo.getEstaciones().size()];
        for (int i = 0; i < grafo.getEstaciones().size(); i++) {
            nombres[i] = grafo.getEstaciones().get(i).getNombre();
        }

        // Mostrar diálogo de selección
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            mensaje,
            "Selección",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nombres,
            nombres[0]
        );

        // Buscar la estación seleccionada 
        if (seleccion != null) {
            for (Estacion estacion : grafo.getEstaciones()) {
                if (estacion.getNombre().equals(seleccion)) {
                    return estacion;
                }
            }
        }
        return null;
    }

    // Método auxiliar para mostrar errores
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}