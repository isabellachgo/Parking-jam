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
	
	public BoardReader(int n_level){
		try {
			String filepath = String.format(filepathFormat, n_level);
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
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
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
	// A map is also filled in with the cars on the board, the red car will always have id 0
	private Character[][] createBoard(){
		int n_exit=0;
		String line;
		Character c;
		int car_id=1;
		cars = new HashMap<>();
		Character[][] board = new Character[dimX][dimY];
		
		for(int i=0; i<dimX; i++) {
			try {
				line=reader.readLine();
			
				for(int j=0; j<dimY; j++) {
					c = line.charAt(j);
					board[i][j] = c;
					switch(c) {
						case '+':
							break;
							
						case '@':
							exit_p = new Pair<Integer,Integer> (i,j);
							n_exit++;
							break;
						
						case ' ':
							board[i][j] = null;
							break;
							
						default:
							if(!cars.containsKey(c)) {
								Set<Pair<Integer,Integer>> positions = new HashSet<>();
								Pair<Integer, Integer> p = new Pair<>(i,j);
								positions.add(p);
								if(c.equals('*')) {
									cars.put(c, new Vehicle(0, false, null, positions));
								}
								else {
									cars.put(c, new Vehicle(car_id, false, null, positions));
									car_id++;
								}
							} else {
								Vehicle car = cars.get(c);
								Set<Pair<Integer,Integer>> positions = car.getPosition();
								positions.add(new Pair<Integer, Integer>(i,j));
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
		
		Iterator it = cars.values().iterator();
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
			
			// check neightbours 
			if(aux_neightbour(car)<0) return null; 
					
		}
		
		return board;
	}
	
	
	private int aux_neightbour(Vehicle car){
		//TODO: Comprobar vecinos
		int n_neightbours=0;
		
		for(Pair<Integer,Integer> p :car.getPosition()) {
			
		}
		
		return 0;
	}
	
}
