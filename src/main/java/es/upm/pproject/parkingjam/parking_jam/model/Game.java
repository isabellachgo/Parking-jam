package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
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

    public Game( String name)
    {
        gameName=name;
        gamePoints=0;
        ultimoLevelPassed = 0;
        if (listaLevels==null) listaLevels=new HashMap<>();
        if (listaPoints==null) listaPoints=new HashMap<>();
    }

    public Integer getGamePoints ()
    {
        return gamePoints;
    }

    public Integer getLevelPoints(int id)
    {
        return listaPoints.get(id);
    }

    public void actualizarGamePoints (int id , int points)
    {
        listaPoints.put(id, points);
        gamePoints=0;
        for (HashMap.Entry<Integer, Integer> entry : listaPoints.entrySet())             
                gamePoints += entry.getValue();   
            
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

    public  void guardarGame(int id, Level level) throws IOException
    {

        String rutaFichero = System.getProperty("user.dir");  // Cambia esto a la ruta deseada
        String rutaDirectorio=rutaFichero+"/src/main/gamesSaved/"+ gameName ;
        
        File directorio = new File(rutaDirectorio);
        directorio.mkdirs();

        // Crear una instancia de File con la ruta especificada
        rutaFichero= rutaDirectorio+ File.separator+ "gamePoints.txt";
        File fichero = new File(rutaFichero);
        System.out.println(rutaFichero);
        // Usar try-with-resources para asegurar que los recursos se cierren automáticamente
        try (FileWriter fw = new FileWriter(fichero);
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Escribir en el fichero
            for (HashMap.Entry<Integer, Level> entry : listaLevels.entrySet())             
           { 
                bw.write(entry.getKey()+" : " + entry.getValue().getGamePoints());
                bw.newLine();  // Salto de línea
           }
            System.out.println("El archivo se ha creado y se ha escrito correctamente en: " + rutaFichero);

        } catch (IOException e) {
            // Manejo de posibles excepciones
            System.err.println("Se produjo un error al escribir en el archivo: " + e.getMessage());
        }

        if ( level!=null)
        {
            rutaFichero= rutaDirectorio+ File.separator+ level.getTitle()+ ".txt";
            File ficheroTablero = new File(rutaFichero);
            System.out.println(rutaFichero);
            // Usar try-with-resources para asegurar que los recursos se cierren automáticamente
            try (FileWriter fw = new FileWriter(ficheroTablero);
                BufferedWriter bw = new BufferedWriter(fw)) {
                    Character [][] board = level.getBoard();
                      // Escribir en el fichero
                    bw.write(level.getTitle());
                    bw.newLine();
                    bw.write(level.getDimensionX()+ " " + level.getDimensionY());
                    bw.newLine();
                    for ( int i = 0 ; i<level.getDimensionY(); i++)
                    {
                        for ( int j =0 ; j< level.getDimensionX();j++)
                        {
                            if(board[j][i]== null) bw.write(" ");
                            else bw.write(board[j][i]);
                        }
                        bw.newLine();
                    }
                System.out.println("El archivo se ha creado y se ha escrito correctamente en: " + rutaFichero);

            } catch (IOException e) {
                // Manejo de posibles excepciones
                System.err.println("Se produjo un error al escribir en el archivo: " + e.getMessage());
            }
            
        }
    }

    public void cargarGame ( String name)
    {
        String rutaDirectorioBase = System.getProperty("user.dir");
        String rutaFichero = rutaDirectorioBase + File.separator + "src" + File.separator + "main" + File.separator + "gamesSaved" + File.separator + name + File.separator + "gamePoints.txt";

        // Crear una instancia de File con la ruta del archivo especificada
        File fichero = new File(rutaFichero);

        // Usar try-with-resources para asegurar que los recursos se cierren automáticamente
        try (FileReader fr = new FileReader(fichero);
             BufferedReader br = new BufferedReader(fr)) {

            // Leer el fichero línea por línea
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en dos partes usando ":" como separador
                String[] partes = linea.split(" : ");
                if (partes.length == 2) {
                    String antesDeDosPuntos = partes[0].trim();
                    String despuesDeDosPuntos = partes[1].trim();

                    // Imprimir las partes
                    System.out.println("id = " + antesDeDosPuntos);
                    System.out.println("points = " + despuesDeDosPuntos);
                    int id = Integer.parseInt(antesDeDosPuntos) ;
                    int points = Integer.parseInt(despuesDeDosPuntos);
                    actualizarGamePoints(id, points);
                } else {
                    System.err.println("Línea mal formateada: " + linea);
                }
            }

        } catch (IOException e) {
            // Manejo de posibles excepciones
            System.err.println("Se produjo un error al leer el archivo: " + e.getMessage());
        }
    }
/*
    public static void main (String[] args) throws FileNotFoundException, IOException
        {
            Game g =new Game("Rachilin3");
            
            Level lv1 = new Level(1);
            lv1.setGamePoints(33);
            Level lv2 = new Level(2);
            lv2.setGamePoints(456);
            Level lv3 = new Level(3);
            lv3.setGamePoints(43);

            
            g.setLevel(1, lv1);
            g.setLevel(2, lv2);
            g.setLevel(3,lv3);
            g.guardarGame(1, new Level(1));

            
            //Level lv1 = new Level(1);
           
            g.guardarGame(1, lv1);
            g.cargarGame("Rachilin3");
            System.out.println(g.getGamePoints());
        }
 */
}

