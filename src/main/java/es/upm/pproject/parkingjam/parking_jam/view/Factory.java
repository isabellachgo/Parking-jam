package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Factory {
	// Fonts:
	
	
	public static void setFormatButton (JButton b, String t, Dimension size, ImageIcon ic, Color foreg, Color backg, Font font, Integer sc) {
		if(t!=null) b.setText(t);
		if(size!=null) b.setPreferredSize(size);
		if(ic!=null) b.setIcon(ic);
		if(foreg!=null) b.setForeground(foreg);
		if(backg!=null) b.setBackground(backg);
		if(font!=null) b.setFont(font);
		if(sc!=null) b.setHorizontalAlignment(sc);
	}


}
