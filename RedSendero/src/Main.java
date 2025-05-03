import model.*;
import vista.*;
import controlador.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        //Inicialización de componentes
        Grafo grafo = new Grafo();
        MainWindow ventana = new MainWindow();
        Controlador controlador = new Controlador(grafo, ventana);

        //Configuración de botones
        ventana.getBtnAgregarEstacion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.agregarEstacion();
            }
        });

        ventana.getBtnAgregarSendero().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.agregarSendero();
            }
        });

        // Datos de prueba
        agregarDatosPrueba(grafo, ventana);

        //Configuración de la ventana
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }

    // Metodos de prueba
    private static void agregarDatosPrueba(Grafo grafo, MainWindow ventana) {
        Estacion m = new Estacion("Mirador", 100, 100);
        Estacion r = new Estacion("Refugio", 300, 200);
        Estacion c = new Estacion("Cascada", 200, 300);
        
        grafo.agregarEstacion(m);
        grafo.agregarEstacion(r);
        grafo.agregarEstacion(c);
        
        grafo.agregarSendero(m, r, 3);
        grafo.agregarSendero(r, c, 7);
        
        ventana.getPanelMapa().setGrafo(grafo);
    }
}