package controlador;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import model.*;
import vista.*;

public class Controlador{

	private Grafo grafo;
    private MainWindow vista;

    public Controlador(MainWindow vista) {
        this.grafo = new Grafo();
        this.vista = vista;
    }
    
	public void agregarEstacion(String nombre, double x, double y) {
		// Agregar estación al grafo
		Estacion nuevaEstacion = new Estacion(nombre, x, y);
		grafo.agregarEstacion(nuevaEstacion);
		vista.dibujarEstacion(nombre,x,y);
	}	

	public void agregarSendero() {
        Estacion inicio = seleccionarEstacion("Seleccione estación origen:");
        if (inicio == null) return;
        
        Estacion fin = seleccionarEstacion("Seleccione estación destino:");
        if (fin == null) return;
        
        String inputImpacto = JOptionPane.showInputDialog("Impacto ambiental (1-10):");
        if (inputImpacto == null || inputImpacto.trim().isEmpty()) {
            vista.mostrarError("El impacto no puede estar vacío");
            return;
        }
        try {
            int impacto = Integer.parseInt(inputImpacto);
            if (impacto < 1 || impacto > 10) {
                vista.mostrarError("El impacto debe ser entre 1 y 10");
                return;
            }
            nuevoSendero(inicio, fin, impacto);   
        } catch (NumberFormatException e) {
            vista.mostrarError("El impacto debe ser un número válido");
        }
    }
	
	// Método para agregar sendero
	public void nuevoSendero(Estacion inicio, Estacion fin, int impacto) {
	    if (inicio != null && fin != null && !inicio.equals(fin)) {
	        grafo.agregarSendero(inicio, fin, impacto);
			grafo.agregarSendero(fin, inicio, impacto); 

	        List<Coordinate> coords = new ArrayList<>();
	        coords.add(new Coordinate(inicio.getX(), inicio.getY()));
	        coords.add(new Coordinate(fin.getX(), fin.getY()));
	        coords.add(new Coordinate(inicio.getX(), inicio.getY()));

	        MapPolygonImpl sendero = new MapPolygonImpl(coords);
	        sendero.setColor(obtenerColorImpacto(impacto));

	        vista.dibujarSendero(sendero); // La vista se encarga de agregarlo al mapa
	    } else {
	        vista.mostrarError("Estación origen o destino no encontrada\n o son la misma");
	    }
	}

	private Color obtenerColorImpacto(int impacto) {
	    if (impacto >= 8) return Color.RED;
	    if (impacto >= 5) return Color.ORANGE;
	    return Color.GREEN;
	}

	//Metodo para arbol generador minimo
	public void caminoMinimo() {
		List<Sendero> agmPrim = Prim.prim(grafo.getEstaciones(), grafo.getSenderos(), grafo.getEstaciones().get(0));
		if(agmPrim==null || agmPrim.isEmpty())return;
		for(Sendero s : agmPrim) {
	        List<Coordinate> coords = new ArrayList<>();
	        coords.add(new Coordinate(s.getInicio().getX(), s.getInicio().getY()));
	        coords.add(new Coordinate(s.getFin().getX(), s.getFin().getY()));
	        coords.add(new Coordinate(s.getInicio().getX(), s.getInicio().getY()));
	        MapPolygonImpl camino = new MapPolygonImpl(coords);
	        camino.setColor(obtenerColorImpacto(s.getImpacto()));
			vista.dibujarSendero(camino);
		}
	}

	public Estacion seleccionarEstacion(String mensaje) {
        // Crear arreglo con los nombres de las estaciones
        String[] estaciones = new String[grafo.getEstaciones().size()];
        for (int i = 0; i < estaciones.length; i++)
            estaciones[i] = grafo.getEstaciones().get(i).getNombre();
        
        // Mostrar diálogo de selección
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            mensaje,
            "Selección",
            JOptionPane.PLAIN_MESSAGE,
            null,
            estaciones,
            estaciones[0]
        );

		// Buscar la estación seleccionada
		if (seleccion != null)
			for (Estacion estacion : grafo.getEstaciones())
				if (estacion.getNombre().equals(seleccion))
					return estacion;				
        return null;
    }
}