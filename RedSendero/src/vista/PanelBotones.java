package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelBotones extends JPanel {
    private JButton btnAgregarEstacion, btnAgregarSendero, btnGenerarAGM, btnGuardar, btnCargar;

    public PanelBotones(MainWindow ventana) {
        setBackground(SystemColor.activeCaption);
        setBounds(1000, 0, 200, 800);
        setBorder(BorderFactory.createTitledBorder("Controles"));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));

        btnAgregarEstacion = new JButton("Agregar Estación");
        btnAgregarSendero = new JButton("Conectar Estaciones");
        btnGenerarAGM = new JButton("Camino minimo");
        btnGuardar = new JButton("Guardar");
        btnCargar = new JButton("Cargar");

        add(btnAgregarEstacion);
        add(btnAgregarSendero);
        add(btnGenerarAGM);
        add(btnGuardar);
        add(btnCargar);

        registrarEventos(ventana);
    }

    private void registrarEventos(MainWindow ventana) {
        btnAgregarEstacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.agregarEstacion();
            }
        });

        btnAgregarSendero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.agregarSendero();
            }
        });

        btnGenerarAGM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.arbolGeneradorMinimo();
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.guardarGrafo();
            }
        });

        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.cargarGrafo();
            }
        });
    }
}
