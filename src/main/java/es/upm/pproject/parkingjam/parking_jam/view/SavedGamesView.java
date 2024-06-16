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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import es.upm.pproject.parkingjam.parking_jam.controller.controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;

public class SavedGamesView {

	private JFrame frame;
	private controller cont;
	private ArrayList<String> savedGames;

	public SavedGamesView(JFrame f, ArrayList<String> savedGames, controller cont)	{
		this.frame= f;
		this.savedGames = savedGames;
		this.cont= cont;

		initSGV();
		f.setVisible(true);
	}

	public void initSGV() {
		// Altura scroll:
		Integer listH = (savedGames.size()+1)*80 + (savedGames.size())*10;
		Integer pictureH = listH +60;
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
		Font textFont = null;
		try {
			textFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/menuText.ttf")).deriveFont(16f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}

		// Dimensiones:
		Dimension gameButtonSize = new Dimension(420, 80);

		// Colores:
		Color bg = new Color(180,220,110);
		Color gameBColor = new Color(252,231,68); //amarillo

		// Iconos:
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		ImageIcon carIcon = resizeIcon( new ImageIcon(getClass().getResource("/icons/car.png")),40,40);
		ImageIcon loadIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/upload.png")),30,30);

		// Imagenes:
		Image parkingImage= parkingIcon.getImage().getScaledInstance(500, Math.max(pictureH, 450), Image.SCALE_SMOOTH);		

		// Elementos:
		JLabel titleL = new JLabel();
		titleL.setText("Saved Games");
		titleL.setFont(titleFont);

		JLabel textL = new JLabel();
		textL.setText("Choose the game you want to load");
		textL.setFont(textFont);
		
		JLabel noGamesTextL = new JLabel();
		noGamesTextL.setText("There are no saved games");
		noGamesTextL.setFont(textFont);
		noGamesTextL.setForeground(Color.white);
		

		ArrayList<JButton> buttons = new ArrayList<>();
		for(String g : savedGames) {
			JButton b = new JButton(" "+g);
			b.setPreferredSize(gameButtonSize);
			b.setIcon(carIcon);
			b.setBackground(gameBColor);
			b.setFont(gameFont);
			b.setHorizontalAlignment(SwingConstants.LEFT);
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(g+" game button pressed");
					frame.getContentPane().removeAll();
					// llamar controller: añadir game al menu (y abrir game???)
					cont.openSavedGame(g);
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
		row2.add(textL);

		panelNorth.add(row0);
		panelNorth.add(row1);
		panelNorth.add(row2);

		JLayeredPane panelCenter = new JLayeredPane();
		panelCenter.setBackground(bg);
		panelCenter.setPreferredSize(new Dimension(700,Math.max(gamesH, 540)));
		panelCenter.setBounds(0,0,700, Math.max(gamesH, 540));

		JPanel panelBg = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(parkingImage,100, 30, this);
			}
		};
		panelBg.setBackground(bg);
		panelBg.setBounds(0, 0, 700, Math.max(gamesH, 540) ); 

		JPanel panelElem = new JPanel();
		panelElem.setBounds(140, 70, 420, listH);
		panelElem.setBackground(new Color(0,0,0,0));
		panelElem.setLayout(new BoxLayout(panelElem, BoxLayout.Y_AXIS));

		if(buttons.isEmpty()) {
			JPanel row4= new JPanel(new FlowLayout(FlowLayout.CENTER));
			row4.setBackground(new Color(0,0,0,0));
			row4.add(noGamesTextL);
			panelElem.add(row4);
		} else {
			for(JButton b : buttons) {
				JPanel rowX = new JPanel(new FlowLayout(FlowLayout.CENTER));
				rowX.setBackground(new Color(0,0,0,0));
				rowX.add(b);
				panelElem.add(rowX);
			}
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

	private ImageIcon resizeIcon(ImageIcon icon, int i, int j) {
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(i, j, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImg);
	}
	
	
	
	
	
	/*
	public static void main(String[] args) {
		ArrayList<String> sg = new ArrayList<>();
		sg.add("p1");
		sg.add("aaa");
		sg.add("p1");
		sg.add("aaa");
		sg.add("aaa");
		
		JFrame f = new JFrame();
		f.setTitle("Parking Game");
		f.setSize(700, 700);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		f.setResizable(false);
		
		SavedGamesView sgv = new SavedGamesView(f, sg);
	}
	*/
}
