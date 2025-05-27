package vista;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelMapa extends JPanel {
	protected JMapViewer mapa;
    private MainWindow ventana;
    private static int cantEstaciones = 0;

	public PanelMapa(MainWindow ventana) {
		setLayout(new BorderLayout());
		setBounds(0, 0, 1000, 800);

        this.ventana = ventana;
		mapa = new JMapViewer();

		for (var l : mapa.getMouseListeners())
			mapa.removeMouseListener(l);
		for (var l : mapa.getMouseMotionListeners())
			mapa.removeMouseMotionListener(l);
		for (var l : mapa.getMouseWheelListeners())
			mapa.removeMouseWheelListener(l);

		mapa.setDisplayPosition(new Coordinate(-41.1655, -71.5600), 12);
		mapa.setZoomControlsVisible(false);
		mapa.setScrollWrapEnabled(false);
		mapa.setRequestFocusEnabled(false);

		add(mapa, BorderLayout.CENTER);
	}

	public void dibujarEstacion(String nombreEstacion, double x, double y) {
		MapMarkerDot marker = new MapMarkerDot(nombreEstacion, new Coordinate(x, y));
		mapa.addMapMarker(marker);
		cantEstaciones++;
	}

    protected void agregarSendero() {
		if (cantEstaciones < 2) {
			ventana.mostrarError("Necesitas al menos 2 estaciones para crear un sendero");
			return;
		}
        String estacionInicio = seleccionarEstacion("Seleccione estación origen:");
        if (estacionInicio == null) return;
        
        String estacionFin = seleccionarEstacion("Seleccione estación destino:");
        if (estacionFin == null) return;
        
        String inputImpacto = JOptionPane.showInputDialog("Impacto ambiental (1-10):");
        if (inputImpacto == null || inputImpacto.trim().isEmpty()) {
            ventana.mostrarError("El impacto no puede estar vacío");
            return;
        }
        try {
            int impacto = Integer.parseInt(inputImpacto);
            if (impacto < 1 || impacto > 10) {
                ventana.mostrarError("El impacto debe ser entre 1 y 10");
                return;
            }
            ventana.getControlador().agregarSendero(estacionInicio,estacionFin,impacto);   
        } catch (NumberFormatException e) {
            ventana.mostrarError("El impacto debe ser un número válido");
        }	
	}

    public String seleccionarEstacion(String mensaje) {
        String[] estaciones = ventana.getControlador().getNombresEstaciones();
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            mensaje,
            "Selección",
            JOptionPane.PLAIN_MESSAGE,
            null,
            estaciones,
            estaciones[0]
        );				
        return seleccion;
    }

	protected void agregarEstacion() {
		String nombre = JOptionPane.showInputDialog("Nombre de la estación:");
		if (nombre == null || nombre.trim().isEmpty()) {
			ventana.mostrarError("El nombre no puede estar vacío");
			return;
		}

		String inputX = JOptionPane.showInputDialog("Coordenada X:");
		if (inputX == null || inputX.trim().isEmpty()) {
			ventana.mostrarError("La coordenada X no puede estar vacía");
			return;
		}

		String inputY = JOptionPane.showInputDialog("Coordenada Y:");
		if (inputY == null || inputY.trim().isEmpty()) {
			ventana.mostrarError("La coordenada Y no puede estar vacía");
			return;
		}

		try {
			int x = Integer.parseInt(inputX);
			int y = Integer.parseInt(inputY);
			ventana.getControlador().agregarEstacion(nombre, x, y);

		} catch (NumberFormatException e) {
			ventana.mostrarError("Las coordenadas deben ser números enteros");
		}
	}
	
	public void dibujarSendero(double inicioX, double inicioY, double finX, double finY, int impacto) {
		MapPolygonImpl sendero = construirSendero(inicioX,inicioY,finX,finY);
		sendero.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
		sendero.setStroke(new BasicStroke(3.0f));
        sendero.setColor(obtenerColorImpacto(impacto));
		sendero.setName(String.valueOf(impacto));
		mapa.addMapPolygon(sendero);
	}

	private MapPolygonImpl construirSendero(double inicioX, double inicioY, double finX, double finY) {
		List<Coordinate> coords = new ArrayList<>();
		coords.add(new Coordinate(inicioX, inicioY));
		coords.add(new Coordinate(finX, finY));
		coords.add(new Coordinate(inicioX, inicioY));
		MapPolygonImpl sendero = new MapPolygonImpl(coords);
		return sendero;
	}
	
	private Color obtenerColorImpacto(int impacto) {
	    if (impacto >= 8) return Color.RED;
	    if (impacto >= 5) return Color.ORANGE;
	    return Color.GREEN;
	}
	
	protected void arbolGeneradorMinimo() {
		if (cantEstaciones < 2) {
			ventana.mostrarError("Necesitas al menos 2 estaciones para crear un sendero");
			return;
		}
		limpiarSenderos();
		ventana.getControlador().caminoMinimo();
	}

	public void limpiarSenderos() {
		mapa.removeAllMapPolygons();
	}

	public void limpiarMapa() {
		limpiarSenderos();
		mapa.removeAllMapMarkers();
		cantEstaciones = 0;
	}
}
