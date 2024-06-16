package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
				boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
		public void InValidBox() {	
			Vehicle  car1= level.getCars().get('a');
			Pair<Integer,Integer> pos = new Pair<>(4,4);
			assertFalse(level.posicionValida(car1, pos));
		}
		@Test
		public void InValidBox2() {	
			Vehicle  car1= level.getCars().get('c');
			Pair<Integer,Integer> pos = new Pair<>(6,0);
			assertFalse(level.posicionValida(car1, pos));
		}
		@Test
		public void ValidBox() {
			Vehicle  car1= level.getCars().get('e');
			Pair<Integer,Integer> pos = new Pair<>(2,5);
			assertTrue(level.posicionValida(car1, pos));
		}
		@Test
		public void ValidBox2() {
			Vehicle  car1= level.getCars().get('f');
			Pair<Integer,Integer> pos = new Pair<>(6,4);
			assertTrue(level.posicionValida(car1, pos));
		}
		@Test
		public void ValidBox3() {
			Vehicle  car1= level.getCars().get('e');
			Pair<Integer,Integer> pos = new Pair<>(2,2);
			assertTrue(level.posicionValida(car1, pos));
		}
		@Test
		public void ValidBoxForRedcAR() {
			Vehicle  car1= level.getCars().get('*');
			Pair<Integer,Integer> pos = new Pair<>(4,7);
			assertTrue(level.posicionValida(car1, pos));
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
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]='e'; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
		public void BoardHistory() {
			Deque<Character[][]> boardHistory = new ArrayDeque<>();

			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';
			boardHistory.add(boardt);

			Character[][]  ob= level.getBoardHistory().peek();

			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(ob[i][j], boardt[i][j]);
				}
			}

		}
		@Test
		public void BoardHistoryWithMov() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('f'), 'L', 1);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);

			Deque<Character[][]> boardHistory = new ArrayDeque<>();
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]=null;  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]='e'; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]=null;  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]='e'; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]='f'; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]=null;  boardt[6][4]='c'; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]=null;  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]='c'; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]='g'; boardt[2][6]='g';  boardt[3][6]='g'; boardt[4][6]=null;  boardt[5][6]=null;  boardt[6][6]=null;  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';
			boardHistory.add(boardt);

			Character[][]  ob= level.getBoardHistory().peek();

			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals( boardt[i][j], ob[i][j]);
				}
			}

		}
		@Test
		public void BoardHistoryWithUndo() {


			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('f'), 'L', 1);
			level.undo();
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.undo();
			Deque<Character[][]> boardHistory = new ArrayDeque<>();
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]='e'; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]='e'; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]=null;  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]='g'; boardt[2][6]='g';  boardt[3][6]='g'; boardt[4][6]=null;  boardt[5][6]=null;  boardt[6][6]=null;  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';
			boardHistory.add(boardt);

			Character[][]  ob= level.getBoardHistory().peek();

			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals( boardt[i][j], ob[i][j]);
				}
			}
		}

		@Test
		public void BoardHistoryPostReset() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			level.reset();
			
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';
			
			Character[][]  ob= level.getBoard();

			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals( boardt[i][j], ob[i][j]);
				}
			}


		}

		@Test
		public void PositionHistory() {

			Deque<Map<Character, Set<Pair<Integer, Integer>>>> vehiclePositionHistory = new ArrayDeque<>();
			Map<Character, Set<Pair<Integer, Integer>>> m1= new HashMap<Character, Set<Pair<Integer, Integer>>>();
			Set<Pair<Integer, Integer>>  h1 =new HashSet<>();
			h1.add(new Pair<>(1,1));
			h1.add(new Pair<>(2,1));
			m1.put('a',h1);

			Set<Pair<Integer, Integer>>  h2 =new HashSet<>();
			h2.add(new Pair<>(3,1));
			h2.add(new Pair<>(4,1));	
			h2.add(new Pair<>(5,1));
			m1.put('b',h2);

			Set<Pair<Integer, Integer>>  h3 =new HashSet<>();
			h3.add(new Pair<>(6,1));
			h3.add(new Pair<>(6,2));	
			m1.put('c',h3);

			Set<Pair<Integer, Integer>>  h4 =new HashSet<>();
			h4.add(new Pair<>(1,3));
			h4.add(new Pair<>(1,4));	
			h4.add(new Pair<>(1,5));
			m1.put('d',h4);

			Set<Pair<Integer, Integer>>  h5 =new HashSet<>();
			h5.add(new Pair<>(2,5));
			h5.add(new Pair<>(2,6));	
			m1.put('e',h5);

			Set<Pair<Integer, Integer>>  h6 =new HashSet<>();
			h6.add(new Pair<>(3,4));
			h6.add(new Pair<>(4,4));	
			h6.add(new Pair<>(5,4));
			m1.put('f',h6);

			Set<Pair<Integer, Integer>>  h7 =new HashSet<>();
			h7.add(new Pair<>(4,6));
			h7.add(new Pair<>(5,6));	
			h7.add(new Pair<>(6,6));
			m1.put('g',h7);

			vehiclePositionHistory.add(m1);

			assertEquals(m1.get('a'), level.getVehiclePositionHistory().peek().get('a'));
			assertEquals(m1.get('b'), level.getVehiclePositionHistory().peek().get('b'));
			assertEquals(m1.get('c'), level.getVehiclePositionHistory().peek().get('c'));
			assertEquals(m1.get('d'), level.getVehiclePositionHistory().peek().get('d'));
			assertEquals(m1.get('e'), level.getVehiclePositionHistory().peek().get('e'));
			assertEquals(m1.get('f'), level.getVehiclePositionHistory().peek().get('f'));
			assertEquals(m1.get('g'), level.getVehiclePositionHistory().peek().get('g'));
			
		}
		@Test
		public void PositionHistoryWithMov() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('f'), 'L', 1);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);

			
			
			Deque<Map<Character, Set<Pair<Integer, Integer>>>> vehiclePositionHistory = new ArrayDeque<>();
			Map<Character, Set<Pair<Integer, Integer>>> m1= new HashMap<Character, Set<Pair<Integer, Integer>>>();
			Set<Pair<Integer, Integer>>  h1 =new HashSet<>();
			h1.add(new Pair<>(1,1));
			h1.add(new Pair<>(2,1));
			m1.put('a',h1);

			Set<Pair<Integer, Integer>>  h2 =new HashSet<>();
			h2.add(new Pair<>(3,1));
			h2.add(new Pair<>(4,1));	
			h2.add(new Pair<>(5,1));
			m1.put('b',h2);

			Set<Pair<Integer, Integer>>  h3 =new HashSet<>();
			h3.add(new Pair<>(6,4));
			h3.add(new Pair<>(6,5));	
			m1.put('c',h3);

			Set<Pair<Integer, Integer>>  h4 =new HashSet<>();
			h4.add(new Pair<>(1,3));
			h4.add(new Pair<>(1,4));	
			h4.add(new Pair<>(1,5));
			m1.put('d',h4);

			Set<Pair<Integer, Integer>>  h5 =new HashSet<>();
			h5.add(new Pair<>(2,2));
			h5.add(new Pair<>(2,3));	
			m1.put('e',h5);

			Set<Pair<Integer, Integer>>  h6 =new HashSet<>();
			h6.add(new Pair<>(2,4));
			h6.add(new Pair<>(3,4));	
			h6.add(new Pair<>(4,4));
			m1.put('f',h6);

			Set<Pair<Integer, Integer>>  h7 =new HashSet<>();
			h7.add(new Pair<>(1,6));
			h7.add(new Pair<>(2,6));	
			h7.add(new Pair<>(3,6));
			m1.put('g',h7);

			vehiclePositionHistory.add(m1);

			assertEquals(m1.get('a'), level.getVehiclePositionHistory().peek().get('a'));
			assertEquals(m1.get('b'), level.getVehiclePositionHistory().peek().get('b'));
			assertEquals(m1.get('c'), level.getVehiclePositionHistory().peek().get('c'));
			assertEquals(m1.get('d'), level.getVehiclePositionHistory().peek().get('d'));
			assertEquals(m1.get('e'), level.getVehiclePositionHistory().peek().get('e'));
			assertEquals(m1.get('f'), level.getVehiclePositionHistory().peek().get('f'));
			assertEquals(m1.get('g'), level.getVehiclePositionHistory().peek().get('g'));
			
		}
		
		@Test
		public void MiddlePositionHistoryWithMov() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('f'), 'L', 1);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);

			Deque<Map<Character, Set<Pair<Integer, Integer>>>> vehiclePositionHistory = new ArrayDeque<>();
			Map<Character, Set<Pair<Integer, Integer>>> m1= new HashMap<Character, Set<Pair<Integer, Integer>>>();
			Set<Pair<Integer, Integer>>  h1 =new HashSet<>();
			h1.add(new Pair<>(1,1));
			h1.add(new Pair<>(2,1));
			m1.put('a',h1);

			Set<Pair<Integer, Integer>>  h2 =new HashSet<>();
			h2.add(new Pair<>(3,1));
			h2.add(new Pair<>(4,1));	
			h2.add(new Pair<>(5,1));
			m1.put('b',h2);

			Set<Pair<Integer, Integer>>  h3 =new HashSet<>();
			h3.add(new Pair<>(6,1));
			h3.add(new Pair<>(6,2));	
			m1.put('c',h3);

			Set<Pair<Integer, Integer>>  h4 =new HashSet<>();
			h4.add(new Pair<>(1,3));
			h4.add(new Pair<>(1,4));	
			h4.add(new Pair<>(1,5));
			m1.put('d',h4);

			Set<Pair<Integer, Integer>>  h5 =new HashSet<>();
			h5.add(new Pair<>(2,2));
			h5.add(new Pair<>(2,3));	
			m1.put('e',h5);

			Set<Pair<Integer, Integer>>  h6 =new HashSet<>();
			h6.add(new Pair<>(2,4));
			h6.add(new Pair<>(3,4));	
			h6.add(new Pair<>(4,4));
			m1.put('f',h6);

			Set<Pair<Integer, Integer>>  h7 =new HashSet<>();
			h7.add(new Pair<>(4,6));
			h7.add(new Pair<>(5,6));	
			h7.add(new Pair<>(6,6));
			m1.put('g',h7);
			Map<Character, Set<Pair<Integer, Integer>>> serchedPosition= new HashMap<>();
			vehiclePositionHistory.add(m1);
            int index=0; // 0 is the original position
			for (Map<Character, Set<Pair<Integer, Integer>>> middlePosition : level.getVehiclePositionHistory()) {
				if(index==2) { 
					 serchedPosition= middlePosition ;
					break;
				}
				
				index++;
				
				
			}
			assertEquals(m1.get('a'), serchedPosition.get('a'));
			assertEquals(m1.get('b'),  serchedPosition.get('b'));
			assertEquals(m1.get('c'),  serchedPosition.get('c'));
			assertEquals(m1.get('d'),  serchedPosition.get('d'));
			assertEquals(m1.get('e'),  serchedPosition.get('e'));
			assertEquals(m1.get('f'),  serchedPosition.get('f'));
			assertEquals(m1.get('g'),  serchedPosition.get('g'));
			
		}
		
		
		@Test
		public void PositionHistoryWithUndo() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('f'), 'L', 1);
			level.undo();
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.undo();
			

			Deque<Map<Character, Set<Pair<Integer, Integer>>>> vehiclePositionHistory = new ArrayDeque<>();
			Map<Character, Set<Pair<Integer, Integer>>> m1= new HashMap<Character, Set<Pair<Integer, Integer>>>();
			Set<Pair<Integer, Integer>>  h1 =new HashSet<>();
			h1.add(new Pair<>(1,1));
			h1.add(new Pair<>(2,1));
			m1.put('a',h1);

			Set<Pair<Integer, Integer>>  h2 =new HashSet<>();
			h2.add(new Pair<>(3,1));
			h2.add(new Pair<>(4,1));	
			h2.add(new Pair<>(5,1));
			m1.put('b',h2);

			Set<Pair<Integer, Integer>>  h3 =new HashSet<>();
			h3.add(new Pair<>(6,1));
			h3.add(new Pair<>(6,2));	
			m1.put('c',h3);

			Set<Pair<Integer, Integer>>  h4 =new HashSet<>();
			h4.add(new Pair<>(1,3));
			h4.add(new Pair<>(1,4));	
			h4.add(new Pair<>(1,5));
			m1.put('d',h4);

			Set<Pair<Integer, Integer>>  h5 =new HashSet<>();
			h5.add(new Pair<>(2,2));
			h5.add(new Pair<>(2,3));	
			m1.put('e',h5);

			Set<Pair<Integer, Integer>>  h6 =new HashSet<>();
			h6.add(new Pair<>(3,4));
			h6.add(new Pair<>(4,4));	
			h6.add(new Pair<>(5,4));
			m1.put('f',h6);

			Set<Pair<Integer, Integer>>  h7 =new HashSet<>();
			h7.add(new Pair<>(1,6));
			h7.add(new Pair<>(2,6));	
			h7.add(new Pair<>(3,6));
			m1.put('g',h7);

			vehiclePositionHistory.add(m1);

			assertEquals(m1.get('a'), level.getVehiclePositionHistory().peek().get('a'));
			assertEquals(m1.get('b'), level.getVehiclePositionHistory().peek().get('b'));
			assertEquals(m1.get('c'), level.getVehiclePositionHistory().peek().get('c'));
			assertEquals(m1.get('d'), level.getVehiclePositionHistory().peek().get('d'));
			assertEquals(m1.get('e'), level.getVehiclePositionHistory().peek().get('e'));
			assertEquals(m1.get('f'), level.getVehiclePositionHistory().peek().get('f'));
			assertEquals(m1.get('g'), level.getVehiclePositionHistory().peek().get('g'));

		}

		@Test
		public void PositionHistoryPostReset() {
			
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			level.reset();
			Map<Pair<Character,Vehicle>, Set<Pair<Integer, Integer>>> initialVehiclePositions = new HashMap<>();

			for(Entry<Character, Vehicle> vh : level.getCars().entrySet()) {
				initialVehiclePositions.put(new Pair <>(vh.getKey(),vh.getValue()), vh.getValue().getPosition());
			}
			
			assertEquals(initialVehiclePositions.get(new Pair<>('a', level.getCars().get('a'))), level.getCars().get('a').getPosition());
			assertEquals(initialVehiclePositions.get(new Pair<>('b', level.getCars().get('b'))), level.getCars().get('b').getPosition());
			assertEquals(initialVehiclePositions.get(new Pair<>('c', level.getCars().get('c'))), level.getCars().get('c').getPosition());
			assertEquals(initialVehiclePositions.get(new Pair<>('d', level.getCars().get('d'))), level.getCars().get('d').getPosition());
			assertEquals(initialVehiclePositions.get(new Pair<>('e', level.getCars().get('e'))), level.getCars().get('e').getPosition());
			assertEquals(initialVehiclePositions.get(new Pair<>('f', level.getCars().get('f'))), level.getCars().get('f').getPosition());
			assertEquals(initialVehiclePositions.get(new Pair<>('g', level.getCars().get('g'))), level.getCars().get('g').getPosition());
			

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
		public void undoMov() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			assertEquals('g',level.undo());
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]='e'; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]='e'; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]=null;  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]=null;  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}


		}

		@Test
		public void carPositionPostMov() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			assertEquals('g',level.undo());
			Set<Pair<Integer, Integer>> position = new HashSet<>();
			Pair<Integer, Integer> par1 = new Pair<>(2,2);
			Pair<Integer, Integer> par2 = new Pair<>(2,3);
			position.add(par1);
			position.add(par2);
			Set<Pair<Integer, Integer>> position2 = new HashSet<>();
			Pair<Integer, Integer> par3 = new Pair<>(4,6);
			Pair<Integer, Integer> par4 = new Pair<>(5,6);
			Pair<Integer, Integer> par5 = new Pair<>(6,6);
			position2.add(par3);
			position2.add(par4);
			position2.add(par5);
			assertEquals(position, level.getCars().get('e').getPosition());
			assertEquals(position2, level.getCars().get('g').getPosition());
		}
		@Test
		public void LevelPoints() {
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
			boardt[0][6]='+'; boardt[1][6]='g'; boardt[2][6]='g';  boardt[3][6]='g'; boardt[4][6]='*';  boardt[5][6]=null;  boardt[6][6]=null;  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='*';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoard();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}

		}
		@Test
		public void ResetGame() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.reset();
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
		public void WinGameWhitUndo() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('f'), 'L', 1);
			level.undo();
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.undo();
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
			boardt[0][6]='+'; boardt[1][6]='g'; boardt[2][6]='g';  boardt[3][6]='g'; boardt[4][6]='*';  boardt[5][6]=null;  boardt[6][6]=null;  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='*';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

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
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
		public void testInvalidMoveCarPosition() {

			Vehicle car4 = level.getCars().get('g');
			Set<Pair<Integer, Integer>> positionA =car4.getPosition();
			level.move(car4,'D',19); // A CAR CANNOT MOVE outside of the board
			Set<Pair<Integer, Integer>> positionD =car4.getPosition();


			assertEquals(positionA, positionD);

		}
		@Test
		public void testInvalidMoveCarPosition2() {

			Vehicle car3 = level.getCars().get('*');
			Set<Pair<Integer, Integer>> positionA =car3.getPosition();
			level.move(car3,'D',1 ); // A CAR CANNOT MOVE IF THERES ANOHER CAR IN THAT POSITION
			Set<Pair<Integer, Integer>> positionD =car3.getPosition();
			assertEquals(positionA, positionD);

		}
		@Test
		public void testInvalidMoveCarPosition3() {

			Vehicle car2 = level.getCars().get('g');
			Set<Pair<Integer, Integer>> positionA =car2.getPosition();
			level.move(car2,'R',1 ); // A CAR CANNOT MOVE TOWARS A WALL
			Set<Pair<Integer, Integer>> positionD =car2.getPosition();
			assertEquals(positionA, positionD);
		}

		@Test
		public void testValidMoveCarPosition() {

			Vehicle car = level.getCars().get('e');

			Set<Pair<Integer, Integer>> positionA =new HashSet();
			Pair<Integer, Integer> p1 =new Pair<Integer, Integer>(2,2);
			Pair<Integer, Integer> p2 =new Pair<Integer, Integer>(2,3);
			positionA.add(p1);
			positionA.add(p2);
			level.move(car, 'U', 3);
			Set<Pair<Integer, Integer>> positionD =car.getPosition();
			assertEquals(positionA, positionD);
		}

		@Test
		public void testValidMoveCarPosition2() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			Vehicle car = level.getCars().get('*');
			Set<Pair<Integer, Integer>> positionA =new HashSet();
			Pair<Integer, Integer> p1 =new Pair<Integer, Integer>(4,6);
			Pair<Integer, Integer> p2 =new Pair<Integer, Integer>(4,7);
			positionA.add(p1);
			positionA.add(p2);
			Set<Pair<Integer, Integer>> positionD =car.getPosition();
			assertEquals(positionD, positionA);
		}

		@Test
		public void UndoAllTheWay_BoardStatus() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
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
		public void MoreUndosThanMovements() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			assertEquals('e',level.undo());
		
			assertEquals(' ',level.undo());
		}
		@Test
		public void IntialStatus() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.move(level.getCars().get('a'), 'R', 1);
			level.move(level.getCars().get('d'), 'U', 2);
			level.move(level.getCars().get('f'), 'L', 2);
			level.move(level.getCars().get('*'), 'D', 4);
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			level.undo();
			assertEquals('e',level.undo());
			assertEquals(' ',level.undo());
			Character[][] boardt = new Character[8][8];
			boardt[0][0]='+'; boardt[1][0]='+';	 boardt[2][0]='+';  boardt[3][0]='+';  boardt[4][0]='+';  boardt[5][0]='+';  boardt[6][0]='+';  boardt[7][0]='+';
			boardt[0][1]='+'; boardt[1][1]='a';	 boardt[2][1]='a';  boardt[3][1]='b';  boardt[4][1]='b';  boardt[5][1]='b';  boardt[6][1]='c';  boardt[7][1]='+';
			boardt[0][2]='+'; boardt[1][2]=null; boardt[2][2]=null; boardt[3][2]=null; boardt[4][2]='*';  boardt[5][2]=null; boardt[6][2]='c';  boardt[7][2]='+';
			boardt[0][3]='+'; boardt[1][3]='d';  boardt[2][3]=null; boardt[3][3]=null; boardt[4][3]='*';  boardt[5][3]=null; boardt[6][3]=null;  boardt[7][3]='+';
			boardt[0][4]='+'; boardt[1][4]='d';  boardt[2][4]=null; boardt[3][4]='f';  boardt[4][4]='f';  boardt[5][4]='f';  boardt[6][4]=null; boardt[7][4]='+';
			boardt[0][5]='+'; boardt[1][5]='d';  boardt[2][5]='e';  boardt[3][5]=null; boardt[4][5]=null; boardt[5][5]=null; boardt[6][5]=null; boardt[7][5]='+';
			boardt[0][6]='+'; boardt[1][6]=null; boardt[2][6]='e';  boardt[3][6]=null; boardt[4][6]='g';  boardt[5][6]='g';  boardt[6][6]='g';  boardt[7][6]='+';
			boardt[0][7]='+'; boardt[1][7]='+';  boardt[2][7]='+';  boardt[3][7]='+';  boardt[4][7]='@';  boardt[5][7]='+';  boardt[6][7]='+';  boardt[7][7]='+';

			Character[][] board = level.getBoardHistory().peek();
			for(int i =0; i<8; i++) {
				for(int j=0; j<8;j++) {
					assertEquals(board[i][j], boardt[i][j]);
				}
			}
			
		}
		
		@Test
		public void UndoAlTheWay_PositionStatus() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);

			level.undo();
			level.undo();
			level.undo();

			Set<Pair<Integer, Integer>> vehiclesPosition1 = new HashSet<>();
			Pair<Integer, Integer> par11= new Pair<>(2,5);
			Pair<Integer, Integer> par12= new Pair<>(2,6);
			vehiclesPosition1.add(par11);
			vehiclesPosition1.add(par12);

			assertEquals(vehiclesPosition1, level.getCars().get('e').getPosition());

			Set<Pair<Integer, Integer>> vehiclesPosition2 = new HashSet<>();
			Pair<Integer, Integer> par3 = new Pair<>(4,6);
			Pair<Integer, Integer> par4 = new Pair<>(5,6);
			Pair<Integer, Integer> par5 = new Pair<>(6,6);
			vehiclesPosition2.add(par3);
			vehiclesPosition2.add(par4);
			vehiclesPosition2.add(par5);

			assertEquals(vehiclesPosition2, level.getCars().get('g').getPosition());

			Set<Pair<Integer, Integer>> vehiclesPosition3 = new HashSet<>();
			Pair<Integer, Integer> par21= new Pair<>(6,1);
			Pair<Integer, Integer> par22= new Pair<>(6,2);
			vehiclesPosition3.add(par21);
			vehiclesPosition3.add(par22);

			assertEquals(vehiclesPosition3, level.getCars().get('c').getPosition());

		}

		@Test
		public void UndoAlTheWay_PositionStatus2() {
			level.move(level.getCars().get('e'), 'U', 3);
			level.move(level.getCars().get('g'), 'L', 3);
			level.move(level.getCars().get('c'), 'D', 3);
			level.move(level.getCars().get('b'), 'R', 1);
			level.undo();
			level.undo();


			Set<Pair<Integer, Integer>> vehiclesPosition1 = new HashSet<>();
			Pair<Integer, Integer> par11= new Pair<>(2,2);
			Pair<Integer, Integer> par12= new Pair<>(2,3);
			vehiclesPosition1.add(par11);
			vehiclesPosition1.add(par12);	
			assertEquals(vehiclesPosition1, level.getCars().get('e').getPosition());

			Set<Pair<Integer, Integer>> vehiclesPosition2 = new HashSet<>();
			Pair<Integer, Integer> par3 = new Pair<>(1,6);
			Pair<Integer, Integer> par4 = new Pair<>(2,6);
			Pair<Integer, Integer> par5 = new Pair<>(3,6);
			vehiclesPosition2.add(par3);
			vehiclesPosition2.add(par4);
			vehiclesPosition2.add(par5);
			assertEquals(vehiclesPosition2, level.getCars().get('g').getPosition());


			Set<Pair<Integer, Integer>> vehiclesPosition4 = new HashSet<>();
			Pair<Integer, Integer> par41= new Pair<>(3,1);
			Pair<Integer, Integer> par42= new Pair<>(4,1);
			Pair<Integer, Integer> par43= new Pair<>(5,1);
			vehiclesPosition4.add(par41);
			vehiclesPosition4.add(par42);
			vehiclesPosition4.add(par43);
			assertEquals(vehiclesPosition4, level.getCars().get('b').getPosition());


			Set<Pair<Integer, Integer>> vehiclesPosition3 = new HashSet<>();
			Pair<Integer, Integer> par21= new Pair<>(6,1);
			Pair<Integer, Integer> par22= new Pair<>(6,2);
			vehiclesPosition3.add(par21);
			vehiclesPosition3.add(par22);
			assertEquals(vehiclesPosition3, level.getCars().get('c').getPosition());



		}





	}

	class GameTest{
		private Game game;

		@BeforeEach
		public void Ini() throws FileNotFoundException,IOException {
			game = new Game("Paco");
		}

		@Test
		public void getGamePointsTest()
		{
			assertEquals(0,game.getGamePoints());
			game.actualizarGamePoints(1, 100);
			assertEquals(100, game.getGamePoints());
			game.actualizarGamePoints(2, 30);
			assertEquals(130, game.getGamePoints());
			game.actualizarGamePoints(1, 50);
			assertEquals(80, game.getGamePoints());

		}
		@Test
		public void getLevelPointsTest()
		{
			game.actualizarGamePoints(1, 100);
			assertEquals(100, game.getLevelPoints(1));
		}
		@Test
		public void getNameTest()
		{
			assertEquals("Paco", game.getName());
		}
		@Test
		public void getUltimoLevelPassedTest()
		{
			game.setUltimoLevelPassed(3);
			assertEquals(3, game.getUltimoLevelPassed());
		}
		@Test
		public void getLevelTest()
		{
			Level level=null;
			try {
				level = new Level (1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			game.setLevel(1, level);
			assertEquals(level, game.getLevel(1));
		}
		@Test
		public void setLevelTest()
		{
			Level level=null;
			try {
				level = new Level (1);
				level.setLevelPoints(50);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			game.setLevel(1, level);
			assertEquals(level, game.getLevel(1));
			assertEquals(50, game.getLevelPoints(1));
		}
		@Test
		public void guardarGameSinLevelPointsTest()
		{
			try {
				game.guardarGame(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String rutaDirectorioBase = System.getProperty("user.dir");
       		String rutaFicheroPoints = rutaDirectorioBase + File.separator + "src" + File.separator + "main" + File.separator + "gamesSaved" + File.separator + "Paco" + File.separator + "gamePoints.txt";
			 File ficheroPoints = new File(rutaFicheroPoints);

			// Usar try-with-resources para asegurar que los recursos se cierren automáticamente
			try (FileReader fr = new FileReader(ficheroPoints);
				BufferedReader br = new BufferedReader(fr)) {
				String linea = br.readLine();
				assertEquals(null, linea);
				
			}catch (IOException e) {
				// Manejo de posibles excepciones
				System.err.println("Se produjo un error al leer el archivo: " + e.getMessage());
			}
		}
		@Test
		public void guardarGameSinLevelTest()
		{
			Level level1=null;
			Level level2=null;
			try {
				level1 = new Level(1);
				level2= new Level(2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			level1.setLevelPoints(10);
			level2.setLevelPoints(20);
			try {
				game.guardarGame(null);
				game.setLevel(1, level1);
				game.setLevel(2, level2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String rutaDirectorioBase = System.getProperty("user.dir");
       		String rutaFicheroPoints = rutaDirectorioBase + File.separator + "src" + File.separator + "main" + File.separator + "gamesSaved" + File.separator + "Paco" + File.separator + "gamePoints.txt";
			 File ficheroPoints = new File(rutaFicheroPoints);

			// Usar try-with-resources para asegurar que los recursos se cierren automáticamente
			try (FileReader fr = new FileReader(ficheroPoints);
				BufferedReader br = new BufferedReader(fr)) {
				String linea = br.readLine();
				assertEquals("1 : 10", linea);
				String linea2 = br.readLine();
				assertEquals("2 : 20", linea2);
				String linea3 = br.readLine();
				assertEquals(null, linea3);
				
			}catch (IOException e) {
				// Manejo de posibles excepciones
				System.err.println("Se produjo un error al leer el archivo: " + e.getMessage());
			}
		}
		@Test
		public void guardarGameConLevelTest()
		{
			Level level1=null;
			Level level2=null;
			Level level3=null;
			try {
				level1 = new Level(1);
				level2= new Level(2);
				level3= new Level(3);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			level1.setLevelPoints(10);
			level2.setLevelPoints(20);
			try {
				game.guardarGame(null);
				game.setLevel(1, level1);
				game.setLevel(2, level2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String rutaDirectorioBase = System.getProperty("user.dir");
       		String rutaFicheroPoints = rutaDirectorioBase + File.separator + "src" + File.separator + "main" + File.separator + "gamesSaved" + File.separator + "Paco" + File.separator + "gamePoints.txt";
			 File ficheroPoints = new File(rutaFicheroPoints);

			// Usar try-with-resources para asegurar que los recursos se cierren automáticamente
			try (FileReader fr = new FileReader(ficheroPoints);
				BufferedReader br = new BufferedReader(fr)) {
				String linea = br.readLine();
				assertEquals("1 : 10", linea);
				String linea2 = br.readLine();
				assertEquals("2 : 20", linea2);
				String linea3 = br.readLine();
				assertEquals(null, linea3);
				
			}catch (IOException e) {
				// Manejo de posibles excepciones
				System.err.println("Se produjo un error al leer el archivo: " + e.getMessage());
			}
			String rutaFicheroTablero = rutaDirectorioBase + File.separator + "src" + File.separator + "main" + File.separator + "gamesSaved" + File.separator + "Paco" + File.separator + level3.getTitle()+".txt";
			 File ficheroTablero = new File(rutaFicheroTablero);

			// Usar try-with-resources para asegurar que los recursos se cierren automáticamente
			try (FileReader fr = new FileReader(ficheroTablero);
				BufferedReader br = new BufferedReader(fr)) {
				String linea = br.readLine();
				assertEquals("Third level", linea);
				linea = br.readLine();
				assertEquals("8 8", linea);
				linea = br.readLine();
				assertEquals("++++++++", linea);
				linea = br.readLine();
				assertEquals("+abbb  +", linea);
				linea = br.readLine();
				assertEquals("+a  c  +", linea);
				linea = br.readLine();
				assertEquals("+dddc  +", linea);
				linea = br.readLine();
				assertEquals("+dddc  +", linea);
				linea = br.readLine();
				assertEquals("@  e**f+", linea);
				linea = br.readLine();
				assertEquals("+  e  f+", linea);
				linea = br.readLine();
				assertEquals("+  e  f+", linea);
				linea = br.readLine();
				assertEquals("++++++++", linea);
				linea = br.readLine();
				assertEquals(null, linea);
				
			}catch (IOException e) {
				// Manejo de posibles excepciones
				System.err.println("Se produjo un error al leer el archivo: " + e.getMessage());
			}
		}
		@Test
		public void cargarGameSinLevelTest()
		{
			Game gameTest= new Game("");
			Level level1=null;
			Level level2=null;
			try {
				level2= new Level(2);
				level1=new Level(1);
				level1.setLevelPoints(20);
				level2.setLevelPoints(50);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				game.setLevel(1, level1);
				game.setLevel(2, level2);
				game.guardarGame(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Pair <Deque<Character[][]>,Level> pair= gameTest.cargarGame("Paco");
			assertEquals(20, gameTest.getLevelPoints(1) );
			assertEquals(50, gameTest.getLevelPoints(2));
			assertEquals(70, game.getGamePoints());
			assertEquals(level1, gameTest.getLevel(1));
			assertEquals(level2, gameTest.getLevel(2));
			assertEquals(null, pair.getKey());
			assertEquals(null, pair.getValue());
		}
		@Test
		public void cargarGameConLevelTest()
		{
			Game gameTest= new Game("");
			Level level1=null;
			Level level2=null;
			Level level3=null;
			try {
				level2= new Level(2);
				level1=new Level(1);
				level3=new Level(3);
				level1.setLevelPoints(20);
				level2.setLevelPoints(50);
				level3.setLevelPoints(30);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				game.setLevel(1, level1);
				game.setLevel(2, level2);
				game.guardarGame(level3);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Pair <Deque<Character[][]>,Level> pair= gameTest.cargarGame("Paco");
			assertEquals(20, gameTest.getLevelPoints(1) );
			assertEquals(50, gameTest.getLevelPoints(2));
			assertEquals(70, game.getGamePoints());
			assertEquals(level1, gameTest.getLevel(1));
			assertEquals(level2, gameTest.getLevel(2));
			assertEquals(level3.getBoardHistory(), pair.getKey());
			assertEquals(level3, pair.getValue());
		}


	}
	class GameListTest{

		private GamesList gamesList;

		@BeforeEach
		public void Ini() throws FileNotFoundException,IOException {
			gamesList = new GamesList();
		}
		@Test
		public void addGameTest()
		{
			gamesList.addGame("Paco");
			gamesList.addGame("Antonio");
			ArrayList<String> lista = gamesList.getList();
			assertTrue(lista.contains("Paco"));
			assertTrue(lista.contains("Antonio"));
			assertTrue(lista.size()==2);
			String rutaFicheroListaGames=System.getProperty("user.dir")+"/src/main/gamesSaved/GamesList.txt"; 
			File ficheroListaGames = new File(rutaFicheroListaGames);

			// Usar try-with-resources para asegurar que los recursos se cierren automáticamente
			try (FileReader fr = new FileReader(ficheroListaGames);
				BufferedReader br = new BufferedReader(fr)) {
				String linea = br.readLine();
				assertEquals("Paco", linea);
				linea = br.readLine();
				assertEquals("Antonio", linea);
				linea = br.readLine();
				assertEquals(null, linea);
			}catch (IOException e) {
			// Manejo de posibles excepciones
			System.err.println("Se produjo un error al leer el archivo: " + e.getMessage());
			}
		}
		@Test
		public void addGameVacioTest()
		{
			ArrayList<String> lista = gamesList.getList();
			assertTrue(lista.isEmpty());
		}
		@Test 
		public void loadListTest()
		{
			gamesList.addGame("Paco");
			gamesList.addGame("Roberto");
			gamesList.addGame("Sandra");
			ArrayList<String> list= gamesList.loadList();
			assertEquals(3, list.size());
			assertEquals("Paco", list.get(1));
			assertEquals("Roberto", list.get(2));
			assertEquals("Sandra", list.get(3));
			
		}
		@Test
		public void loadListVaciaTest()
		{
			ArrayList <String> list= gamesList.loadList();
			assertTrue(list.isEmpty());
		}


	}


}


