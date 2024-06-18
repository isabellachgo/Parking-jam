package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import javafx.util.Pair;


public class SavedGamesView {

	private JFrame frame;
	private Controller cont;
	private ArrayList<String> savedGames;

	public SavedGamesView(JFrame f, ArrayList<String> savedGames, Controller cont)	{
		this.frame= f;
		this.savedGames = savedGames;
		this.cont= cont;

		initSGV();
		f.setVisible(true);
	}

	private void initSGV() {
		// Altura scroll:
		Integer listH = (savedGames.size()+1)*80 + (savedGames.size())*10;
		Integer pictureH = listH +60;
		Integer gamesH = pictureH +80;
		Integer scrollH = gamesH + 40;

		// Dimensiones:
		Dimension gameButtonSize = new Dimension(420, 80);
		Dimension buttonSize = new Dimension(40,40);

		// Colores:
		Color bg = new Color(180,220,110);
		Color gameBColor = new Color(252,231,68); //amarillo
		Color buttonColor = new Color(65,130,4); 

		// Iconos:
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		ImageIcon carIcon = Factory.resizeIcon( new ImageIcon(getClass().getResource("/icons/car.png")),40,40);
		ImageIcon loadIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/upload.png")),30,30);
		ImageIcon backIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/back.png")),30,30);

		// Elementos:
		JLabel titleL = new JLabel();
		titleL.setText("Saved Games");
		titleL.setFont(Factory.titleFont);

		JLabel textL = new JLabel();
		textL.setText("Choose the game you want to load");
		textL.setFont(Factory.menuFont);
		
		JLabel noGamesTextL = new JLabel();
		noGamesTextL.setText("There are no saved games");
		noGamesTextL.setFont(Factory.menuFont);
		noGamesTextL.setForeground(Color.white);
		
		JButton goBackB = new JButton(); 
		Factory.setFormatButton(goBackB, null, buttonSize, backIcon, new Pair<>(null, buttonColor), null, null);
		goBackB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				cont.gamesMenuButton();
			}
		});
		
		JTextArea textArea = Factory.genTextArea("There is alredy a game with the same name, it is not possible to load a game with the same name as another existing one.");

	    JPanel panelErrorNewg = new JPanel(new BorderLayout());
	    panelErrorNewg.add(textArea, BorderLayout.CENTER);
		
		ArrayList<JButton> buttons = new ArrayList<>();
		for(String g : savedGames) {
			JButton b = new JButton(" "+g);
			Factory.setFormatButton(b, null, gameButtonSize, carIcon, new Pair<>(null, gameBColor), Factory.buttonFont, SwingConstants.LEFT);
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.getContentPane().removeAll();
					if(cont.openSavedGame(g) == 1) {
						JDialog existingGame = new JDialog(frame, "Existing Game", true);
						existingGame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						existingGame.setSize(new Dimension(300,150));
					    existingGame.add(panelErrorNewg);	
						existingGame.setLocationRelativeTo(frame);
						existingGame.setVisible(true);
					}
				}
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
		row0.add(Box.createHorizontalStrut(30));
		row0.add(goBackB);
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

		JPanel panelBg = Factory.genPanelBg(bg, gamesH, 540, pictureH, 450, parkingIcon);

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

}
