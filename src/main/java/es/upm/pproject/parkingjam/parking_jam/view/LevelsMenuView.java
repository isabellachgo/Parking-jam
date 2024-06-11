package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import es.upm.pproject.parkingjam.parking_jam.controller.controller;

public class LevelsMenuView {
	
	private JFrame frame;
	
	public LevelsMenuView(JFrame frame/*, Game game, controller controller*/) {
		// TODO : también debe recivir un Game
		this.frame = frame;
		initLMV();
		this.frame.setVisible(true);
	}
	
	private void initLMV() {
		
		// Fuentes:
		Font titleFont = null;
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/titlefont.ttf")).deriveFont(35f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font gamePointsFont = null;
		try {
			gamePointsFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/pointsfont.ttf")).deriveFont(23f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font menuFont = null;
		try {
			menuFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/menuText.ttf")).deriveFont(16f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font levelFont = null;
		try {
			levelFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/titlefont.ttf")).deriveFont(27f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		
		// Colores:
		Color bg = new Color(180,220,110);
		Color buttonColor = new Color(65,130,4); 
		Color levelBColor = new Color(39,193,245);
		Color lockedLevelBColor = new Color(80,155,180);
		
		// Dimensiones:
		Dimension buttonSize = new Dimension(40,40);
		Dimension levelBSize = new Dimension(80,80);
		Dimension buttonSize2 = new Dimension(195,40);
		
		// Iconos:
		ImageIcon closeMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/close.png")),30,30);
		ImageIcon addMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/add.png")),30,30);
		ImageIcon saveMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/save.png")),30,30);
		ImageIcon loadMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/upload.png")),30,30);
		ImageIcon menuIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/menu.png")),30,30);
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		
		// Imagenes:
		Image parkingImage= parkingIcon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);
		
		// Elementos:
		JPopupMenu menuPanel = new JPopupMenu();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuPanel.setPopupSize(207,195);
		menuPanel.setBounds(50, 80, 207, 193);

		JButton saveB = new JButton("save game");
		saveB.setPreferredSize(buttonSize2);
		saveB.setIcon(saveMIcon);
		saveB.setBackground(buttonColor);
		saveB.setForeground(Color.white);
		saveB.setFont(menuFont);
		saveB.setHorizontalAlignment(SwingConstants.LEFT);
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("save button pressed");
			}
		});
		JButton loadB= new JButton("load game");
		loadB.setPreferredSize(buttonSize2);
		loadB.setIcon(loadMIcon);
		loadB.setBackground(buttonColor);
		loadB.setForeground(Color.white);
		loadB.setFont(menuFont);
		loadB.setHorizontalAlignment(SwingConstants.LEFT);
		loadB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("load button pressed");
			}
		});
		JButton newGameB = new JButton("new game");
		newGameB.setPreferredSize(buttonSize2);
		newGameB.setIcon(addMIcon);
		newGameB.setBackground(buttonColor);
		newGameB.setForeground(Color.white);
		newGameB.setFont(menuFont);
		newGameB.setHorizontalAlignment(SwingConstants.LEFT);
		newGameB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("nwe game button pressed");
			}
		});
		JButton closeB = new JButton("close Parking Jam");
		closeB.setPreferredSize(buttonSize2);
		closeB.setIcon(closeMIcon);
		closeB.setBackground(buttonColor);
		closeB.setForeground(Color.white);
		closeB.setFont(menuFont);
		closeB.setHorizontalAlignment(SwingConstants.LEFT);
		closeB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("close button pressed");
				frame.dispose();
			}
		});

		menuPanel.add(saveB);
		menuPanel.add(loadB);
		menuPanel.add(newGameB);
		menuPanel.add(closeB);
		
		JButton menuB = new JButton();
		menuB.setPreferredSize(buttonSize);
		menuB.setIcon(menuIcon);
		menuB.setBackground(buttonColor);	
		menuB.setForeground(Color.white);
		menuB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("menu button pressed ");

				if(!menuPanel.isVisible()) {
					menuPanel.show(frame, 50, 87);
					menuPanel.setVisible(true);
				} else {
					menuPanel.setVisible(false);
				}
			}
		});
		
		
		JLabel gamePointsL = new JLabel();
		gamePointsL.setFont(gamePointsFont);
		gamePointsL.setText("Game Points: ");
		
		JLabel gamePointsVL = new JLabel();
		gamePointsVL.setFont(gamePointsFont);
		gamePointsVL.setText("xx"); //TODO : valor a partir de game
		
		JLabel gameNameL = new JLabel();
		gameNameL.setFont(titleFont);
		gameNameL.setText("Partida 1"); //TODO : nombre a parir de game
		
		JButton l1B = new JButton();
		l1B.setText("1");
		l1B.setFont(levelFont);
		l1B.setPreferredSize(levelBSize);
		l1B.setBackground(levelBColor);
		l1B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 
				// llamar a controller
				// abrir view nivel 1
				System.out.println("level 1");
				
				//controller.showLevel(1);
			}
		});
		JButton l2B = new JButton();
		l2B.setText("2");
		l2B.setFont(levelFont);
		l2B.setPreferredSize(levelBSize);
		l2B.setBackground(lockedLevelBColor);
		l2B.setEnabled(false);
		l2B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 
				// llamar a controller
				// abrir view nivel 2
				System.out.println("level 2");
			}
		});
		JButton l3B = new JButton();
		l3B.setText("3");
		l3B.setFont(levelFont);
		l3B.setPreferredSize(levelBSize);
		l3B.setBackground(lockedLevelBColor);
		l3B.setEnabled(false);
		l3B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 
				// llamar a controller
				// abrir view nivel 3
				System.out.println("level 3");
			}
		});
		JButton l4B = new JButton();
		l4B.setText("4");
		l4B.setFont(levelFont);
		l4B.setPreferredSize(levelBSize);
		l4B.setBackground(lockedLevelBColor);
		l4B.setEnabled(false);
		l4B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 
				// llamar a controller
				// abrir view nivel 4
				System.out.println("level 4");
			}
		});
		
		
		// Estructura:
		JPanel panel = new JPanel();
		panel.setBackground(bg);
		panel.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel();
		panelNorth.setBackground(bg);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		row1.setBackground(bg);
		row1.add(Box.createHorizontalStrut(30));
		row1.add(menuB);
		row1.add(Box.createHorizontalStrut(390));
		row1.add(gamePointsL);
		row1.add(gamePointsVL);
		row1.add(Box.createHorizontalStrut(30));
		
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		row2.setBackground(bg);
		row2.add(gameNameL);
		
		panelNorth.add(row1);
		panelNorth.add(row2);
		
		
		JLayeredPane panelCenter = new JLayeredPane();
		panelCenter.setPreferredSize(new Dimension(500,500));
		
		JPanel panelBg = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(parkingImage,100,75 , this);
			}
		};
		panelBg.setBackground(bg);
		panelBg.setBounds(0, 10, 700, 500);
		
		JPanel panelElem = new JPanel();
		panelElem.setBounds(100, 160, 500, 300);
		panelElem.setBackground(new Color(3,3,3,0));
		panelElem.setLayout(new BoxLayout(panelElem, BoxLayout.Y_AXIS));
		JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		row3.setBackground(new Color(3,3,3,0));
		row3.add(l1B);
		row3.add(Box.createHorizontalStrut(60));
		row3.add(l2B);
		JPanel row4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		row4.setBackground(new Color(3,3,3,0));
		row4.add(l3B);
		row4.add(Box.createHorizontalStrut(60));
		row4.add(l4B);
		panelElem.add(row3);
		panelElem.add(row4);
		
		panelCenter.add(panelBg, JLayeredPane.DEFAULT_LAYER);
		panelCenter.add(panelElem, JLayeredPane.PALETTE_LAYER);
		panelCenter.revalidate();
		panelCenter.repaint();
		
		panel.add(panelNorth, BorderLayout.NORTH);
		panel.add(panelCenter, BorderLayout.CENTER);
		frame.add(panel);
	}
	
	
	private ImageIcon resizeIcon(ImageIcon icon, int i, int j) {
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(i, j, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImg);
	}
	
	private void unlockLevel(Integer n) {
		// TODO
		// desbloquear siguiente nivel
		// actualizar puntos game
	}
	
	
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setTitle("Parking Game");
		f.setSize(700, 700);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE );
		f.setResizable(false);  // Impide que la ventana sea redimensionable
		
		LevelsMenuView lmv = new LevelsMenuView(f);
		
	}
	
}
