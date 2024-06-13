package es.upm.pproject.parkingjam.parking_jam.view;

import javax.swing.JFrame;

import es.upm.pproject.parkingjam.parking_jam.controller.controller;

public class SavedGamesView {
	
	private JFrame frame;
	private controller cont;
	
	public SavedGamesView(JFrame f, controller cont)	{
		this.frame= f;
		this.cont= cont;
		
		initSGV();
		f.setVisible(true);
	}
	
	public void initSGV() {
		
	}
}
