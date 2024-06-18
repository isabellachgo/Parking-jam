package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;

public class StartView {
    private JFrame frame;
    private Menu menu;
    private Controller cont;

    public StartView(JFrame frame, Controller cont) {
        this.frame = frame;
        this.menu = new Menu();
        this.cont = cont;
        initSV();
        this.frame.setVisible(true);
    }

    private void initSV() {
        // Dimensiones:
        Dimension buttonSize = new Dimension(200, 50);

        // Colores:
        Color bg = new Color(180, 220, 110);
        Color buttonColor = new Color(39, 193, 245); // Azul
        Color buttonActionColor = new Color(100, 170, 200);

        // Iconos:
        ImageIcon startIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/playicon.png")), 97, 60);
        ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parkinglogo.png"));

        // Imagenes:
        Image parkingImage = parkingIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);

        // Elementos:
        JLabel titleL = new JLabel();
        titleL.setText("Parking Jam");
        titleL.setFont(Factory.titleFont);
        titleL.setHorizontalAlignment(SwingConstants.CENTER);

        JButton startButton = new JButton();
        startButton.setPreferredSize(buttonSize);
        startButton.setIcon(startIcon);
        startButton.setBackground(buttonColor);
        startButton.setFont(Factory.buttonFont);
        startButton.setHorizontalAlignment(SwingConstants.CENTER);
        startButton.setFocusPainted(false);
        Timer timer = new Timer(500, e -> {
            Color currentColor = startButton.getBackground();
            startButton.setBackground(currentColor == buttonColor ? buttonActionColor : buttonColor);
        });
        timer.start();

        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            Factory.playSound("src/main/resources/sounds/gameStart.wav");
            cont.gamesMenu(menu);
        });

        // Estructura:
        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.setLayout(new BorderLayout());

        JLayeredPane panelCenter = new JLayeredPane();
        panelCenter.setPreferredSize(new Dimension(900, 900));

        JPanel panelBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(parkingImage, 50, 30, this);
                
            }
        };
        panelBg.setBackground(bg);
        panelBg.setBounds(0, 0, 950, 750);

        startButton.setBounds(250, 540, 200, 50); // Ajusta la posición y tamaño del botón

        panelCenter.add(panelBg, JLayeredPane.DEFAULT_LAYER);
        panelCenter.add(startButton, JLayeredPane.PALETTE_LAYER);

        panel.add(panelCenter, BorderLayout.CENTER);
        frame.add(panel);
    }

}