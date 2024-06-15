package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import es.upm.pproject.parkingjam.parking_jam.controller.controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;

public class EndGameView {
    private JFrame frame;
    private Menu menu;
    private Game game;
    private int pos;
    private controller cont;

    public EndGameView(JFrame frame, Menu menu, Game g, controller cont) {
        this.frame = frame;
        this.menu = menu;
        this.cont = cont;
        this.game = g;
        for (int i = 0; i < menu.getNumGames(); i++) {
            if (menu.getGames().get(i).equals(g)) {
                this.pos = i;
            }
        }

        initSV();
        this.frame.setVisible(true);
    }

    private void initSV() {
        // Fuentes:
        Font titleFont = null;
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/titlefont.ttf")).deriveFont(45f);
        } catch (FontFormatException | IOException e1) {
            e1.printStackTrace();
        }
        Font buttonFont = null;
        try {
            buttonFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/menuText.ttf")).deriveFont(30f);
        } catch (FontFormatException | IOException e1) {
            e1.printStackTrace();
        }
        Font levelPointsFont = null;
        try {
            levelPointsFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/pointsfont.ttf")).deriveFont(27f);
        } catch (FontFormatException | IOException e1) {
            e1.printStackTrace();
        }

        // Dimensiones:
        Dimension buttonSize = new Dimension(200, 50);

        // Colores:
        Color bg = new Color(180, 220, 110);
        Color buttonColor = new Color(39, 193, 245); // Azul
        Color buttonActionColor = new Color(100, 170, 200);
        Color winPColor = new Color(180,220,110);
      //  Color borderWinPColor = new Color(50, 150, 90);
        //Color shadeWinPColor = new Color(57, 64, 50);

        // Iconos:
        ImageIcon levelsMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/levelsMenu.png")), 30, 30);
        ImageIcon startIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/playicon.png")), 97, 60);
        ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/finalParking.png"));
        ImageIcon starIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/yellowstar.png")), 30, 30);
        ImageIcon starIcon2 = rotateIcon(starIcon, 180);

        // Imagenes:
        Image parkingImage = parkingIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);

        // Elementos:
        JLabel titleL = new JLabel();
        titleL.setText("Parking Jam");
        titleL.setFont(titleFont);
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

      //  BevelBorder b = new BevelBorder(BevelBorder.RAISED, borderWinPColor, shadeWinPColor);
       // winPanel.setBorder(b);

        JLabel winL = new JLabel("Congratulations!");
        winL.setFont(titleFont);
       // winL.setAlignmentX(JLabel.);
        
        JLabel info = new JLabel("You have passed all the levels in the game '"+ game.getName()+"'");
        winL.setFont(titleFont);
    //    winL.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JLabel pointsWL = new JLabel(game.getGamePoints().toString()); // TODO : valor a partir de game
        pointsWL.setFont(levelPointsFont);
        pointsWL.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel star1W = new JLabel(starIcon);
        JLabel star2W = new JLabel(starIcon2);
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        starPanel.setBackground(winPColor);
        starPanel.add(star1W);
        starPanel.add(pointsWL);
        starPanel.add(star2W);

        JButton levelsWB = new JButton(levelsMIcon);
        levelsWB.setBackground(buttonColor);
        levelsWB.setPreferredSize(buttonSize);
        levelsWB.setAlignmentX(JButton.CENTER_ALIGNMENT);
        levelsWB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("levels button pressed");
                frame.getContentPane().removeAll();
                cont.levelMenuButon();
            }
        });

        winPanel.add(Box.createVerticalGlue());
        winPanel.add(winL);
        winPanel.add(info);
        winPanel.add(starPanel);
        winPanel.add(levelsWB);
        winPanel.add(Box.createVerticalGlue());

        panelCenter.add(panelBg, JLayeredPane.DEFAULT_LAYER);
        panelCenter.add(winPanel, JLayeredPane.PALETTE_LAYER);

        panel.add(panelCenter, BorderLayout.CENTER);
        frame.add(panel);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int i, int j) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(i, j, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private ImageIcon rotateIcon(ImageIcon icon, double angle) {
        Image img = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        // Rotar la imagen usando AffineTransform
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), img.getWidth(null) / 2, img.getHeight(null) / 2);
        g2d.setTransform(transform);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(bufferedImage);
    }
}