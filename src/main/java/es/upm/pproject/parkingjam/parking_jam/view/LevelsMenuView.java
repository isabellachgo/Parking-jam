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

import javax.swing.SwingConstants;


import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import javafx.util.Pair;

public class LevelsMenuView {

	private JFrame frame;
	private Game game;
	private Controller cont;

	public LevelsMenuView(JFrame frame, Game game, Controller cont) {
		this.frame = frame;
		this.game = game;
		this.cont = cont;
				
		initLMV();
		this.frame.setVisible(true);
	}

	// Builds the elements and the structure of the view
	private void initLMV() {
		JButton l1B;
		JButton l2B;
		JButton l3B;
		JButton l4B;

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
		Image parkingImage= parkingIcon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);

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
		l1B = new JButton();
		Factory.setFormatButton(l1B, "1", levelBSize, null, new Pair<>(null, null),  Factory.levelFont, null);
		Factory.levelsStatus(l1B, game);
		l1B.addActionListener(e ->{
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(1)==1) {
						Factory.corruptLevel(1, frame, game, cont, buttons, true);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		});
		l2B = new JButton();
		Factory.setFormatButton(l2B, "2", levelBSize, null, new Pair<>(null, null),  Factory.levelFont, null);
		Factory.levelsStatus(l2B, game);
		l2B.addActionListener(e ->{
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(2)==1) {
						Factory.corruptLevel(2, frame, game, cont, buttons, true);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		});
		l3B = new JButton();
		Factory.setFormatButton(l3B, "3", levelBSize, null, new Pair<>(null, null),  Factory.levelFont, null);
		Factory.levelsStatus(l3B, game);
		l3B.addActionListener(e ->{
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(3)==1) {
						Factory.corruptLevel(3, frame, game, cont, buttons, true);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		});
		l4B = new JButton();
		Factory.setFormatButton(l4B, "4", levelBSize, null, new Pair<>(null, null),  Factory.levelFont, null);
		Factory.levelsStatus(l4B, game);
		l4B.addActionListener(e ->{
				frame.getContentPane().removeAll();
				try {
					if(cont.showLevel(4)==1) {
						Factory.corruptLevel(4, frame, game, cont, buttons, true);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		});
		buttons.add(l1B);
		buttons.add(l2B);
		buttons.add(l3B);
		buttons.add(l4B);

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

	
}
