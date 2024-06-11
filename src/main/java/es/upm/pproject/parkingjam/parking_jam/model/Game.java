package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void guardarGame()
    {

    }

}
