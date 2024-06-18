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

	public boolean addGame (String game) // usar cuando se guarde un game
	{
		boolean res = false;
		if(!listGames.contains(game))
		{
			res= listGames.add(game);
			saveGames();
		}
		return res;
	}

	public boolean removeGame(String game) // usar cuando se elimine un game guardado
	{
		if(!listGames.contains(game))return false;
		boolean res=listGames.remove(game);
		saveGames();
		String rutaDirectorio = System.getProperty(USER_DIR)+"/src/main/gamesSaved/"+ game;
		File directorio = new File(rutaDirectorio);

		// Eliminar el contenido del directorio y luego el directorio
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

	public ArrayList<String> loadList() // llamar siempre al iniciar el programa
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
			// Manejo de posibles excepciones
			LOGGER.error("Error reading the file: "+ e.getMessage());
		}

		return list;
	}

	private void saveGames()
	{

		String rutaFicheroListaGames=System.getProperty(USER_DIR)+"/src/main/gamesSaved/GamesList.txt";
		File fichero = new File(rutaFicheroListaGames);

		// Usar try-with-resources para asegurar que los recursos se cierren automáticamente
		try (FileWriter fw = new FileWriter(fichero);
				BufferedWriter bw = new BufferedWriter(fw)) {

			for (String g : listGames){
				bw.write(g);
				bw.newLine();
			} 

		}catch (IOException e) {
			// Manejo de posibles excepciones
			LOGGER.error("Error writing on the file: "+ e.getMessage());
		}
	}

	private static void removeDirectory(File directorio) {
		// Obtener todos los archivos y subdirectorios en el directorio
		File[] contenido = directorio.listFiles();

		if (contenido != null) {
			for (File archivo : contenido) {
				// Si el archivo es un directorio, eliminar su contenido recursivamente
				if (archivo.isDirectory()) {
					removeDirectory(archivo);
				} else {
					// Eliminar archivo
					Path archivoPath= archivo.toPath();
					try {
						Files.delete(archivoPath);
					} catch (IOException e) {
						LOGGER.error("Can not remove the file: "+ e.getMessage());
					}
				}
			}
		}

		// Finalmente, eliminar el directorio en sí
		Path directorioPath= directorio.toPath();
		try {
			Files.delete(directorioPath);
		} catch (IOException e) {
			LOGGER.error("Can not remove the file: "+ e.getMessage());
		}
	}


}
