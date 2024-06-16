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
import java.util.ArrayList;

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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import es.upm.pproject.parkingjam.parking_jam.controller.controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;

public class GamesMenuView {

	private JFrame frame ;
	private Menu menu;
	private controller cont;

	public GamesMenuView(JFrame frame, Menu menu, controller cont) {
		this.frame = frame;
		this.menu = menu;
		this.cont = cont;
		initGMV();
		this.frame.setVisible(true);
	}

	public void initGMV() {
		
		// Altura scroll:
		Integer listH = (menu.getNumGames()+1)*80 + (menu.getNumGames())*10;
		Integer pictureH = listH +80;
		Integer gamesH = pictureH +80;
		Integer scrollH = gamesH + 40;

		// Fuentes:
		Font titleFont = null;
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/titlefont.ttf")).deriveFont(45f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font gameFont = null;
		try {
			gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/menuText.ttf")).deriveFont(30f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font newGameFont = null;
		try {
			newGameFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/menuText.ttf")).deriveFont(23f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font menuFont = null;
		try {
			menuFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/menuText.ttf")).deriveFont(16f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		
		// Dimensiones:
		Dimension gameButtonSize = new Dimension(420, 80);
		Dimension buttonSize2 = new Dimension(180,40);

		// Colores:
		Color bg = new Color(180,220,110);
		Color buttonColor = new Color(65,130,4); //verde oscuro
		Color gameBColor = new Color(252,231,68); //amarillo
		
		// Iconos:
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		ImageIcon addIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/add_black.png")),40,40);
		ImageIcon carIcon = resizeIcon( new ImageIcon(getClass().getResource("/icons/car.png")),40,40);
		ImageIcon loadIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/upload.png")),30,30);
		
		
		// Imagenes:
		Image parkingImage= parkingIcon.getImage().getScaledInstance(500, Math.max(pictureH, 430), Image.SCALE_SMOOTH);		
		
		// Elementos:
		JButton loadGameB = new JButton("Load game");
		loadGameB.setPreferredSize(buttonSize2);
		loadGameB.setIcon(loadIcon);
		loadGameB.setBackground(buttonColor);
		loadGameB.setForeground(Color.white);
		loadGameB.setFont(menuFont);
		loadGameB.setHorizontalAlignment(SwingConstants.CENTER);
		loadGameB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("load game button pressed");
				
				frame.getContentPane().removeAll();
				cont.openSavedGames();
			}
		});

		JLabel titleL = new JLabel();
		titleL.setText("Parking Jam");
		titleL.setFont(titleFont);
		
		JPanel panelGameName = new JPanel();
		panelGameName.setLayout(new BorderLayout());
		
		JLabel pgnText = new JLabel();
		pgnText.setFont(newGameFont);
		pgnText.setOpaque(true);
		pgnText.setText("Game name: ");
		
		JTextField pgnInput = new JTextField();
		pgnInput.setBackground(Color.white);
		
		panelGameName.add(pgnText, BorderLayout.NORTH);
		panelGameName.add(pgnInput, BorderLayout.CENTER);
				
		JButton addGameB = new JButton("New game");
		addGameB.setPreferredSize(gameButtonSize);
		addGameB.setIcon(addIcon);
		addGameB.setBackground(gameBColor);
		addGameB.setFont(gameFont);
		addGameB.setHorizontalAlignment(SwingConstants.LEFT);
		addGameB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("new game button pressed");
				
				JDialog dialog = new JOptionPane(panelGameName, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, carIcon).createDialog(frame, "New Game");
				dialog.setLocationRelativeTo(frame);
				dialog.setVisible(true);
				Object res = ((JOptionPane) dialog.getContentPane().getComponent(0)).getValue();
				
				if(res!=null && res instanceof Integer && (Integer)res == JOptionPane.OK_OPTION) {
					String input = pgnInput.getText();
					if(input != null && !input.trim().isEmpty()) {
						System.out.println("Ok new game button pressed");
						frame.getContentPane().removeAll();
						if(cont.newGame(input) == 1) {
							JDialog existingGame = new JDialog(frame, "Existing Game", true);
							existingGame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							existingGame.setSize(new Dimension(400,200));
							JLabel errorM = new JLabel("There is alredy a game with the same name, please change the name of the new game.");
							errorM.setForeground(Color.red);
							JPanel errorP = new JPanel();
							errorP.setLayout(new BoxLayout(errorP, BoxLayout.Y_AXIS));
							errorP.add(errorM);
							existingGame.add(errorP);
							existingGame.pack();
							existingGame.setLocationRelativeTo(frame);
							existingGame.setVisible(true);
						}
					}
				}
			}
		});
		
		ArrayList<JButton> buttons = new ArrayList<>();
		ArrayList<Game> games = menu.getGames();
		for(Game g : games) {
			JButton b = new JButton(" "+g.getName());
			b.setPreferredSize(gameButtonSize);
			b.setIcon(carIcon);
			b.setBackground(gameBColor);
			b.setFont(gameFont);
			b.setHorizontalAlignment(SwingConstants.LEFT);
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(g.getName()+" game button pressed");
					frame.getContentPane().removeAll();
					cont.openGame(g);
				}
			});
			
			buttons.add(b);
		}
				
		// Estructura:
		JPanel panel = new JPanel();
		panel.setBackground(bg);
		panel.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel();
		panelNorth.setBackground(bg);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));
		JPanel row0= new JPanel(new FlowLayout(FlowLayout.LEFT));
		row0.setBackground(bg);
		row0.add(new JLabel(" "));
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		row1.setBackground(bg);
		row1.add(titleL);
		JPanel row2= new JPanel(new FlowLayout(FlowLayout.CENTER));
		row2.setBackground(bg);
		row2.add(new JLabel(" "));
		
		panelNorth.add(row0);
		panelNorth.add(row1);
		panelNorth.add(row2);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		panelCenter.setBackground(bg);
		panelCenter.setPreferredSize(new Dimension(700,Math.max(scrollH, 540)));
		panelCenter.setBounds(0,0,700, Math.max(scrollH, 550));
		
		JPanel panelLoadGameB = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelLoadGameB.setBackground(bg);
		panelLoadGameB.add(loadGameB);
		
		JLayeredPane panelGames = new JLayeredPane();
		panelGames.setBackground(bg);
		panelGames.setPreferredSize(new Dimension(700,Math.max(gamesH, 500)));
		panelGames.setBounds(0,0,700, Math.max(gamesH, 500));
		
		JPanel panelBg = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(parkingImage,100, 30, this);
			}
		};
		panelBg.setBackground(bg);
		panelBg.setBounds(0, 0, 700, Math.max(gamesH, 500) ); 
		
		JPanel panelElem = new JPanel();
		panelElem.setBounds(140, 70, 420, listH);
		panelElem.setBackground(new Color(0,0,0,0));
		panelElem.setLayout(new BoxLayout(panelElem, BoxLayout.Y_AXIS));
		JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		row3.setBackground(new Color(0,0,0,0));
		row3.add(addGameB);
		panelElem.add(row3);
		
		for(JButton b : buttons) {
			JPanel rowX = new JPanel(new FlowLayout(FlowLayout.CENTER));
			rowX.setBackground(new Color(0,0,0,0));
			rowX.add(b);
			panelElem.add(rowX);
		}
		
		panelGames.add(panelBg, JLayeredPane.DEFAULT_LAYER);
		panelGames.add(panelElem, JLayeredPane.PALETTE_LAYER);
		panelGames.revalidate();
		panelGames.repaint();
		
		panelCenter.add(panelLoadGameB, BorderLayout.NORTH);
		panelCenter.add(panelGames, BorderLayout.CENTER);
		
		JScrollPane panelScroll = new JScrollPane(panelCenter);
		panelScroll.setBackground(bg);
		panelScroll.setBounds(0,0,700,550);
		panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			
		
		panel.add(panelNorth, BorderLayout.NORTH);
		panel.add(panelScroll, BorderLayout.CENTER);
		frame.add(panel);
	}
	
	
	private ImageIcon resizeIcon(ImageIcon icon, int i, int j) {
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(i, j, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImg);
	}

}
