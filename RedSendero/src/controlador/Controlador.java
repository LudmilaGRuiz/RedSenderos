package controlador;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import model.*;
import utilidades.JsonManager;
import vista.*;

public class Controlador{

	private Grafo grafo;
    private MainWindow vista;

    public Controlador() {
        this.grafo = new Grafo();
		this.vista = new MainWindow(this);
    }

	public void abrirInterfaz() {
		vista.getMainWindow().setVisible(true);
	}
    
	public void agregarEstacion(String nombre, double x, double y) {
		// Agregar estación al grafo
		try {
			Estacion nuevaEstacion = new Estacion(nombre, x, y);
			grafo.agregarEstacion(nuevaEstacion);
			vista.dibujarEstacion(nombre,x,y);
		} catch (Exception e) {
			vista.mostrarError("Error al agregar la estación: " + e.getMessage());
		}
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

	        MapPolygonImpl sendero = construirCamino(inicio, fin);
	        sendero.setColor(obtenerColorImpacto(impacto));
			sendero.setName(String.valueOf(impacto));
	        vista.dibujarSendero(sendero); // La vista se encarga de agregarlo al mapa
	    } else {
	        vista.mostrarError("Estación origen o destino no encontrada\n o son la misma");
	    }
	}

	private MapPolygonImpl construirCamino(Estacion inicio, Estacion fin) {
		List<Coordinate> coords = new ArrayList<>();
		coords.add(new Coordinate(inicio.getX(), inicio.getY()));
		coords.add(new Coordinate(fin.getX(), fin.getY()));
		coords.add(new Coordinate(inicio.getX(), inicio.getY()));

		MapPolygonImpl camino = new MapPolygonImpl(coords);
		camino.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		camino.setStroke(new BasicStroke(3.0f));
		return camino;
	}


	private Color obtenerColorImpacto(int impacto) {
	    if (impacto >= 8) return Color.RED;
	    if (impacto >= 5) return Color.ORANGE;
	    return Color.GREEN;
	}

	//Metodo para arbol generador minimo
	public void caminoMinimo() {
		int sumaImpacto = 0;
		List<Sendero> agmPrim = Prim.prim(grafo.getEstaciones(), grafo.getSenderos(), grafo.getEstaciones().get(0));
		if(agmPrim==null || agmPrim.isEmpty())return;
		for(Sendero s : agmPrim) {
			sumaImpacto += s.getImpacto();
	        MapPolygonImpl camino = construirCamino(s.getInicio(), s.getFin());
			camino.setName(String.valueOf(s.getImpacto()));
			vista.dibujarSendero(camino);
		}
		vista.mostrarMensaje("El impacto total del árbol generador mínimo es: " + sumaImpacto);
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

	public void guardarGrafo(String archivo) {
		try {
			String ruta = getFilePathArchivosGuardados().getAbsolutePath() +"/"+ archivo;
			System.out.println("Ruta: " + ruta);
			JsonManager.guardarGrafo(grafo, ruta);
		} catch (Exception e) {
			vista.mostrarError("Error al guardar el grafo: " + e.getMessage());
		}
	}

	public void cargarGrafo(String archivo) {
		try {
			grafo = JsonManager.cargarGrafo(archivo);
			vista.limpiarMapa();
			for (Estacion estacion : grafo.getEstaciones()) {
				vista.dibujarEstacion(estacion.getNombre(), estacion.getX(), estacion.getY());
			}
			for (Sendero sendero : grafo.getSenderos()) {
				MapPolygonImpl camino = construirCamino(sendero.getInicio(), sendero.getFin());
				camino.setColor(obtenerColorImpacto(sendero.getImpacto()));
				camino.setName(String.valueOf(sendero.getImpacto()));
				vista.dibujarSendero(camino);
			}
		} catch (Exception e) {
			vista.mostrarError("Error al cargar el grafo: " + e.getMessage());
		}
	}

	public File getFilePathArchivosGuardados() {
		String userDir = System.getProperty("user.dir");
		File archivosGuardadosDir;

		if (userDir.endsWith("RedSendero")) {
			archivosGuardadosDir = new File("ArchivosGuardados");
		} else {
			archivosGuardadosDir = new File("RedSendero/ArchivosGuardados");
		}
		return archivosGuardadosDir;
	}
}