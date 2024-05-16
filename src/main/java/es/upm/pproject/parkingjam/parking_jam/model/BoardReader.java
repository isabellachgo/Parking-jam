package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;

public class BoardReader {
	private FileReader file;
	private BufferedReader reader;
	private String title;
	private Integer dimX;
	private Integer dimY;
	private Pair<Integer,Integer> exit_p;
	private Map<Character, Vehicle> cars;
	private Character[][] board;
	
	String filepathFormat = "src/main/resources/levels/level_%d.txt";
	
	public BoardReader(int nlevel) throws FileNotFoundException, IOException{
		
		String filepath = String.format(filepathFormat, nlevel);
		file = new FileReader(new File(filepath));
		reader = new BufferedReader(file);
		
		title = reader.readLine();
		String dimensions;
		dimensions = reader.readLine();
		if(dimensions != null && dimensions.length() >= 3) {
			dimX = Character.getNumericValue(dimensions.charAt(0));
			dimY = Character.getNumericValue(dimensions.charAt(2));
		}
		
		board = createBoard();
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public Integer getDimensionX() {
		return dimX;
	}
	
	public Integer getDimensionY() {
		return dimY;
	}
	
	public Map<Character, Vehicle> getCars(){
		return cars;
	}
	
	public Pair<Integer,Integer> getExit(){
		return exit_p;
	}
	
	public Character[][] getBoard() {
		return board;
	}
	
	
	
	// Reads the file and returns a character array whit the file board if it represents a valid level, otherwise it returns null.
	// A map is also filled in with the cars on the board
	private Character[][] createBoard(){
		int n_exit=0;
		String line;
		Character c;
		cars = new HashMap<>();
		Character[][] board = new Character[dimX][dimY];
		
		for(int i=0; i<dimY; i++) {
			try {
				line=reader.readLine();
			
				for(int j=0; j<dimX; j++) {
					c = line.charAt(j);
					board[j][i] = c;
					switch(c) {
						case '+':
							break;
							
						case '@':
							exit_p = new Pair<Integer,Integer> (j,i);
							n_exit++;
							break;
						
						case ' ':
							board[j][i] = null;
							break;
							
						default:
							if(!cars.containsKey(c)) {
								Set<Pair<Integer,Integer>> positions = new HashSet<>();
								Pair<Integer, Integer> p = new Pair<>(j,i);
								positions.add(p);
								if(c.equals('*')) {
									cars.put(c, new Vehicle(c, true, null, positions));
								}
								else {
									cars.put(c, new Vehicle(c, false, null, positions));
								}
							} else {
								Vehicle car = cars.get(c);
								Set<Pair<Integer,Integer>> positions = car.getPosition();
								positions.add(new Pair<Integer, Integer>(j,i));
								car.setPosition(positions);
							}
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		// check that there is an exit
		if(n_exit != 1) return null; //error: no tiene salida o tiene más de una
		
		// check that there is a redcar
		if(cars.get('*')==null || cars.get('*').getPosition().size() != 2) return null;
		
		Iterator<Vehicle> it = cars.values().iterator();
		while(it.hasNext()) {
			Vehicle car = (Vehicle) it.next();
			
			// check car size
			int length = car.getPosition().size();
			if(length<2) return null;
			
			// assign size and direction
			Iterator<Pair<Integer,Integer>> itv = car.getPosition().iterator();
			Pair<Integer,Integer> aux1=null;
			Pair<Integer,Integer> aux2=null;
			while(itv.hasNext() && aux2==null) {
				Pair<Integer, Integer> paux = itv.next();
				if(aux1==null) aux1 = paux;
				else aux2 = paux;
			}
			if(aux1==null || aux2==null) return null; 
			else {
				if((aux1.getKey()).equals(aux2.getKey())) car.setDimension(new Pair<Integer, Integer>(1,length));
				else car.setDimension(new Pair<>(length, 1));
			}					
		}
		
		return board;
	}
		
}
