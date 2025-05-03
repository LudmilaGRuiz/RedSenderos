package vista;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private PanelMapa panelMapa;
    private JButton btnAgregarEstacion, btnAgregarSendero;

    public MainWindow() {
        setTitle("Red de Senderos");
        setLayout(new BorderLayout());
        
        panelMapa = new PanelMapa();
        add(panelMapa, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnAgregarEstacion = new JButton("Agregar Estación");
        btnAgregarSendero = new JButton("Conectar Estaciones");
        
        panelBotones.add(btnAgregarEstacion);
        panelBotones.add(btnAgregarSendero);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Getters
    public JButton getBtnAgregarEstacion() { 
    	return btnAgregarEstacion; 
    	}
    
    public JButton getBtnAgregarSendero() { 
    	return btnAgregarSendero;
    	}
    
    public PanelMapa getPanelMapa() { 
    	return panelMapa;
    	}
}