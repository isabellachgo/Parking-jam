package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import javafx.util.Pair;


public class SavedGamesView {

	private JFrame frame;
	private Controller cont;
	private List<String> savedGames;

	public SavedGamesView(JFrame f, List<String> savedGames, Controller cont)	{
		this.frame= f;
		this.savedGames = savedGames;
		this.cont= cont;

		initSGV();
		f.setVisible(true);
	}
	
	// Builds the elements and the structure of the view
	private void initSGV() {
		// Scroll height:
		Integer listH = (savedGames.size()+1)*80 + (savedGames.size())*10;
		Integer pictureH = listH +60;
		Integer gamesH = pictureH +80;
	
		// Dimensions:
		Dimension gameButtonSize = new Dimension(420, 80);
		Dimension buttonSize = new Dimension(40,40);

		// Colores:
		Color bg = new Color(180,220,110);
		Color gameBColor = new Color(252,231,68);
		Color buttonColor = new Color(65,130,4); 

		// Icons:
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		ImageIcon carIcon = Factory.resizeIcon( new ImageIcon(getClass().getResource("/icons/car.png")),40,40);
		ImageIcon backIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/back.png")),30,30);

		// Elements:
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
		goBackB.addActionListener(e-> {	
				frame.getContentPane().removeAll();
				cont.gamesMenuButton();
			
		});
		
		JTextArea textArea = Factory.genTextArea("Do you want to overwrite the Game?");

	    JPanel panelErrorNewg = new JPanel(new BorderLayout());
	    panelErrorNewg.add(textArea, BorderLayout.SOUTH);
		
		ArrayList<JButton> buttons = new ArrayList<>();
		for(String g : savedGames) {
			JButton b = new JButton(" "+g);
			Factory.setFormatButton(b, null, gameButtonSize, carIcon, new Pair<>(null, gameBColor), Factory.buttonFont, SwingConstants.LEFT);
			b.addActionListener(e-> {
					frame.getContentPane().removeAll();
					if(cont.openSavedGame(g) == 1) {
						JDialog dialog = new JOptionPane(panelErrorNewg, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, carIcon).createDialog(frame, "Existing Game");
						dialog.setLocationRelativeTo(frame);
						dialog.setSize(new Dimension(300,150));
						dialog.setVisible(true);
						Object res = ((JOptionPane) dialog.getContentPane().getComponent(0)).getValue();
						if(res instanceof Integer && (Integer)res == JOptionPane.OK_OPTION) {
							frame.getContentPane().removeAll();
							cont.overwrite(g);
						}
					}
			});
			
			buttons.add(b);
		}

		// Structure:
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
		panelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add(panelNorth, BorderLayout.NORTH);
		panel.add(panelScroll, BorderLayout.CENTER);
		frame.add(panel);	
	}

}
