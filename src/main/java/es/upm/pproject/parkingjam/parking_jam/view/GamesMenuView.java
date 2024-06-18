package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;
import javafx.util.Pair;

public class GamesMenuView {

	private JFrame frame ;
	private Menu menu;
	private Controller cont;

	public GamesMenuView(JFrame frame, Menu menu, Controller cont) {
		this.frame = frame;
		this.menu = menu;
		this.cont = cont;
		initGMV();
		this.frame.setVisible(true);
	}

	private void initGMV() {
		
		// Altura scroll:
		Integer listH = (menu.getNumGames()+1)*80 + (menu.getNumGames())*10;
		Integer pictureH = listH +80;
		Integer gamesH = pictureH +80;
		Integer scrollH = gamesH + 40;

		// Dimensiones:
		Dimension gameButtonSize = new Dimension(420, 80);
		Dimension buttonSize2 = new Dimension(180,40);

		// Colores:
		Color bg = new Color(180,220,110);
		Color buttonColor = new Color(65,130,4); //verde oscuro
		Color gameBColor = new Color(252,231,68); //amarillo
		
		// Iconos:
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		ImageIcon addIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/add_black.png")),40,40);
		ImageIcon carIcon = Factory.resizeIcon( new ImageIcon(getClass().getResource("/icons/car.png")),40,40);
		ImageIcon loadIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/upload.png")),30,30);
				
		// Elementos:
		JButton loadGameB = new JButton("Load game");
		Factory.setFormatButton(loadGameB, null, buttonSize2, loadIcon, new Pair<>(Color.white, buttonColor), Factory.menuFont, SwingConstants.CENTER);
		loadGameB.addActionListener(e -> {
				frame.getContentPane().removeAll();
				cont.openSavedGames();
		});

		JLabel titleL = new JLabel();
		titleL.setText("Parking Jam");
		titleL.setFont(Factory.titleFont);
		
		JPanel panelGameName = new JPanel();
		panelGameName.setLayout(new BorderLayout());
		
		JLabel pgnText = new JLabel();
		pgnText.setFont(Factory.newGameFont);
		pgnText.setOpaque(true);
		pgnText.setText("Game name: ");
		
		JTextField pgnInput = new JTextField();
		pgnInput.setBackground(Color.white);
		
		panelGameName.add(pgnText, BorderLayout.NORTH);
		panelGameName.add(pgnInput, BorderLayout.CENTER);
		
		JTextArea textArea = Factory.genTextArea("There is alredy a game with the same name, please change the name of the new game.");

	    JPanel panelErrorNewg = new JPanel(new BorderLayout());
	    panelErrorNewg.add(textArea, BorderLayout.CENTER);
		
		JButton addGameB = new JButton("New game");
		Factory.setFormatButton(addGameB, null, gameButtonSize, addIcon, new Pair<>(null, gameBColor), Factory.buttonFont, SwingConstants.LEFT);
		addGameB.addActionListener(e -> {				
				JDialog dialog = new JOptionPane(panelGameName, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, carIcon).createDialog(frame, "New Game");
				dialog.setLocationRelativeTo(frame);
				dialog.setVisible(true);
				Object res = ((JOptionPane) dialog.getContentPane().getComponent(0)).getValue();
				
				if(res instanceof Integer && (Integer)res == JOptionPane.OK_OPTION) {
					String input = pgnInput.getText();
					if(input != null && !input.trim().isEmpty()) {
						frame.getContentPane().removeAll();
						if(cont.newGame(input) == 1) {
							JDialog existingGame = new JDialog(frame, "Existing Game", true);
							existingGame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							existingGame.setSize(new Dimension(300,150));
						    existingGame.add(panelErrorNewg);	
							existingGame.setLocationRelativeTo(frame);
							existingGame.setVisible(true);
						}
					}
				}
		});
		
		ArrayList<JButton> buttons = new ArrayList<>();
		List<Game> games = menu.getGames();
		for(Game g : games) {
			JButton b = new JButton(" "+g.getName());
			Factory.setFormatButton(b, null, gameButtonSize, carIcon, new Pair<>(null, gameBColor), Factory.buttonFont, SwingConstants.LEFT);
			b.addActionListener(e ->{
					frame.getContentPane().removeAll();
					cont.openGame(g);
			});
			
			buttons.add(b);
		}
				
		// Estructura:
		JPanel panel = Factory.genPanel(bg, new BorderLayout());
		
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
		
		JPanel panelBg = Factory.genPanelBg(bg, gamesH, 500, pictureH, 430, parkingIcon);
		
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
		panelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			
		
		panel.add(panelNorth, BorderLayout.NORTH);
		panel.add(panelScroll, BorderLayout.CENTER);
		frame.add(panel);
	}


}
