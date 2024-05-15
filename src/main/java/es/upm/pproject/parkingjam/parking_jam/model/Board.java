package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.InputStream;
import java.util.Map;

import javafx.util.Pair;

public class Board {
	
	private BoardReader bReader;
	private Character[][] board;
	private Integer dimensionX;
	private Integer dimensionY;
	private String title;
	private Pair<Integer,Integer> exit_position;
	private Map<Character,Vehicle> cars;
	
	String filepathFormat = "src/main/resources/levels/level_%d.txt";
	
	public Board(int n_level) {
		
		String filepath = String.format(filepathFormat, n_level);
		bReader = new BoardReader(filepath);
		
		dimensionX = bReader.getDimensionX();
		dimensionY = bReader.getDimensionY();
		title = bReader.getTitle();
		board = bReader.getBoard();
		exit_position = bReader.getExit();
		cars = bReader.getCars();
	}
	
	public String getTitle() {
		return title;
	}
	
	public Integer getDimensionX() {
		return dimensionX;
	}
	
	public Integer getDimensionY() {
		return dimensionY;
	}
	
	public Map<Character, Vehicle> getCars(){
		return cars;
	}
	
	public Pair<Integer,Integer> getExit(){
		return exit_position;
	}
	
	public Character[][] getBoard(){
		return board;
	}
	
	// prints the level title, dimensions and board
	public void printBoard () {
		System.out.println("title: "+ title);
		System.out.println("size: "+ dimensionX+" "+dimensionY);
		
		for(int i=0; i<dimensionX; i++) {
			for(int j=0; j<dimensionY; j++) {
				if(board[i][j]==null) System.out.print(" ");
				else System.out.print(board[i][j]);
			}
			System.out.println("");
		}
	}

	
	/* 
	public static void main(String args[]) {
		Board b1 = new Board(1);
		if(b1.board==null) System.out.println("error al construir tablero");
		else b1.printBoard();
	}
	*/
	
}