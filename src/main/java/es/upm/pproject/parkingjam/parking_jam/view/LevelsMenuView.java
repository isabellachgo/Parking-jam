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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import javax.swing.SwingConstants;


import es.upm.pproject.parkingjam.parking_jam.controller.controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;

public class LevelsMenuView {

	private JFrame frame;
	private Game game;
	private controller cont;

	Font menuFont;
	Color levelBColor;
	Color lockedLevelBColor;
	
	private JButton l1B;
	private JButton l2B;
	private JButton l3B;
	private JButton l4B;

	public LevelsMenuView(JFrame frame, Game game, controller cont) {
		this.frame = frame;
		this.game = game;
		this.cont = cont;
				
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
		menuFont = null;
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
		levelBColor = new Color(39,193,245);
		lockedLevelBColor = new Color(80,155,180);

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
		ImageIcon homeMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/home.png")),30,30);


		// Imagenes:
		Image parkingImage= parkingIcon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);

		// Elementos:
		JPopupMenu menuPanel = new JPopupMenu();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuPanel.setPopupSize(207,145);
		menuPanel.setBounds(50, 80, 207, 145);

		JButton gamesB= new JButton("games menu");
		setFormatButton(gamesB, null, buttonSize2, homeMIcon, Color.white, buttonColor,  menuFont, SwingConstants.LEFT);
		gamesB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				cont.gamesMenuButton();
			}
		});
		JButton saveB = new JButton("save game");
		setFormatButton(saveB, null, buttonSize2, saveMIcon, Color.white, buttonColor,  menuFont, SwingConstants.LEFT);
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cont.saveGame();
			}
		});
		JButton closeB = new JButton("close Parking Jam");
		setFormatButton(closeB, null, buttonSize2, closeMIcon, Color.white, buttonColor,  menuFont, SwingConstants.LEFT);
		closeB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		menuPanel.add(gamesB);
		menuPanel.add(saveB);
		menuPanel.add(closeB);

		JButton menuB = new JButton();
		setFormatButton(menuB, null, buttonSize, menuIcon, Color.white, buttonColor, null, null);
		menuB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
		gamePointsVL.setText(game.getGamePoints().toString()); 

		JLabel gameNameL = new JLabel();
		gameNameL.setFont(titleFont);
		gameNameL.setText(game.getName()); 

		l1B = new JButton();
		setFormatButton(l1B, "1", levelBSize, null, null, null,  levelFont, null);
		levelsStatus(l1B);
		l1B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(1)==1) {
						corruptLevel(1);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		l2B = new JButton();
		setFormatButton(l2B, "2", levelBSize, null, null, null,  levelFont, null);
		levelsStatus(l2B);
		l2B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(2)==1) {
						corruptLevel(2);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		l3B = new JButton();
		setFormatButton(l3B, "3", levelBSize, null, null, null,  levelFont, null);
		levelsStatus(l3B);
		l3B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(3)==1) {
						corruptLevel(3);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		l4B = new JButton();
		setFormatButton(l4B, "4", levelBSize, null, null, null,  levelFont, null);
		levelsStatus(l4B);
		l4B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(4)==1) {
						corruptLevel(4);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	private void levelsStatus(JButton b) {
		int last = game.getUltimoLevelPassed() + 1;
		if(Integer.parseInt(b.getText()) <= last && game.getOkLevel(Integer.parseInt(b.getText())) ) {
			b.setBackground(levelBColor);
			b.setEnabled(true);
		} else {
			b.setBackground(lockedLevelBColor);
			b.setEnabled(false);
		}
	}
	
	private void corruptLevel(Integer n) {
		JPanel errorP = new JPanel();
		errorP.setLayout(new BorderLayout());
		JLabel peText = new JLabel();
		peText.setFont(menuFont);
		peText.setOpaque(true);
		peText.setForeground(Color.red);
		peText.setText("Sorry, this level's file is corrupt, play the next level.");
		errorP.add(peText, BorderLayout.CENTER);
		
		JDialog dialog = new JOptionPane(errorP, JOptionPane.INFORMATION_MESSAGE).createDialog(frame, "Corrupt level");
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
		Object res = ((JOptionPane) dialog.getContentPane().getComponent(0)).getValue();

		game.setOkLevel(n, false);
		game.setUltimoLevelPassed(n);
		levelsStatus(l1B);
		levelsStatus(l2B);
		levelsStatus(l3B);
		levelsStatus(l4B);
		
		if( res instanceof Integer && (Integer)res == JOptionPane.OK_OPTION) {
			System.out.println("Ok new game button pressed");
			
			frame.getContentPane().removeAll();
			try {
				if(n==4) cont.endGame();
				else if(cont.showLevel(n+1) == 1) corruptLevel(n+1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	private void setFormatButton (JButton b, String t, Dimension size, ImageIcon ic, Color foreg, Color backg, Font font, Integer sc) {
		if(t!=null) b.setText(t);
		if(size!=null) b.setPreferredSize(size);
		if(ic!=null) b.setIcon(ic);
		if(foreg!=null) b.setForeground(foreg);
		if(backg!=null) b.setBackground(backg);
		if(font!=null) b.setFont(font);
		if(sc!=null) b.setHorizontalAlignment(sc);
	}

}
