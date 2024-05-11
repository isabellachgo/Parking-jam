package es.upm.pproject.parkingjam.parking_jam.model;

public class Board {
	
	BoardReader bReader;
	
	Integer[][] board;
	Integer dimensionX;
	Integer dimensionY;
	String title;
	
	public Board(String filepath) {
		bReader = new BoardReader(filepath);
		
		dimensionX = bReader.getDimensionX();
		dimensionY = bReader.getDimensionY();
		title = bReader.getTitle();
		board = bReader.createBoard();
		
	}
	
	
}