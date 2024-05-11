package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.util.Pair;

public class BoardReader {
	FileReader file;
	BufferedReader reader;
	String title;
	Integer dimX;
	Integer dimY;
	Map<Character, Vehicle> cars;
	
	public BoardReader(String filepath){
		try {
			file = new FileReader(filepath);
			
			reader = new BufferedReader(file);
			
			if((title = reader.readLine()) == null) {
				//tratar error con fichero
			}
			
			String dimensions;
			if((dimensions = reader.readLine()) == null) {
				//tratar error
			}
			dimX = dimensions.charAt(0)-0;
			dimY = dimensions.charAt(2)-0;
		} catch (IOException e) {
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
	
	
	// 0 -> wall
	// 1 -> exit
	// 2 -> red car
	public Integer[][] createBoard(){
		int n_exit=0;
		int n_redcar=0;
		String line;
		Character c;
		int car_id=3;
		cars = new HashMap<>();
		Integer[][] board = new Integer[dimX][dimY];
		
		for(int i=0; i<dimX; i++) {
			try {
				line=reader.readLine();
			
				for(int j=0; j<dimY; j++) {
					c = line.charAt(j);
					switch(c) {
						case '+':
							board[i][j] = 0;
							break;
							
						case '@':
							board[i][j] = 1;
							n_exit++;
							break;
						
						case ' ':
							board[i][j] = null;
							break;
							
						default:
							if(!cars.containsKey(c)) {
								Set<Pair<Integer,Integer>> positions = new TreeSet<>();
								positions.add(new Pair(i,j));
								if(c.equals('*')) {
									cars.put(c, new Vehicle(2, false, null, positions));
									n_redcar++;
								}
								else {
									cars.put(c, new Vehicle(car_id, false, null, positions));
									car_id++;
								}
							} else {
								Vehicle car = cars.get(c);
								Set<Pair<Integer,Integer>> positions = car.getPosition();
								positions.add(new Pair(i,j));
								car.setPosition(positions);
							}
					}
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
					
		}
		
		//comprobar salida
		if(n_exit != 2) {
			//error: no tiene salida o tiene más de una
			return null;
		}
		
		//comprobar redcar
		if(cars.get('*')==null || cars.get('*').getPosition().size() < 2) {
			//error: no hay redcar o no es del tamaño valido 2
			return null;
		}
		
		//poner dimensiones a los coches
		Iterator it = cars.values().iterator();
		while(it.hasNext()) {
			Vehicle car = (Vehicle) it.next();
			
			//comprobar tamaño coches
			int length = car.getPosition().size();
			if(length<2) {
				//error: coche con tamaño inválido <2
				return null;
			}
			
			//asignar dimensiones
			Iterator itv = car.getPosition().iterator();
			Pair<Integer,Integer> aux1=null, aux2=null;
			while(it.hasNext() && aux2==null) {
				if(aux1==null) aux1 = (Pair<Integer, Integer>) itv.next();
				else aux2 = (Pair<Integer, Integer>) itv.next();
			}
			
			if((aux1.getKey()).equals(aux2.getKey())) car.setDimension(new Pair(1,length));
			else car.setDimension(new Pair(length, 1));
			
			//comprobar vecinos
			
			
			
			
			
		}
		
		return board;
	}
}
