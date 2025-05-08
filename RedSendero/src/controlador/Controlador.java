package controlador;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import model.*;
import vista.*;

public class Controlador{

	private Grafo grafo;
    private MainWindow vista;

    // Constructor
    public Controlador(MainWindow vista) {
        this.grafo = new Grafo();
        this.vista = vista;
    }

    // Método para agregar estación
	public void agregarEstacion(String nombre, double x, double y) {
		// Agregar estación al grafo
		Estacion nuevaEstacion = new Estacion(nombre, x, y);
		grafo.agregarEstacion(nuevaEstacion);
		vista.dibujarEstacion(nombre,x,y);
	}	

	// Método para agregar sendero
	public void agregarSendero(Estacion inicio, Estacion fin, int impacto) {
	    if (inicio != null && fin != null) {
	        grafo.agregarSendero(inicio, fin, impacto);

	        List<Coordinate> coords = new ArrayList<>();
	        coords.add(new Coordinate(inicio.getX(), inicio.getY()));
	        coords.add(new Coordinate(fin.getX(), fin.getY()));
	        coords.add(new Coordinate(inicio.getX(), inicio.getY()));

	        Color color = obtenerColorImpacto(impacto);
	        MapPolygonImpl sendero = new MapPolygonImpl(coords);
	        sendero.setColor(color);

	        vista.dibujarSendero(sendero); // La vista se encarga de agregarlo al mapa
	    } else {
	        vista.mostrarError("Estación origen o destino no encontrada");
	    }
	}

	private Color obtenerColorImpacto(int impacto) {
	    if (impacto >= 8) return Color.RED;
	    if (impacto >= 5) return Color.ORANGE;
	    return Color.GREEN;
	}

	public int cantidadEstaciones() {
		return grafo.getEstaciones().size();
	}

	public List<Estacion> getEstaciones() {
		return grafo.getEstaciones();
	}
	
	public List<Sendero> getSenderos() {
		return grafo.getSenderos();
	}
}