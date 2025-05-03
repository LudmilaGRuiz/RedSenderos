package vista;

// Imports explícitos para model
import model.Estacion;
import model.Grafo;
import model.Sendero;

// Imports explícitos para Swing y AWT
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class PanelMapa extends JPanel {
    private Grafo grafo;

    public PanelMapa() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 400));
    }

    public void setGrafo(Grafo nuevoGrafo) {
        this.grafo = nuevoGrafo;
        repaint(); // Vuelve a dibujar el componente
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Llama al método de la clase padre primero
        super.paintComponent(g);
        
        // No dibujar si no hay grafo
        if (grafo == null) {
            return;
        }

        // Dibujar todos los senderos
        java.util.List<Sendero> senderos = grafo.getSenderos();
        for (int i = 0; i < senderos.size(); i++) {
            Sendero s = senderos.get(i);
            g.setColor(obtenerColorImpacto(s.getImpacto()));
            g.drawLine(
                s.getInicio().getX(), 
                s.getInicio().getY(),
                s.getFin().getX(), 
                s.getFin().getY()
            );
        }

        // Dibujar todas las estaciones
        g.setColor(Color.RED);
        java.util.List<Estacion> estaciones = grafo.getEstaciones();
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion e = estaciones.get(i);
            // Dibuja un círculo
            g.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);
            // Dibuja el nombre al lado
            g.drawString(e.getNombre(), e.getX() + 15, e.getY());
        }
    }

    private Color obtenerColorImpacto(int impacto) {
        if (impacto >= 8) {
            return Color.RED;
        } else if (impacto >= 5) {
            return Color.ORANGE;
        } else {
            return Color.GREEN;
        }
    }
}