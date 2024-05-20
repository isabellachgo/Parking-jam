
package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
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
	private Integer idLevel;
	private Integer levelPoints;
	private Integer gamePoints;


// NOTA: he hecho que le vehiculo rojo sea el unico que puede salir, y ocupar la casilla de salida, y una vez alli no imprimirlo, 
	public Level (Integer n_level) throws FileNotFoundException, IOException{
		bReader = new BoardReader(n_level);
		dimensionX = bReader.getDimensionX();
		dimensionY = bReader.getDimensionY();
		title = bReader.getTitle();
		board = bReader.getBoard();
		exit_position = bReader.getExit();
		cars = bReader.getCars();
		this.levelPoints= 0;
		if (n_level ==1) this.gamePoints= 0; // if n_level is 1, gamePoints is initialized to 0.
		else this.gamePoints=0; // if is another level getGamePoints ??
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
	public Integer getLevelPoint() {
		return levelPoints;
	}


	public Integer getGamePoints() {
		return gamePoints + levelPoints;
	}

	public void setGamePoints(int gamePoints) {
		this.gamePoints = gamePoints;
	}

	// prints the level title, dimensions and board

	public boolean move (Vehicle car, char direction, int distance ) {
		boolean moved=true;
		if(distance ==0) return false;
		Character [][] newBoard = updateBoard(car, direction, distance);
		if(newBoard !=null) {
			this.board=newBoard;
			levelPoints++;
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
		for (Pair<Integer, Integer> newPos : newPosition) {
			if(!car.getRedCar()) {
				Integer newX = newPos.getKey();
				Integer newY = newPos.getValue();
				updatedBoard[newX][newY] = car.getId();
			}
		}
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
			if(board[newX][newY] != null && this.getExit().equals(position)/* board[newX][newY] == '@'*/ ) { // the new position is the exit
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

	
	public void reset() {
		// TODO Auto-generated method stub

	}
	public static void main(String args[]) throws FileNotFoundException, IOException {
		Level b1 = new Level(1);
		if (b1.board == null) {
			System.out.println("Error al construir el tablero");
		} else {
			b1.printBoard();
			// nota: no se escogio la ruta mas optima para probar el movimiento de coches.
			Vehicle vehicle = b1.getCars().get('e'); // 
			if (b1.move(vehicle, 'U', 3)) {
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			Vehicle vehicle2 = b1.getCars().get('g');
			if (b1.move(vehicle2, 'L', 3)) {
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			Vehicle vehicle3 = b1.getCars().get('c');
			if (b1.move(vehicle3, 'D', 3)) {
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			Vehicle vehicle4 = b1.getCars().get('b');
			if (b1.move(vehicle4, 'R', 1)) {
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			Vehicle vehicle5 = b1.getCars().get('a');
			if (b1.move(vehicle5, 'R', 1)) {
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			Vehicle vehicle6 = b1.getCars().get('d');
			if (b1.move(vehicle6, 'U', 2)) {
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			Vehicle vehicle7 = b1.getCars().get('f');
			if (b1.move( vehicle7, 'L', 2)) {
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			// DINAMICA GANAR
			Vehicle vehicle8 = b1.getCars().get('*');
			if (b1.move(vehicle8, 'D', 4)) { // como maximo puedo moverlo hasta la entrada (ocupando su casilla) , y una vez alli no lo imprimo por ser coche rojo
				System.out.println("Movimiento exitoso. Tablero después del movimiento:");
				b1.printBoard(); // Imprimir el tablero después del movimiento
			} else {
				System.out.println("Movimiento no válido. No se pudo mover el vehículo.");
			}
			System.out.println("Se han conseguido "+b1.getLevelPoint()+ " puntos en este nivel");

		}
	}





}