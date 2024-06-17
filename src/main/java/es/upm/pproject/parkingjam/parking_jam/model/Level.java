
package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.util.Pair;

public class Level  {
	private BoardReader bReader;
	private Character[][] board;
	private Integer dimensionX;
	private Integer dimensionY;
	private String title;
	private Pair<Integer,Integer> exit_position;
	private Map<Character,Vehicle> cars;
	private Integer levelPoints;
	private Character[][] initialBoard;
	private Map<Character, Set<Pair<Integer, Integer>>> initialVehiclePositions;
	private Deque<Character[][]> boardHistory;
	private Deque<Map<Character, Set<Pair<Integer, Integer>>>> vehiclePositionHistory;

	public Level (Integer n_level) throws FileNotFoundException, IOException{
		bReader = new BoardReader(n_level);
		dimensionX = bReader.getDimensionX();
		dimensionY = bReader.getDimensionY();
		title = bReader.getTitle();
		board = bReader.getBoard();
		if(board!=null) {
			exit_position = bReader.getExit();
			cars = bReader.getCars();

			this.vehiclePositionHistory = new ArrayDeque<>();

			this.initialBoard= cloneBoard();
			this.boardHistory = new ArrayDeque<>();
			boardHistory.add(initialBoard);
			this.initialVehiclePositions = new HashMap<>();
			for(Entry<Character, Vehicle> vh : cars.entrySet()) {
				initialVehiclePositions.put(vh.getKey(), vh.getValue().getPosition());
			}	
			vehiclePositionHistory.add(cloneCarPositions());
		}
		this.levelPoints= 0;

	}
	public Level (String filepath) throws FileNotFoundException, IOException{
		bReader = new BoardReader(filepath);
		dimensionX = bReader.getDimensionX();
		dimensionY = bReader.getDimensionY();
		title = bReader.getTitle();
		board = bReader.getBoard();
		exit_position = bReader.getExit();
		cars = bReader.getCars();

		this.vehiclePositionHistory = new ArrayDeque<>();

		this.initialBoard= cloneBoard();
		this.boardHistory = new ArrayDeque<>();
		boardHistory.add(initialBoard);
		this.initialVehiclePositions = new HashMap<>();
		for(Entry<Character, Vehicle> vh : cars.entrySet()) {
			initialVehiclePositions.put(vh.getKey(), vh.getValue().getPosition());
		}	
		vehiclePositionHistory.add(cloneCarPositions());

		this.levelPoints= 0;

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
	public void setBoard(Character[][] b){
		this.board=b;
	}
	public Integer getLevelPoint() {
		return levelPoints;
	}
	public void setBoardHistory( Deque<Character[][]> bh) {
		boardHistory =bh;
	}
	public void setVehiclePositionHistory(Deque<Map<Character, Set<Pair<Integer, Integer>>>> ph) {
		vehiclePositionHistory= ph;
	}
	public void setInitialBoard(Character[][] b) {
		initialBoard= b;
	}
	public void setInitialVehiclePositions (Map<Character, Set<Pair<Integer, Integer>>> m) {
		initialVehiclePositions = m;
	}

	public void setLevelPoints(int levelPoints) {
		this.levelPoints = levelPoints;
	}
	public Deque<Character[][]> getBoardHistory() {
		return boardHistory;
	}
	public Deque<Map<Character, Set<Pair<Integer, Integer>>>> getVehiclePositionHistory() {
		return vehiclePositionHistory;
	}

	public boolean move (Vehicle car, char direction, int distance ) {
		boolean moved=true;
		if(distance ==0) return false;

		Character [][] newBoard = updateBoard(car, direction, distance);
		if(newBoard !=null) {
			this.board=newBoard;
			levelPoints++;
			boardHistory.push(this.board);
			vehiclePositionHistory.push(cloneCarPositions());

		}
		else moved=false;

		return moved;
	}


	// Checks if the requested movement is valid and calculates de new position of the vehicle.
	public Character[][] updateBoard(Vehicle car, char direction, int distance) {
		Set<Pair<Integer, Integer>> currentPosition = car.getPosition();
		Set<Pair<Integer, Integer>> newPosition = new HashSet<>();

		for (Pair<Integer, Integer> position : currentPosition) {
			Integer Y = position.getValue();
			Integer X = position.getKey();
			Integer newY = Y;
			Integer newX = X;

			// Calcules the new coordinates depending on the direction and distance
			switch (direction) {
			case 'U': // UP
				newY -= distance;
				break;
			case 'D': // DOWN
				newY += distance;
				break;
			case 'L': // LEFT
				newX -= distance;
				break;
			case 'R': // RIGHT
				newX += distance;
				break;
			default: // UNVALID DIRECTION
				return null;
			}

			// Adds the new position to the set of positions
			newPosition.add(new Pair<Integer,Integer>(newX, newY));
		}
		if(!CheckMovement(car, direction,currentPosition,newPosition) )return null;

		// clones the board
		Character[][] updatedBoard = cloneBoard();

		// deletes the current position of the vehicle actual 
		for (Pair<Integer, Integer> pos : currentPosition) {
			Integer currentX = pos.getKey();
			Integer currentY = pos.getValue();
			updatedBoard[currentX][currentY] = null;
		}

		//Updates the board with the new positions only if the car is not the red one.
		Set<Pair<Integer,Integer>> positionNew= new HashSet<>();
		for (Pair<Integer, Integer> newPos : newPosition) {
			Integer newX = newPos.getKey();
			Integer newY = newPos.getValue();
			updatedBoard[newX][newY] = car.getId();
			Pair<Integer, Integer> p = new Pair<>(newX,newY);
			positionNew.add(p);
		}
		car.setPosition(positionNew);
		return updatedBoard;
	}

	// Checks if the movement is valid
	private boolean CheckMovement(Vehicle car, char direction, Set<Pair<Integer, Integer>> currentPosition, Set<Pair<Integer, Integer>> newPosition) {
		boolean Goal=false;
		for (Pair<Integer, Integer> position : newPosition) {
			Integer newX = position.getKey();
			Integer newY = position.getValue();

			// The new Position cannot be outside the board
			if (newX < 0 || newX >= dimensionX || newY < 0 || newY >= dimensionY) {
				return false;
			}
			if(board[newX][newY] != null && this.getExit().equals(position)) { // the new position is the exit
				if(!car.getRedCar()) {								     // a vehicle that is not the read one cannot be in the exit spot
					return false;
				}
				else Goal=true;
			}

			if (!Goal && board[newX][newY] != null &&  !currentPosition.contains(position) ) {
				return false;
			}


			switch (direction) {
			case 'U': 
				if (car.getDimension().getKey() != 1) { // An horizontal vehicle cannot move up
					return false;
				}
				break;
			case 'D': 
				if (car.getDimension().getKey() != 1) { /// An horizontal vehicle cannot move down
					return false;
				}
				break;
			case 'L': 
				if (car.getDimension().getValue() != 1) { // A vertical vehicle cannot move to the left
					return false;
				}
				break;
			case 'R': 
				if (car.getDimension().getValue() != 1) { // A vertical vehicle cannot move to the right
					return false;
				}
				break;
			default: 
				return false;
			}
		}

		return true;
	}



	public Character[][] cloneBoard() {
		Character[][] cloneBoard= new Character[dimensionX][dimensionY];
		for(int i=0; i<dimensionX; i++) {
			System.arraycopy(board[i], 0, cloneBoard[i], 0, dimensionY);
		}
		return cloneBoard;
	}
	private Map<Character, Set<Pair<Integer, Integer>>> cloneCarPositions() {
		Map<Character, Set<Pair<Integer, Integer>>> clone = new HashMap<>();
		for (Map.Entry<Character, Vehicle> entry : cars.entrySet()) {
			clone.put(entry.getKey(), new HashSet<>(entry.getValue().getPosition()));
		}
		return clone;
	}

	private void restoreCarPositions(Map<Character, Set<Pair<Integer, Integer>>> positions) {
		for (Map.Entry<Character, Set<Pair<Integer, Integer>>> entry : positions.entrySet()) {
			cars.get(entry.getKey()).setPosition(entry.getValue());
		}
	}

	// prints the level title, dimensions and board
	public void printBoard () {
		System.out.println("title: "+ title);
		System.out.println("size: "+ dimensionX+" "+dimensionY);

		for(int i=0; i<dimensionY; i++) {
			for(int j=0; j<dimensionX; j++) {
				if(board[j][i]==null) System.out.print(" ");
				else System.out.print(board[j][i]);
			}
			System.out.println("");
		}
	}
	public boolean posicionValida(Vehicle car, Pair<Integer, Integer> box) {
		boolean result = false;
		Integer boxX= box.getKey();
		Integer boxY = box.getValue();

		if(board[boxX][boxY]== null) result=true;
		else {
			if(car.getRedCar() && board[boxX][boxY]=='@')result =true;
			else {
				Set<Pair<Integer, Integer>> position =car.getPosition();
				Iterator <Pair<Integer, Integer>>it = position.iterator();
				while(it.hasNext() && !result) {
					Pair<Integer, Integer> pos = it.next();
					Integer newX = pos.getKey();
					Integer newY = pos.getValue();
					if(newX.equals(boxX) && newY.equals(boxY)) result=true;
				}	
			}
		}
		return result;
	}
	public void reset() {
		levelPoints=0;
		this.board=initialBoard;
		Map<Character,Vehicle> newCars = new HashMap<>();
		for(Entry<Character, Set<Pair<Integer, Integer>>> vh : initialVehiclePositions.entrySet()) {
			Vehicle car = cars.get(vh.getKey());
			car.setPosition(vh.getValue()); // changes the cars position to its initial position
			newCars.put(vh.getKey(),car);
		}
		this.cars=newCars;
	}
	public Character findChangedVehicle(Map<Character, Set<Pair<Integer, Integer>>> oldState, Map<Character, Set<Pair<Integer, Integer>>> newState) {
		for (Character vehicleId : oldState.keySet()) {
			Set<Pair<Integer, Integer>> oldPosition = oldState.get(vehicleId);
			Set<Pair<Integer, Integer>> newPosition = newState.get(vehicleId);
			if (!oldPosition.equals(newPosition)) {
				return vehicleId;
			}
		}
		return ' '; 
	}
	public Character undo() {
		if(!boardHistory.isEmpty() && !vehiclePositionHistory.isEmpty()) {

			if(vehiclePositionHistory.size()!=1 && boardHistory.size()!=1 ) {
				boardHistory.pop();
				Map<Character, Set<Pair<Integer, Integer>>> vv=vehiclePositionHistory.pop();
				if(!boardHistory.isEmpty() && !vehiclePositionHistory.isEmpty()) {
					this.board = boardHistory.peek();
					restoreCarPositions(vehiclePositionHistory.peek());
					levelPoints--;
					return findChangedVehicle(vv,vehiclePositionHistory.peek()) ;
				}
			}
		}
		return ' ';
	}

}