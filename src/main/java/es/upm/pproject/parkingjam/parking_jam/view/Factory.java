package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import es.upm.pproject.parkingjam.parking_jam.controller.controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;

public class Factory {
	// Colors:
	static Color levelBColor = new Color(39,193,245);
	static Color lockedLevelBColor = new Color(80,155,180);
	
	// Fonts:
	static Font menuFont= genFont("src/main/resources/fonts/menuText.ttf", 16f);
	static Font titleFont = genFont("src/main/resources/fonts/titlefont.ttf", 45f);
    static Font buttonFont = genFont("src/main/resources/fonts/menuText.ttf", 30f);
    static Font titleFont2 = genFont("src/main/resources/fonts/titlefont.ttf", 35f);
    static Font gamePointsFont = genFont("src/main/resources/fonts/pointsfont.ttf", 23f);
    static Font levelFont = genFont("src/main/resources/fonts/titlefont.ttf", 27f);
    static Font levelPointsFont =genFont("src/main/resources/fonts/pointsfont.ttf", 50f);
    static Font infoFont = genFont("src/main/resources/fonts/pointsfont.ttf", 27f);
	static Font levelPointsFont2 = genFont("src/main/resources/fonts/pointsfont.ttf", 27f);
	static Font newGameFont = genFont("src/main/resources/fonts/menuText.ttf", 23f);
	
	private static Font genFont(String path, float size) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		return font;
	}
	

	public static void setFormatButton (JButton b, String t, Dimension size, ImageIcon ic, Color foreg, Color backg, Font font, Integer sc) {
		if(t!=null) b.setText(t);
		if(size!=null) b.setPreferredSize(size);
		if(ic!=null) b.setIcon(ic);
		if(foreg!=null) b.setForeground(foreg);
		if(backg!=null) b.setBackground(backg);
		if(font!=null) b.setFont(font);
		if(sc!=null) b.setHorizontalAlignment(sc);
	}

	public static ImageIcon resizeIcon(ImageIcon icon, int i, int j) {
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(i, j, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImg);
	}

	public static ImageIcon rotateIcon(ImageIcon icon, double angle) {
		Image img = icon.getImage();
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(angle), img.getWidth(null) / 2.0, img.getHeight(null) / 2.0);
		g2d.setTransform(transform);
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();

		return new ImageIcon(bufferedImage);
	}

	public static void corruptLevel(Integer n, JFrame frame, Game game, controller cont, ArrayList<JButton> buttons, Boolean checkStatus) {
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

		if(checkStatus && game!=null) {
			game.setOkLevel(n, false);
			game.setUltimoLevelPassed(n);
			for(JButton b : buttons) {
				levelsStatus(b, game);
			}
		}

		if( res instanceof Integer && (Integer)res == JOptionPane.OK_OPTION) {			
			frame.getContentPane().removeAll();
			try {
				if(n==4) cont.endGame();
				else if(cont.showLevel(n+1) == 1) corruptLevel(n+1, frame, game, cont, buttons, checkStatus);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	
	public static void levelsStatus(JButton b, Game game) {
		int last = game.getUltimoLevelPassed() + 1;
		if(Integer.parseInt(b.getText()) <= last 
				&& game.getOkLevel(Integer.parseInt(b.getText())) ) {
			b.setBackground(levelBColor);
			b.setEnabled(true);
		} else {
			b.setBackground(lockedLevelBColor);
			b.setEnabled(false);
		}
	}
	
	public static JTextArea genTextArea(String text) {
		JTextArea ta = new JTextArea(text);
	    ta.setWrapStyleWord(true);
	    ta.setLineWrap(true);
	    ta.setOpaque(false);
	    ta.setEditable(false);
	    ta.setFocusable(false);
	    ta.setForeground(Color.red);
	    ta.setBackground(UIManager.getColor("Label.background"));
	    ta.setFont(Factory.menuFont);
	    ta.setMargin(new Insets(10, 10, 10, 10));
	    
	    return ta;
	}
}
