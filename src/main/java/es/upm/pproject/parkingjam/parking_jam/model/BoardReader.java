package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.File;
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
	private Pair<Integer,Integer> exitp;
	private Map<Character, Vehicle> cars;
	private Character[][] board;

	String filepathFormat = "src/main/resources/levels/level_%d.txt";

	// Constructor with number level as  argument
	public BoardReader(int nlevel) throws  IOException{

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
	// Constructor with filepath as argument
	public BoardReader(String filepath) throws  IOException{
		
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
	// Getters and setters
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
		return exitp;
	}

	public Character[][] getBoard() {
		return board;
	}



	// Reads the file and returns a character array whit the file board if it represents a valid level, otherwise it returns null.
	// A map is also filled in with the cars on the board
	private Character[][] createBoard(){
		int nExit=0;
		String line;
		Character c;
		cars = new HashMap<>();
		Character[][] boardAux = new Character[dimX][dimY];

		for(int i=0; i<dimY; i++) {
			try {
				line=reader.readLine();

				for(int j=0; j<dimX; j++) {
					c = line.charAt(j);
					boardAux[j][i] = c;
					switch(c) {
					case '+':
						break;

					case '@':
						exitp = new Pair<> (j,i);
						nExit++;
						break;

					case ' ':
						boardAux[j][i] = null;
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
							positions.add(new Pair<>(j,i));
							car.setPosition(positions);
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}

		// check that there is an exit in valid position
		if(nExit != 1) return null; //error:there is no exit or there is more than one 
		if((exitp.getKey()==0 && exitp.getValue()==0)
				|| (exitp.getKey()==0 && exitp.getValue()==dimY-1)
				|| (exitp.getKey()==dimX-1 && exitp.getValue()==0)
				|| (exitp.getKey()==dimX-1 && exitp.getValue()==dimY-1)) return null;

		// check that there is a redcar
		if(cars.get('*')==null || cars.get('*').getPosition().size() != 2) return null;

		Iterator<Vehicle> it = cars.values().iterator();
		while(it.hasNext()) {
			Vehicle car = it.next();

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
				if((aux1.getKey()).equals(aux2.getKey())) car.setDimension(new Pair<>(1,length));
				else car.setDimension(new Pair<>(length, 1));
			}					
		}

		return boardAux;
	}

}
