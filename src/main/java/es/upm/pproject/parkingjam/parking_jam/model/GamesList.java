package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class GamesList {

	private ArrayList <String> listGames ;
	private static final String USER_DIR = "user.dir";
	private static final Logger LOGGER = Logger.getLogger(GamesList.class);
	public GamesList(){
		if(listGames==null) listGames = new ArrayList<>();
	}
	// Adds a game to the list of games
	public boolean addGame (String game)
	{
		boolean res = false;
		if(!listGames.contains(game))
		{
			res= listGames.add(game);
			saveGames();
		}
		return res;
	}
	// removes the game from the list
	public boolean removeGame(String game) 
	{
		if(!listGames.contains(game))return false;
		boolean res=listGames.remove(game);
		saveGames();
		String rutaDirectorio = System.getProperty(USER_DIR)+"/src/main/gamesSaved/"+ game;
		File directorio = new File(rutaDirectorio);

		// DEletes the content on the directly
		if (directorio.exists()) {
			removeDirectory(directorio);
			LOGGER.info("Folder and its content had been removed correctly: "+ rutaDirectorio);
		} else {
			LOGGER.error("The folder does not exist:" +rutaDirectorio);
		}
		return res;

	}

	public ArrayList<String> getList() {
		return listGames;
	}
    // loads the list of games
	public ArrayList<String> loadList() 
	{
		ArrayList<String> list=new ArrayList<>();
		String rutaFicheroListaGames=System.getProperty(USER_DIR)+"/src/main/gamesSaved/GamesList.txt";
		File fichero = new File(rutaFicheroListaGames);

		try (FileReader fr = new FileReader(fichero); BufferedReader br = new BufferedReader(fr)) {
			String linea;
			while ((linea = br.readLine()) != null) {
				list.add(linea);
				listGames.add(linea);
			}

		}catch (IOException e) {
			LOGGER.error("Error reading the file: "+ e.getMessage());
		}

		return list;
	}
	// saves the game in the file 'GameList'
	private void saveGames()
	{
		String rutaFicheroListaGames=System.getProperty(USER_DIR)+"/src/main/gamesSaved/GamesList.txt";
		File fichero = new File(rutaFicheroListaGames);
		try (FileWriter fw = new FileWriter(fichero);
				BufferedWriter bw = new BufferedWriter(fw)) {

			for (String g : listGames){
				bw.write(g);
				bw.newLine();
			} 

		}catch (IOException e) {
			LOGGER.error("Error writing on the file: "+ e.getMessage());
		}
	}

	//Deletes the directory
	private static void removeDirectory(File directorio) {
		File[] contenido = directorio.listFiles();

		if (contenido != null) {
			for (File archivo : contenido) {
				if (archivo.isDirectory()) {
					removeDirectory(archivo);
				} else {
					Path archivoPath= archivo.toPath();
					try {
						Files.delete(archivoPath);
					} catch (IOException e) {
						LOGGER.error("Can not remove the file: "+ e.getMessage());
					}
				}
			}
		}
		Path directorioPath= directorio.toPath();
		try {
			Files.delete(directorioPath);
		} catch (IOException e) {
			LOGGER.error("Can not remove the file: "+ e.getMessage());
		}
	}


}
