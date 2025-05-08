package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

//Imports explícitos para model
import controlador.Controlador;
import model.Estacion;
import model.Sendero;

public class MainWindow extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JFrame mainWindow;
	private Controlador controlador;
	private JPanel panelMapa, panelBotones;
	private JMapViewer mapa;
    private JButton btnAgregarEstacion, btnAgregarSendero;
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
		mainWindow.setTitle("Red de Senderos Circuito Chico, Bariloche");
		mainWindow.setLayout(null);
		mainWindow.setBounds(350, 100, 1200, 800);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);

		panelMapa = new JPanel();
		panelMapa.setLayout(new BorderLayout());
		panelMapa.setBounds(0, 0, 1184, 698);	
		
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
		mainWindow.getContentPane().add(panelMapa);
		
		panelBotones = new JPanel();
		panelBotones.setBackground(SystemColor.activeCaption);
		panelBotones.setBounds(0, 697, 1233, 64);
		
		btnAgregarEstacion = new JButton("Agregar Estación");
		btnAgregarSendero = new JButton("Conectar Estaciones");
		panelBotones.add(btnAgregarEstacion);
		panelBotones.add(btnAgregarSendero);
		mainWindow.getContentPane().add(panelBotones);
		
		detectarEstacionPorClick();
		detectarBtnAgregarEstacion();
		detectarBtnAgregarSendero();
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
	
	private void detectarBtnAgregarEstacion() {
		btnAgregarEstacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarEstacion();}
		});
	}
	
	private void detectarBtnAgregarSendero() {
		btnAgregarSendero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarSendero();}
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
	
	private void agregarSendero() {
        //	Verificar que haya al menos 2 estaciones
        if (cantEstaciones < 2) {
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
            controlador.agregarSendero(inicio, fin, impacto);
            
        } catch (NumberFormatException e) {
            mostrarError("El impacto debe ser un número válido");
        }
    }

    // Método auxiliar para seleccionar una estación (con lista desplegable)
    private Estacion seleccionarEstacion(String mensaje) {
        // Crear arreglo con los nombres de las estaciones
        String[] estaciones = new String[controlador.cantidadEstaciones()];
        for (int i = 0; i < estaciones.length; i++)
            estaciones[i] = controlador.getEstaciones().get(i).getNombre();
        

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
			for (Estacion estacion : controlador.getEstaciones())
				if (estacion.getNombre().equals(seleccion))
					return estacion;
				
        return null;
    }

    public void dibujarSendero(MapPolygonImpl sendero) {
        mapa.addMapPolygon(sendero);
    }
	
    // Método auxiliar para mostrar errores
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
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


}