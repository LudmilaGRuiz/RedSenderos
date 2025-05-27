package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import controlador.Controlador;

public class MainWindow {
	private JFrame mainWindow;
	private Controlador controlador;
	protected PanelMapa panelMapa;
	private PanelBotones panelBotones;

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

		panelMapa = new PanelMapa(this);
		getMainWindow().getContentPane().add(panelMapa);
		panelBotones = new PanelBotones(this);
		getMainWindow().getContentPane().add(panelBotones);

		detectarEstacionPorClick();
	}

	private void detectarEstacionPorClick() {
		panelMapa.mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Coordinate marker = (Coordinate) panelMapa.mapa.getPosition(e.getPoint());
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

	public PanelMapa getPanelMapa() {
		return panelMapa;
	}

	public JFrame getMainWindow() {
		return mainWindow;
	}
	
	public Controlador getControlador() {
		return controlador;
	}
}