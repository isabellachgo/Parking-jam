package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GamesList {
    
    private ArrayList <String> listGames ;

    public GamesList(){
        if(listGames==null) listGames = new ArrayList<>();
    }

    public boolean addGame (String game) // usar cuando se guarde un game
    {
        boolean res = false;
        if(!listGames.contains(game))
        {
            res = true;
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
        String rutaDirectorio = System.getProperty("user.dir")+"/src/main/gamesSaved/"+ game;
        File directorio = new File(rutaDirectorio);

        // Eliminar el contenido del directorio y luego el directorio
        if (directorio.exists()) {
            removeDirectory(directorio);
            System.out.println("El directorio y su contenido se han eliminado correctamente: " + rutaDirectorio);
        } else {
            System.err.println("El directorio no existe: " + rutaDirectorio);
        }
        return res;

    }
    public ArrayList<String> getList()
    {
        return listGames;
    }
    
    public ArrayList<String> loadList() // llamar siempre al iniciar el programa
    {
        ArrayList<String> list=new ArrayList<>();
        String rutaFicheroListaGames=System.getProperty("user.dir")+"/src/main/gamesSaved/GamesList.txt";
        File fichero = new File(rutaFicheroListaGames);
        
        try (FileReader fr = new FileReader(fichero);
             BufferedReader br = new BufferedReader(fr)) {
            
            String linea;
            while ((linea = br.readLine()) != null) {

                list.add(linea);
                listGames.add(linea);
            }

        }catch (IOException e) {
        // Manejo de posibles excepciones
        System.err.println("Se produjo un error al leer el archivo: " + e.getMessage());
        }

        return list;
    }
    private void saveGames()
    {
     
        String rutaFicheroListaGames=System.getProperty("user.dir")+"/src/main/gamesSaved/GamesList.txt";
        File fichero = new File(rutaFicheroListaGames);
        System.out.println(rutaFicheroListaGames);
        // Usar try-with-resources para asegurar que los recursos se cierren automáticamente
        try (FileWriter fw = new FileWriter(fichero);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            for (String g : listGames){
                bw.write(g);
                bw.newLine();
            } 

        }catch (IOException e) {
            // Manejo de posibles excepciones
            System.err.println("Se produjo un error al escribir en el archivo: " + e.getMessage());
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
                    if (!archivo.delete()) {
                        System.err.println("No se pudo eliminar el archivo: " + archivo.getAbsolutePath());
                    }
                }
            }
        }

        // Finalmente, eliminar el directorio en sí
        if (!directorio.delete()) {
            System.err.println("No se pudo eliminar el directorio: " + directorio.getAbsolutePath());
        }
    }


}
