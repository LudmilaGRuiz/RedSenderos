package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import controlador.Controlador;


public class MainWindow{
	private JFrame mainWindow;
	private Controlador controlador;
	private JPanel panelMapa, panelBotones;
	private JMapViewer mapa;
    private JButton btnAgregarEstacion, btnAgregarSendero, btnGenerarAGM, btnGuardar, btnCargar;
    private static int cantEstaciones=0;
    
	/**
	 * @wbp.parser.constructor
	 */
	public MainWindow() {
		controlador = new Controlador(this);
		initialize();
	}
    
	/**
	 * Initialize the contents of the frame.
	 */
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
		for (var l : mapa.getMouseListeners()) mapa.removeMouseListener(l);
		for (var l : mapa.getMouseMotionListeners()) mapa.removeMouseMotionListener(l);
		for (var l : mapa.getMouseWheelListeners()) mapa.removeMouseWheelListener(l);	
		
		mapa.setDisplayPosition(new Coordinate(-41.115572711852, -71.40666961669922), 12);	
		mapa.setZoomControlsVisible(false);
		mapa.setScrollWrapEnabled(false);
		mapa.setRequestFocusEnabled(false);
		panelMapa.add(mapa, BorderLayout.CENTER);	
		getMainWindow().getContentPane().add(panelMapa);
		
		panelBotones = new JPanel();
		panelBotones.setBackground(SystemColor.activeCaption);
		panelBotones.setBounds(1000, 0, 200, 400);
		//panelBotones.setLayout(new GridLayout(4, 1));
		panelBotones.setBorder(BorderFactory.createTitledBorder("Controles"));
		panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
		
		btnAgregarEstacion = new JButton("Agregar Estación");
		btnAgregarSendero = new JButton("Conectar Estaciones");
		btnGenerarAGM = new JButton("Camino minimo");
		btnGuardar = new JButton("Guardar");
		btnCargar = new JButton("Cargar");
		panelBotones.add(btnAgregarEstacion);
		panelBotones.add(btnAgregarSendero);
		panelBotones.add(btnGenerarAGM);
		panelBotones.add(btnGuardar);
		panelBotones.add(btnCargar);

		getMainWindow().getContentPane().add(panelBotones);
		
		detectarEstacionPorClick();
		detectarBtnAgregarEstacion();
		detectarBtnAgregarSendero();
		detectarBtnGenerarAGM();
		detectarBtnGuardar();
		detectarBtnCargar();
	}

	//Agregar una estacion mediante click sobre el mapa
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
	

	//Agregar una estacion por sus coordenadas
	private void detectarBtnAgregarEstacion() {
		btnAgregarEstacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarEstacion();}
		});
	}
	
	private void agregarEstacion() {
		// Pedir nombre
		String nombre = JOptionPane.showInputDialog("Nombre de la estación:");
		if (nombre == null || nombre.trim().isEmpty()) {
			mostrarError("El nombre no puede estar vacío");
			return;
		}
		// Pedir coordenada X
		String inputX = JOptionPane.showInputDialog("Coordenada X:");
		if (inputX == null || inputX.trim().isEmpty()) {
			mostrarError("La coordenada X no puede estar vacía");
			return;
		}

		// Pedir coordenada Y
		String inputY = JOptionPane.showInputDialog("Coordenada Y:");
		if (inputY == null || inputY.trim().isEmpty()) {
			mostrarError("La coordenada Y no puede estar vacía");
			return;
		}

		// Convertir coordenadas a números
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
		MapMarkerDot marker = new MapMarkerDot(nombreEstacion, new Coordinate(x,y));
		mapa.addMapMarker(marker);
		cantEstaciones++;
	}
	
	//Agregar un nuevo sendero
	private void detectarBtnAgregarSendero() {
		btnAgregarSendero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        if (cantEstaciones < 2) {
		            mostrarError("Necesitas al menos 2 estaciones para crear un sendero");
		            return;
		        }
				controlador.agregarSendero();}
		});
		
	}

    public void dibujarSendero(MapPolygonImpl sendero) {
        mapa.addMapPolygon(sendero);
    }
	
    //Generar arbol generador minimo
    public void detectarBtnGenerarAGM() {
		btnGenerarAGM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arbolGeneradorMinimo();}
		});
    }
    
	private void arbolGeneradorMinimo() {
        if (cantEstaciones < 2) {
            mostrarError("Necesitas al menos 2 estaciones para crear un sendero");
            return;
        }
        limpiarSenderos();
        controlador.caminoMinimo();
	}
	
	private void detectarBtnGuardar() {
		btnGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String nombreArchivo = JOptionPane.showInputDialog(null, "Nombre del archivo");
					if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
						controlador.guardarGrafo(nombreArchivo.trim() + ".json");
					}
				} catch (Exception ex) {
					mostrarError("Error al guardar el grafo: " + ex.getMessage());
				}
			}
		});
	}

	private void detectarBtnCargar() {
		btnCargar.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("RedSendero/ArchivosGuardados");
			fileChooser.setDialogTitle("Seleccionar archivo JSON");


			int seleccion = fileChooser.showOpenDialog(null);

			if (seleccion == JFileChooser.APPROVE_OPTION) {
				File archivoSeleccionado = fileChooser.getSelectedFile();
				String rutaArchivo = archivoSeleccionado.getAbsolutePath();

				// Acá llamás a tu controlador o método para cargar el grafo
				try {
					controlador.cargarGrafo(rutaArchivo);
				} catch (Exception ex) {
					mostrarError("Error al cargar el grafo: " + ex.getMessage());
				}
			}
		}
	});
		return;
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
            JOptionPane.ERROR_MESSAGE
        );
    }

    public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(
			null,
			mensaje,
			"Información",
			JOptionPane.INFORMATION_MESSAGE
		);
	}
	// Getters
	public JButton getBtnAgregarEstacion() {
		return btnAgregarEstacion;
	}

	public JButton getBtnAgregarSendero() {
		return btnAgregarSendero;
	}

	public JPanel getPanelMapa() {
		return panelMapa;
	}

	public JFrame getMainWindow() {
		return mainWindow;
	}
}