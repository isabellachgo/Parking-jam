package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;

public class EndGameView {
    private JFrame frame;
    private Menu menu;
    private Game game;
    private Controller cont;

    public EndGameView(JFrame frame, Menu menu, Game g, Controller cont) {
        this.frame = frame;
        this.menu = menu;
        this.cont = cont;
        this.game = g;
        initEG();
        this.frame.setVisible(true);
    }

    private void initEG() {
       
        // Dimensiones:
        Dimension buttonSize = new Dimension(60, 60);
        // Colores:
        Color bg = new Color(180, 220, 110);
        Color buttonColor = new Color(39, 193, 245); // Azul
        Color buttonActionColor = new Color(100, 170, 200);
        Color winPColor = new Color(180,220,110);

        // Iconos:
        ImageIcon levelsMIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/levelsMenu.png")), 30, 30);
        ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/finalParking.png"));
        ImageIcon starIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/yellowstar.png")), 50, 50);
        ImageIcon starIcon2 = Factory.rotateIcon(starIcon, 180);
        ImageIcon saveMIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/save.png")),30,30);
        ImageIcon homeMIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/home.png")),30,30);
        ImageIcon closeMIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/close.png")),30,30);
        // Imagenes:
        Image parkingImage = parkingIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);

        // Elementos:
        JLabel titleL = new JLabel();
        titleL.setText("Parking Jam");
        titleL.setFont(Factory.titleFont);
        titleL.setHorizontalAlignment(SwingConstants.CENTER);

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
        panelBg.setBounds(0, 0, 900, 900);

        JPanel winPanel = new JPanel();
        winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
        winPanel.setBackground(winPColor);
        winPanel.setBounds(123, 230, 450, 300);

       
        JLabel winL = new JLabel("Congratulations!");
        winL.setFont(Factory.titleFont);
        winL.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel info = new JLabel("<html><div style='text-align: center;'>You have completed all the levels in the game '" + game.getName() + "'</div></html>");
        info.setFont(Factory.infoFont);
        info.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        info.setPreferredSize(new Dimension(400, 90));  // Set preferred size to ensure it fits

        JLabel pointsWL = new JLabel(game.getGamePoints().toString());
        pointsWL.setFont(Factory.levelPointsFont);
        pointsWL.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel star1W = new JLabel(starIcon);
        JLabel star2W = new JLabel(starIcon2);
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        starPanel.setBackground(winPColor);
        starPanel.add(star1W);
        starPanel.add(pointsWL);
        starPanel.add(star2W);

        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonpanel.setBackground(winPColor);

        JButton gamesB = new JButton(homeMIcon);
        Factory.setFormatButton(gamesB, null, buttonSize, null, null, buttonColor, null, null);       
        gamesB.setAlignmentX(JButton.CENTER_ALIGNMENT);
        gamesB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                cont.gamesMenuButton();
            }
        });

        JButton closeB = new JButton(closeMIcon);
        Factory.setFormatButton(closeB, null, buttonSize, null, null, buttonColor, null, null);
        closeB.setAlignmentX(JButton.CENTER_ALIGNMENT);
        closeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        JButton saveB = new JButton(saveMIcon);
        Factory.setFormatButton(saveB, null, buttonSize, null, Color.white, buttonColor, null, SwingConstants.LEFT);
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				cont.saveGame();
			}
		});

        buttonpanel.add(gamesB);
        buttonpanel.add(closeB);
        buttonpanel.add(saveB);
        winPanel.add(Box.createVerticalGlue());
        winPanel.add(winL);
        winPanel.add(info);
        winPanel.add(starPanel);
        winPanel.add(buttonpanel);
        winPanel.add(Box.createVerticalGlue());

        panelCenter.add(panelBg, JLayeredPane.DEFAULT_LAYER);
        panelCenter.add(winPanel, JLayeredPane.PALETTE_LAYER);

        panel.add(panelCenter, BorderLayout.CENTER);
        frame.add(panel);
        Factory.playSound("src/main/resources/sounds/winGame.wav");
    }

    
 
}