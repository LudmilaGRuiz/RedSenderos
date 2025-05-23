package controlador;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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
	
	
	public void nuevoSendero(Estacion inicio, Estacion fin, int impacto) {
	    if (inicio != null && fin != null && !inicio.equals(fin)) {
	        grafo.agregarSendero(inicio, fin, impacto);
			grafo.agregarSendero(fin, inicio, impacto); 

	        vista.dibujarSendero(inicio.getX(),inicio.getY(),fin.getX(),fin.getY(), impacto);
	    } else {
	        vista.mostrarError("Estación origen o destino no encontrada\n o son la misma");
	    }
	}

	public void caminoMinimo() {
		int sumaImpacto = 0;
		List<Sendero> agmPrim = Prim.prim(grafo.getEstaciones(), grafo.getSenderos(), grafo.getEstaciones().get(0));
		if(agmPrim==null || agmPrim.isEmpty())return;
		for(Sendero s : agmPrim) {
			sumaImpacto += s.getImpacto();
			double inicioX = s.getInicio().getX();
			double inicioY = s.getFin().getY();
			double finX = s.getFin().getX();
			double finY = s.getFin().getY();
			vista.dibujarSendero(inicioX, inicioY, finX, finY ,s.getImpacto());
		}
		vista.mostrarMensaje("El impacto total del árbol generador mínimo es: " + sumaImpacto);
	}

	public Estacion seleccionarEstacion(String mensaje) {
        String[] estaciones = new String[grafo.getEstaciones().size()];
        for (int i = 0; i < estaciones.length; i++)
            estaciones[i] = grafo.getEstaciones().get(i).getNombre();
 
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            mensaje,
            "Selección",
            JOptionPane.PLAIN_MESSAGE,
            null,
            estaciones,
            estaciones[0]
        );
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
				double inicioX = sendero.getInicio().getX();
				double inicioY = sendero.getFin().getY();
				double finX = sendero.getFin().getX();
				double finY = sendero.getFin().getY();
				vista.dibujarSendero(inicioX, inicioY, finX, finY, sendero.getImpacto());
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