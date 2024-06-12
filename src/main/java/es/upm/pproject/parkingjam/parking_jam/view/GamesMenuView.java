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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;

public class GamesMenuView {

	private JFrame frame ;
	private Menu menu;

	public GamesMenuView(JFrame frame, Menu menu) {
		this.frame = frame;
		this.menu = menu;
		initGMV();
		this.frame.setVisible(true);
	}

	public void initGMV() {
		
		// Altura scroll:
		Integer listH = (menu.getNumGames()+1)*80 + (menu.getNumGames())*10;
		Integer pictureH = listH +80;
		Integer scrollH = pictureH +100;

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
		
		// Dimensiones:
		Dimension gameButtonSize = new Dimension(420, 80);

		// Colores:
		Color bg = new Color(180,220,110);
		//Color buttonColor = new Color(65,130,4); //verde oscuro
		//Color gameBColor = new Color(39,193,245); //azul
		Color gameBColor = new Color(252,231,68); //amarillo
		//Color gameBColor = new Color(252,197,68); //naranja
		
		// Iconos:
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		ImageIcon addIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/add.png")),40,40);
		
		// Imagenes:
		Image parkingImage= parkingIcon.getImage().getScaledInstance(500, Math.max(pictureH, 450), Image.SCALE_SMOOTH);		
		
		// Elementos:
		JLabel titleL = new JLabel();
		titleL.setText("Parking Jam");
		titleL.setFont(titleFont);
		
		JButton addGameB = new JButton("New game");
		addGameB.setPreferredSize(gameButtonSize);
		addGameB.setIcon(addIcon);
		addGameB.setBackground(gameBColor);
		addGameB.setFont(gameFont);
		addGameB.setHorizontalAlignment(SwingConstants.LEFT);
		addGameB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("game button pressed");
			}
		});
		
		ArrayList<JButton> buttons = new ArrayList<>();
		ArrayList<Game> games = menu.getGames();
		for(Game g : games) {
			JButton b = new JButton(g.getName());
			b.setPreferredSize(gameButtonSize);
			b.setBackground(gameBColor);
			b.setFont(gameFont);
			b.setHorizontalAlignment(SwingConstants.LEFT);
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(g.getName()+" game button pressed");
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
		JPanel row0= new JPanel(new FlowLayout(FlowLayout.CENTER));
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
		
		
		JLayeredPane panelCenter = new JLayeredPane();
		panelCenter.setBackground(bg);
		panelCenter.setPreferredSize(new Dimension(700,Math.max(scrollH, 550)));
		panelCenter.setBounds(0,0,700, Math.max(scrollH, 550));
		
		JPanel panelBg = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(parkingImage,100, 50, this);
			}
		};
		panelBg.setBackground(bg);
		panelBg.setBounds(0, 0, 700, Math.max(scrollH, 550) );
		
		JPanel panelElem = new JPanel();
		panelElem.setBounds(140, 90, 420, listH);
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
		
		panelCenter.add(panelBg, JLayeredPane.DEFAULT_LAYER);
		panelCenter.add(panelElem, JLayeredPane.PALETTE_LAYER);
		panelCenter.revalidate();
		panelCenter.repaint();
		
		JScrollPane panelScroll = new JScrollPane(panelCenter);
		panelScroll.setBackground(bg);
		panelScroll.setBounds(0,0,700,550);
		panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			
		
		panel.add(panelNorth, BorderLayout.NORTH);
		panel.add(panelScroll, BorderLayout.CENTER);
		frame.add(panel);
	}
	

	public static void main (String[] args) {
		JFrame f = new JFrame();
		f.setTitle("Parking Game");
		f.setSize(700, 700);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE );
		f.setResizable(false);
		
		Game g1 = new Game("Sonia");
		Game g2 = new Game("Lucas");
		Game g3 = new Game("Isa");
		Game g4 = new Game("Raul");
		Game g5 = new Game("Nacho");
		Game g6 = new Game("Javi");
		
		Menu m = new Menu();
		m.addGame(g1);
		m.addGame(g2);
		m.addGame(g3);
		m.addGame(g4);
		m.addGame(g5);
		m.addGame(g6);
		
		GamesMenuView gmv = new GamesMenuView(f, m);
		
	}
	
	private ImageIcon resizeIcon(ImageIcon icon, int i, int j) {
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(i, j, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImg);
	}

}
