package controlador;

import java.io.File;
import java.util.List;

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
			vista.getPanelMapa().dibujarEstacion(nombre,x,y);
		} catch (Exception e) {
			vista.mostrarError("Error al agregar la estación: " + e.getMessage());
		}
	}	

	public String[] getNombresEstaciones() {
		String[] estaciones = new String[grafo.getEstaciones().size()];
		for (int i = 0; i < estaciones.length; i++)
			estaciones[i] = grafo.getEstaciones().get(i).getNombre();
		return estaciones;
	}
	
	public void agregarSendero(String inicio, String fin, int impacto) {
		if (inicio==null || fin==null || inicio.equals(fin))
			vista.mostrarError("Estación origen o destino no encontrada\n o son la misma");
		
		Estacion estacionInicio=null;
		Estacion estacionFin=null;
		for (Estacion estacion : grafo.getEstaciones()) {
			if (estacion.getNombre().equals(inicio))
				estacionInicio = estacion;
			else if (estacion.getNombre().equals(fin))
				estacionFin = estacion;
		}
	    grafo.agregarSendero(estacionInicio, estacionFin, impacto);
		grafo.agregarSendero(estacionFin, estacionInicio, impacto); 

		vista.getPanelMapa().dibujarSendero(estacionInicio.getX(),estacionInicio.getY(),estacionFin.getX(),estacionFin.getY(), impacto);
	}


	public void caminoMinimo() {
		int sumaImpacto = 0;
		List<Sendero> agmPrim = AGM.prim(grafo, grafo.getEstaciones().get(0));
		if(agmPrim==null || agmPrim.isEmpty())return;
		for(Sendero s : agmPrim) {
			sumaImpacto += s.getImpacto();
			double inicioX = s.getInicio().getX();
			double inicioY = s.getInicio().getY();
			double finX = s.getFin().getX();
			double finY = s.getFin().getY();
			vista.getPanelMapa().dibujarSendero(inicioX, inicioY, finX, finY ,s.getImpacto());
		}
		vista.mostrarMensaje("El impacto total del árbol generador mínimo es: " + sumaImpacto);
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
			vista.getPanelMapa().limpiarMapa();
			for (Estacion estacion : grafo.getEstaciones()) {
				vista.getPanelMapa().dibujarEstacion(estacion.getNombre(), estacion.getX(), estacion.getY());
			}
			for (Sendero sendero : grafo.getSenderos()) {
				double inicioX = sendero.getInicio().getX();
				double inicioY = sendero.getInicio().getY();
				double finX = sendero.getFin().getX();
				double finY = sendero.getFin().getY();
				vista.getPanelMapa().dibujarSendero(inicioX, inicioY, finX, finY, sendero.getImpacto());
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