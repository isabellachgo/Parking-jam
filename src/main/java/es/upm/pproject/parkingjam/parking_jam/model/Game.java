package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


import javafx.util.Pair;

import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Game {

	private Integer gamePoints;
	private String gameName;
	private Integer ultimoLevelPassed;
	private HashMap <Integer,Level> listaLevels;
	private HashMap <Integer,Integer> listaPoints;
	private Level l;
	private Map<Integer, Boolean> okLevels;
	private static final String ERROR_MESSAGE_WRITE = "There has been an error writing in the file";
	private static final String ERROR_MESSAGE_READ = "There has been an error writing in the file";

	private static final Logger LOGGER = Logger.getLogger(Game.class);
	
	// Constructor
	public Game( String name)
	{
		gameName=name;
		gamePoints=0;
		ultimoLevelPassed = 0;
		l=null;
		if (listaLevels==null) listaLevels=new HashMap<>();
		if (listaPoints==null) listaPoints=new HashMap<>();
		
		okLevels= new HashMap<>();
		okLevels.put(1,true);
		okLevels.put(2,true);
		okLevels.put(3,true);
		okLevels.put(4,true);
	}

	public Integer getGamePoints ()
	{
		return gamePoints;
	}

	public Integer getLevelPoints(int id) {
		if(listaPoints.containsKey(id))	return listaPoints.get(id);
		else return null;
	}
	
	// Updates the game points with the points obtained in the current level
	public void actualizarGamePoints (int id , int points)
	{
		listaPoints.put(id, points);
		gamePoints=0;
		for (Map.Entry<Integer, Integer> entry : listaPoints.entrySet())             
			gamePoints += entry.getValue();   
		LOGGER.info("The game: " + id + "has been updated with " + points+ " points");
	}

	public  String getName()
	{
		return gameName;
	}

	public int getUltimoLevelPassed()
	{
		return ultimoLevelPassed;
	}

	public void setUltimoLevelPassed( int level)
	{
		ultimoLevelPassed = level;
	}

	public Level getLevel(int numero)
	{
		return listaLevels.get(numero);
	}

	public void setLevel( int numero , Level level)
	{
		listaLevels.put(numero, level);
	}
		
	public Level getLastLevel() {
		return l;
	}
	public void setLastLevel(Level l) {
		this.l=l;
	}
	
	public boolean getOkLevel(Integer n) {
		return okLevels.get(n);
	}
	public void setOkLevel (Integer n, Boolean b) {
		okLevels.put(n, b);
	}

	// Saves the Game in 'gamesSaved'
	public  void guardarGame(Level level) throws IOException
	{
		String rutaFichero = System.getProperty("user.dir");  
		String rutaDirectorio=rutaFichero+"/src/main/gamesSaved/"+ gameName ;

		File directorio = new File(rutaDirectorio);
		if (!directorio.mkdirs()) {
			LOGGER.error("Failed to create directories: " + directorio.getAbsolutePath());
		}

		// Creates a file 
		rutaFichero= rutaDirectorio+ File.separator+ "gamePoints.txt";
		File fichero = new File(rutaFichero);
		try (FileWriter fw = new FileWriter(fichero);
				BufferedWriter bw = new BufferedWriter(fw)) {

			//writes in file
			Iterator<Entry<Integer, Level>> it = listaLevels.entrySet().iterator();
		
			for (int i=0; i <listaLevels.size() && it.hasNext(); i++)
			{    
				Entry<Integer, Level> e= it.next();
				bw.write(e.getKey()+" : " + e.getValue().getLevelPoint());
				bw.newLine(); 
			}

			if(level!=null) bw.write(level.getNLevel() + " , " + level.getLevelPoint());

			LOGGER.info("The file has been created and its been witten correctly in " + rutaFichero);

		} catch (IOException e) {
			
			LOGGER.error(ERROR_MESSAGE_WRITE, e);
		}

		if ( level!=null)
		{
			rutaFichero= rutaDirectorio+ File.separator+ "level.txt";
			File ficheroTablero = new File(rutaFichero);
			try (FileWriter fw = new FileWriter(ficheroTablero);
					BufferedWriter bw = new BufferedWriter(fw)) {

				// Writes in file
				bw.write(level.getTitle());
				bw.newLine();
				bw.write(level.getDimensionX()+ " " + level.getDimensionY());
				bw.newLine();
				Character [][] board = level.getBoard();
				for ( int i = 0 ; i<level.getDimensionY(); i++)
				{
					for ( int j =0 ; j< level.getDimensionX();j++)
					{
						if(board[j][i]== null) bw.write(" ");
						else bw.write(board[j][i]);
					}
					bw.newLine();
				}

				
				LOGGER.info("The file has been created and its been witten corectly in " + rutaFichero);

			} catch (IOException e) {
				LOGGER.error(ERROR_MESSAGE_WRITE, e);
			}

			rutaFichero= rutaDirectorio+ File.separator+ "HistoryBoards.txt";
			File ficheroTablerosHistoricos = new File(rutaFichero);
		
			try (FileWriter fw = new FileWriter(ficheroTablerosHistoricos);
					BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write(Integer.toString(level.getBoardHistory().size()) );
				bw.newLine();
				bw.write(level.getDimensionX()+" "+level.getDimensionY());
				bw.newLine();

				while (!level.getBoardHistory().isEmpty()) {

					Character [][] board = level.getBoardHistory().pop();
					for ( int i = 0 ; i<level.getDimensionY(); i++)
					{
						for ( int j =0 ; j< level.getDimensionX();j++)
						{
							if(board[j][i]== null) bw.write(" ");
							else bw.write(board[j][i]);
						}
						bw.newLine();
					}
				}
				LOGGER.info("The file has been created and its been witten corectly in " + rutaFichero);


			} catch (IOException e) {
				LOGGER.error(ERROR_MESSAGE_WRITE, e);
			}

		}
	}

	public Level cargarGame ( String name)
	{
		String rutaDirectorioBase = System.getProperty("user.dir");
		String rutaFicheroPoints = rutaDirectorioBase + File.separator + "src" + File.separator + "main" + File.separator + "gamesSaved" + File.separator + name + File.separator + "gamePoints.txt";
		String rutaFicheroHistoryBoards = rutaDirectorioBase + File.separator + "src" + File.separator + "main" + File.separator + "gamesSaved" + File.separator + name + File.separator + "HistoryBoards.txt";
		List<Character[][]> boardHistory= new ArrayList<>();
		Level levelUncomplete = null;
		this.gameName=name;
		File ficheroPoints = new File(rutaFicheroPoints);

		try (FileReader fr = new FileReader(ficheroPoints);
				BufferedReader br = new BufferedReader(fr)) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] partes = linea.split(" : ");
				if (partes.length == 2) {
					String antesDeDosPuntos = partes[0].trim();
					String despuesDeDosPuntos = partes[1].trim();
					int id = Integer.parseInt(antesDeDosPuntos) ;
					int points = Integer.parseInt(despuesDeDosPuntos);
                    Level level = new Level(id);
                    level.setLevelPoints(points);
                    setLevel(id, level);
					actualizarGamePoints(id, points);
					setUltimoLevelPassed(id);
				}
				else if(partes.length==1) {
					String[] partes2 = linea.split(" , ");
					if (partes2.length == 2) {
						String points = partes2[1].trim();
						String nLevel= partes2[0].trim();
						int levelPoints= Integer.parseInt(points);
						String filepath = "src/main/gamesSaved/" + name + "/level.txt";
						levelUncomplete=new Level(filepath);
						levelUncomplete.setLevelPoints(levelPoints);
						levelUncomplete.setNLevel(Integer.parseInt(nLevel));

					}
				}
				else {
					LOGGER.info("There is an error in the file, the line format is not correct");
				}

			}

		} catch (IOException e) {
			LOGGER.error(ERROR_MESSAGE_READ, e);
		}


		File ficheroHistoyBoards = new File(rutaFicheroHistoryBoards);
		try (FileReader fr = new FileReader(ficheroHistoyBoards);
				BufferedReader br = new BufferedReader(fr)) {
			String linea;
			int sizeX = 0;
			int sizeY=0;
			int nBoards=0;


			if ((linea = br.readLine()) != null) nBoards= Integer.parseInt(linea);
			if ((linea = br.readLine()) != null)
			{
				String tamX=null;
				String tamY=null;
				String[] partes = linea.split(" ");
				if (partes.length == 2) {
					tamX = partes[0].trim();
					tamY = partes[1].trim();
				}
                if (tamX != null && !tamX.isEmpty() && tamY != null && !tamY.isEmpty()) {
                    sizeX = Integer.parseInt(tamX);
                    sizeY = Integer.parseInt(tamY);
                }
			}

			Deque<Map<Character, Set<Pair<Integer, Integer>>>> vph= new ArrayDeque<>();
			HashMap<Character, Set<Pair<Integer,Integer>> > posAux = new HashMap<>();
			Character[][] board = new Character[sizeX][sizeY];
			for(int x=0; x<nBoards;x++)
			{
				board = new Character[sizeX][sizeY];
				posAux = new HashMap<>();

				for(int j =0 ; j< sizeY &&((linea = br.readLine()) != null);j++)
				{
					char[] lineaChar=linea.toCharArray();
					for ( int i=0 ; i<sizeX ; i++)
					{
						Character c = lineaChar[i];
						if(c==' ') board[i][j]=null;
						else board[i][j]= c;
						if(c!='+' && c!=' ' && c!='@') {
							if(!posAux.containsKey(c)) {
								Set<Pair<Integer,Integer>> positions = new HashSet<>();
								Pair<Integer, Integer> p = new Pair<>(i,j);
								positions.add(p);
								posAux.put(c,positions);
							} else {
								Set<Pair<Integer,Integer>> positions = posAux.get(lineaChar[i]);
								positions.add(new Pair<>(i,j));
								posAux.put(c,positions);
							}
						}
					}
				}
				boardHistory.add(board);
				vph.add(posAux);
			}
			if(levelUncomplete!=null) {
				Deque<Character[][]> bh = new ArrayDeque<>();
				for(int i=0; i<boardHistory.size();i++) {
					bh.addLast(boardHistory.get(i));
				}
				levelUncomplete.setBoardHistory(bh);
				levelUncomplete.setVehiclePositionHistory(vph);
				levelUncomplete.setInitialBoard(board);
				levelUncomplete.setInitialVehiclePositions(posAux);
			}
		} catch (IOException e) {
			LOGGER.error(ERROR_MESSAGE_READ, e);
		}
		LOGGER.info("The game has been loaded correctly");
		return levelUncomplete;

	}
   
}

