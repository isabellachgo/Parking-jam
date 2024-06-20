package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import javafx.util.Pair;

public class LevelsMenuView {

	private JFrame frame;
	private Game game;
	private int nLevels;
	private Controller cont;

	public LevelsMenuView(JFrame frame, Game game, int nLevels, Controller cont) {
		this.frame = frame;
		this.game = game;
		this.nLevels = nLevels;
		this.cont = cont;

		initLMV();
		this.frame.setVisible(true);
	}

	// Builds the elements and the structure of the view
	private void initLMV() {
		// Number of rows of levels
		int nRows = nLevels/2;
		if(nLevels%2 != 0) nRows++;
		
		// Scroll height:
		Integer listH = (nRows)*80 + (nRows)*30;
		Integer pictureH = listH + 90;
		Integer levelsH = pictureH + 120;

		// Colors:
		Color bg = new Color(180,220,110);
		Color buttonColor = new Color(65,130,4);

		// Dimensions:
		Dimension buttonSize = new Dimension(40,40);
		Dimension levelBSize = new Dimension(80,80);
		Dimension buttonSize2 = new Dimension(195,40);

		// Icons:
		ImageIcon closeMIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/close.png")),30,30);
		ImageIcon saveMIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/save.png")),30,30);
		ImageIcon menuIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/menu.png")),30,30);
		ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking3.png"));
		ImageIcon homeMIcon = Factory.resizeIcon(new ImageIcon(getClass().getResource("/icons/home.png")),30,30);

		// Images:
		Image parkingImage= parkingIcon.getImage().getScaledInstance(500, Math.max(pictureH, 410), Image.SCALE_SMOOTH);

		// Elements:
		JPopupMenu menuPanel = new JPopupMenu();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuPanel.setPopupSize(207,145);
		menuPanel.setBounds(50, 80, 207, 145);

		JButton gamesB= new JButton("games menu");
		Factory.setFormatButton(gamesB, null, buttonSize2, homeMIcon, new Pair<>(Color.white, buttonColor), Factory.menuFont, SwingConstants.LEFT);
		gamesB.addActionListener(e -> {
			frame.getContentPane().removeAll();
			cont.gamesMenuButton();
		});
		JButton saveB = new JButton("save game");
		Factory.setFormatButton(saveB, null, buttonSize2, saveMIcon, new Pair<>(Color.white, buttonColor),  Factory.menuFont, SwingConstants.LEFT);
		saveB.addActionListener(e -> 
		cont.saveGame()
				);
		JButton closeB = new JButton("close Parking Jam");
		Factory.setFormatButton(closeB, null, buttonSize2, closeMIcon, new Pair<>(Color.white, buttonColor),  Factory.menuFont, SwingConstants.LEFT);
		closeB.addActionListener(e -> {
			frame.dispose();
			System.exit(0);
		});

		menuPanel.add(gamesB);
		menuPanel.add(saveB);
		menuPanel.add(closeB);

		JButton menuB = new JButton();
		Factory.setFormatButton(menuB, null, buttonSize, menuIcon, new Pair<>(Color.white, buttonColor), null, null);
		menuB.addActionListener(e -> {
			if(!menuPanel.isVisible()) {
				menuPanel.show(frame, 50, 87);
				menuPanel.setVisible(true);
			} else {
				menuPanel.setVisible(false);
			}
		});


		JLabel gamePointsL = new JLabel();
		gamePointsL.setFont(Factory.gamePointsFont);
		gamePointsL.setText("Game Points: ");

		JLabel gamePointsVL = new JLabel();
		gamePointsVL.setFont(Factory.gamePointsFont);
		gamePointsVL.setText(game.getGamePoints().toString()); 

		JLabel gameNameL = new JLabel();
		gameNameL.setFont(Factory.titleFont2);
		gameNameL.setText(game.getName()); 

		ArrayList<JButton> buttons = new ArrayList<>();
		for(int i=1; i<=nLevels; i++) {
			int n =i;
			JButton b = new JButton();
			Factory.setFormatButton(b, Integer.toString(n), levelBSize, null, new Pair<>(null, null),  Factory.levelFont, null);
			Factory.levelsStatus(b, game);
			b.addActionListener(e ->{
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(n)==1) {
						Factory.corruptLevel(n, frame, game, cont, buttons, true);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
			buttons.add(b);
		}


		// Structure:
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
		panelCenter.setBackground(bg);
		panelCenter.setPreferredSize(new Dimension(700, Math.max(levelsH, 558)));
		panelCenter.setBounds(0,0,700, Math.max(levelsH, 558));

		JPanel panelBg = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(parkingImage,100,75, this);
			}
		};
		panelBg.setBackground(bg);
		panelBg.setBounds(0, 0, 700, Math.max(levelsH, 558)); 

		JPanel panelElem = new JPanel();
		panelElem.setBounds(100, 130, 500, listH);
		panelElem.setBackground(new Color(0,0,0,0));
		panelElem.setLayout(new BoxLayout(panelElem, BoxLayout.Y_AXIS));
				
		int nb=0;
		for(int j=0; j<nRows; j++) {
			JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
			row.setBackground(new Color(0,0,0,0));
			row.add(buttons.get(nb));
			nb++;	
			if(nb<buttons.size()) {
				row.add(Box.createHorizontalStrut(60));
				row.add(buttons.get(nb));
				nb++;
			}
			panelElem.add(row);
		}

		panelCenter.add(panelBg, JLayeredPane.DEFAULT_LAYER);
		panelCenter.add(panelElem, JLayeredPane.PALETTE_LAYER);
		panelCenter.revalidate();
		panelCenter.repaint();
		
		JScrollPane panelScroll = new JScrollPane(panelCenter);
		panelScroll.setBackground(bg);
		panelScroll.setBounds(0,0,700,Math.max(levelsH, 560));
		panelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add(panelNorth, BorderLayout.NORTH);
		panel.add(panelScroll, BorderLayout.CENTER);
		frame.add(panel);

	}


}
