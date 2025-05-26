package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import controlador.Controlador;

public class MainWindow {
	private JFrame mainWindow;
	private Controlador controlador;
	private JPanel panelMapa;
	private PanelBotones panelBotones;
	private JMapViewer mapa;
	private static int cantEstaciones = 0;

	/**
	 * @wbp.parser.constructor
	 */
	public MainWindow(Controlador controlador) {
		this.controlador = controlador;
		initialize();
	}

	private void initialize() {
		mainWindow = new JFrame();
		getMainWindow().setTitle("Red de Senderos Parque Nacional Nahuel Huapi, Bariloche");
		getMainWindow().setLayout(null);
		getMainWindow().setBounds(350, 100, 1200, 800);
		getMainWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getMainWindow().setVisible(true);
		getMainWindow().setResizable(false);
		getMainWindow().setLocationRelativeTo(null);

		panelMapa = new JPanel();
		panelMapa.setLayout(new BorderLayout());
		panelMapa.setBounds(0, 0, 1000, 800);

		mapa = new JMapViewer();
		// Elimina desplazamiento y zoom
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
		panelMapa.add(mapa, BorderLayout.CENTER);
		getMainWindow().getContentPane().add(panelMapa);
		panelBotones = new PanelBotones(this);
		getMainWindow().getContentPane().add(panelBotones);

		detectarEstacionPorClick();
	}

	private void detectarEstacionPorClick() {
		mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Coordinate marker = (Coordinate) mapa.getPosition(e.getPoint());
					String nombre = JOptionPane.showInputDialog("Nombre de la estación:");
					if (nombre == null || nombre.trim().isEmpty()) {
						mostrarError("El nombre no puede estar vacío");
						return;
					}
					controlador.agregarEstacion(nombre, marker.getLat(), marker.getLon());
				}
			}
		});
	}

	protected void agregarEstacion() {
		String nombre = JOptionPane.showInputDialog("Nombre de la estación:");
		if (nombre == null || nombre.trim().isEmpty()) {
			mostrarError("El nombre no puede estar vacío");
			return;
		}

		String inputX = JOptionPane.showInputDialog("Coordenada X:");
		if (inputX == null || inputX.trim().isEmpty()) {
			mostrarError("La coordenada X no puede estar vacía");
			return;
		}

		String inputY = JOptionPane.showInputDialog("Coordenada Y:");
		if (inputY == null || inputY.trim().isEmpty()) {
			mostrarError("La coordenada Y no puede estar vacía");
			return;
		}

		try {
			int x = Integer.parseInt(inputX);
			int y = Integer.parseInt(inputY);
			controlador.agregarEstacion(nombre, x, y);

		} catch (NumberFormatException e) {
			mostrarError("Las coordenadas deben ser números enteros");
		}
	}

	public void dibujarEstacion(String nombreEstacion, double x, double y) {
		// Actualizar la vista
		MapMarkerDot marker = new MapMarkerDot(nombreEstacion, new Coordinate(x, y));
		mapa.addMapMarker(marker);
		cantEstaciones++;
	}

	protected void agregarSendero() {
		if (cantEstaciones < 2) {
			mostrarError("Necesitas al menos 2 estaciones para crear un sendero");
			return;
		}
        String estacionInicio = seleccionarEstacion("Seleccione estación origen:");
        if (estacionInicio == null) return;
        
        String estacionFin = seleccionarEstacion("Seleccione estación destino:");
        if (estacionFin == null) return;
        
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
            controlador.agregarSendero(estacionInicio,estacionFin,impacto);   
        } catch (NumberFormatException e) {
            mostrarError("El impacto debe ser un número válido");
        }	
	}
	public String seleccionarEstacion(String mensaje) {
        String[] estaciones = controlador.getNombresEstaciones();
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
			mostrarError("Necesitas al menos 2 estaciones para crear un sendero");
			return;
		}
		limpiarSenderos();
		controlador.caminoMinimo();
	}

	protected void guardarGrafo() {
		try {
			String nombreArchivo = JOptionPane.showInputDialog(null, "Nombre del archivo");
			if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
				controlador.guardarGrafo(nombreArchivo.trim() + ".json");
			}
		} catch (Exception ex) {
			mostrarError("Error al guardar el grafo: " + ex.getMessage());
		}
	}

	protected void cargarGrafo() {
		JFileChooser fileChooser = new JFileChooser(controlador.getFilePathArchivosGuardados());
		fileChooser.setDialogTitle("Seleccionar archivo JSON");

		int seleccion = fileChooser.showOpenDialog(null);

		if (seleccion == JFileChooser.APPROVE_OPTION) {
			File archivoSeleccionado = fileChooser.getSelectedFile();
			String rutaArchivo = archivoSeleccionado.getAbsolutePath();
			try {
				controlador.cargarGrafo(rutaArchivo);
			} catch (Exception ex) {
				mostrarError("Error al cargar el grafo: " + ex.getMessage());
			}
		}
	}

	public void limpiarSenderos() {
		mapa.removeAllMapPolygons();
	}

	public void limpiarMapa() {
		limpiarSenderos();
		mapa.removeAllMapMarkers();
		cantEstaciones = 0;
	}

	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(
				null,
				mensaje,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(
				null,
				mensaje,
				"Información",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// Getters

	public JPanel getPanelMapa() {
		return panelMapa;
	}

	public JFrame getMainWindow() {
		return mainWindow;
	}
}