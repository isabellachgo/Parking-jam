package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.model.*;
import javafx.util.Pair;

public class AppTest {

   @Nested
   class VehicleTest{
	   Vehicle vehicle;
	   Character id;
	   boolean redCar;
	   Pair<Integer,Integer> dimension;
	   Set<Pair<Integer,Integer>> position;
	   @BeforeEach
	   public void createCar() {
			id= 'a';
			redCar=false;
			dimension= new Pair<>(2,3);
			position = new HashSet<>();
			position.add(new Pair<>(1,2));
			position.add(new Pair<>(2,3));
			vehicle = new Vehicle(id, redCar, dimension,position);
		}
	   @Test
	   void testGetId() {
		   assertEquals(id, vehicle.getId());
	   }
	   @Test
	   void testGetRedCar() {
		   assertFalse(vehicle.getRedCar());
	   }
	   @Test
	   void testGetDomension() {
		   assertEquals(dimension, vehicle.getDimension());
	   }
	   @Test
	   void testGetPosition() {
		   assertEquals(position, vehicle.getPosition());
	   }
	   @Test
	   void testSetPosition() {
		  Set<Pair<Integer, Integer>> newPosition= new HashSet<>();
		  newPosition.add(new Pair<>(3,4));
		  newPosition.add(new Pair<>(4,4));
		  vehicle.setPosition(newPosition);
		  assertEquals(newPosition, vehicle.getPosition());
	   }

   }



	@Nested
	class BoardReaderTest {
		@Test
		public void notFile() {
			assertThrows(FileNotFoundException.class, () -> {BoardReader br = new BoardReader(0);});
		}

		@Nested
		class wrongFormat_BoardReaderTest {
			@Test
			public void badFormat_noExit() throws FileNotFoundException, IOException {
				BoardReader br = new BoardReader(102);
				assertNull(br.getBoard());
			}
			
			@Test
			public void badFormat_wrongExit() throws FileNotFoundException, IOException {
				BoardReader br = new BoardReader(101);
				assertNull(br.getBoard());
			}

			@Test
			public void badFormat_noRedcar() throws FileNotFoundException, IOException {
				BoardReader br = new BoardReader(103);
				assertNull(br.getBoard());
			}

			@Test
			public void badFormat_badSize() throws FileNotFoundException, IOException {
				BoardReader br = new BoardReader(104);
				assertNull(br.getBoard());
			}
		}

		@Nested
		class correctFormat_BoardReaderTest{
			BoardReader br;
			@BeforeEach
			public void createsReader() throws FileNotFoundException, IOException {
				br = new BoardReader(1);
			}

			@Test
			public void validFile() {
				assertNotNull(br.getBoard());
			}

			@Test
			public void title() {
				assertEquals(br.getTitle(), "Initial level");
			}

			@Test 
			public void dimensions() {
				assertEquals(br.getDimensionX(), 8);
				assertEquals(br.getDimensionY(), 8);
			}

			@Test
			public void exit() {
				assertEquals(br.getExit(), new Pair<Integer,Integer>(4,7));
			}

			@Test
			public void cars() {
				Character[] ccars = new Character[8];
				ccars[0] = 'a';
				ccars[1] = 'b';
				ccars[2] = 'c';
				ccars[3] = '*';
				ccars[4] = 'd';
				ccars[5] = 'f';
				ccars[6] = 'e';
				ccars[7] = 'g';

				Map<Character,Vehicle> cars = br.getCars();
				assertNotNull(cars);
				assertEquals(8, cars.size());
				for(int i=0; i<8; i++) {
					assertTrue(cars.containsKey(ccars[i]));
					assertEquals(cars.get(ccars[i]).getId(), ccars[i]);
				}
			}

			@Test
			public void board() {
				Character[][] boardt = new Character[8][8];
				boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
				boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
				boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
				boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]='c';  boardt[7][3]='+';
				boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
				boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
				boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
				boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

				Character[][] board = br.getBoard();
				for(int i =0; i<8; i++) {
					for(int j=0; j<8;j++) {
						assertEquals(board[i][j], boardt[i][j]);
					}
				}
			}
		}
	}
	@Nested
	class LevelTest{

		private Level level;

		@BeforeEach
		public void Ini() throws FileNotFoundException,IOException {
			level = new Level(1);
		}

		@Test
		public void testGetTitle() {
			assertEquals("Initial level", level.getTitle());
		}
		@Test
		public void testGetDimensions() {
			assertEquals(8, level.getDimensionX());
			assertEquals(8, level.getDimensionY());

		}
		@Test
		public void exit() {
			assertEquals(level.getExit(), new Pair<Integer,Integer>(4,7));
		}

		@Test
		public void Testcars() {
			Character[] ccars = new Character[8];
			ccars[0] = 'a';
			ccars[1] = 'b';
			ccars[2] = 'c';
			ccars[3] = '*';
			ccars[4] = 'd';
			ccars[5] = 'f';
			ccars[6] = 'e';
			ccars[7] = 'g';

			Map<Character,Vehicle> cars = level.getCars();
			assertNotNull(cars);
			assertEquals(8, cars.size());
			for(int i=0; i<8; i++) {
				assertTrue(cars.containsKey(ccars[i]));
				assertEquals(cars.get(ccars[i]).getId(), ccars[i]);
			}
		}
		@Test
		public void board() {
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]='c';  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}
		}

		@Test 
		public void testMove(){
			Vehicle car = level.getCars().get('e');
			boolean moved =level.move(car, 'U', 3);
			assertTrue(moved);
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]='e'; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]='e'; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]='c';  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]=null;  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]=null;  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';
			assertEquals(level.getDimensionX(), 8);
			assertEquals(level.getDimensionY(), 8);
			
			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}


		}
		
	
		@Test
		public void MoveTowin() {
			assertTrue(level.move(level.getCars().get('e'), 'U', 3));
			assertTrue(level.move(level.getCars().get('g'), 'L', 3));
			assertTrue(level.move(level.getCars().get('c'), 'D', 3));
			assertTrue(level.move(level.getCars().get('b'), 'R', 1));
			assertTrue(level.move(level.getCars().get('a'), 'R', 1));
			assertTrue(level.move(level.getCars().get('d'), 'U', 2));
			assertTrue(level.move(level.getCars().get('f'), 'L', 2));
			assertTrue(level.move(level.getCars().get('*'), 'D', 4));
			
		}
		@Test
		public void LevlPoints() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			assertEquals(level.getLevelPoint(), 8);
			
		}
		@Test
		public void WinGame() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='d';	 boardt[2][1]='a';  boardt[3][1]='a';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='b';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]='d'; boardt[2][2]='e'; boardt[3][2]=null; boardt[4][2]=null;  boardt[5][2]=null; boardt[6][2]=null;  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]='e'; boardt[3][3]=null; boardt[4][3]=null;  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='f';  boardt[2][4]='f'; boardt[3][4]='f';  boardt[4][4]=null;  boardt[5][4]=null;  boardt[6][4]='c'; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]=null;  boardt[2][5]=null;  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]='c'; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]='g'; boardt[2][6]='g';  boardt[3][6]='g'; boardt[4][6]=null;  boardt[5][6]=null;  boardt[6][6]='c';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}
			
		}
		
		public void testInvalidMove2() {
			Vehicle car2 = level.getCars().get('g');
			boolean moved2= level.move(car2,'R',1 ); // A CAR CANNOT MOVE TOWARS A WALL
			assertFalse(moved2);

			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]='c';  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}

		}
		@Test
		public void testInvalidMove3() {
			
			Vehicle car3 = level.getCars().get('*');
			boolean moved3= level.move(car3,'D',1 ); // A CAR CANNOT MOVE IF THERES ANOHER CAR IN THAT POSITION
			assertFalse(moved3);

			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]='c';  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}

		}
		@Test
		public void testInvalidMove4() {
			
			Vehicle car4 = level.getCars().get('g');
			boolean moved4= level.move(car4,'D',19); // A CAR CANNOT MOVE outside of the board
			assertFalse(moved4);

			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]='c';  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}

		}
		

	}

}
