package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.HashMap;

public class Game {

    private Integer gamePoints;
    private String gameName;
    private Integer ultimoLevelPassed;
    private HashMap <Integer,Level> listaLevels;

    public Game( String name)
    {
        gameName=name;
        gamePoints=0;
        ultimoLevelPassed = 0;
        if (listaLevels==null) listaLevels=new HashMap<>();
    }

    public Integer getGamePoints ()
    {
        return gamePoints;
    }

    public void setGamePoints (int newPoints)
    {
        gamePoints = newPoints;
    }

    public void sumarGamePoints (int cantidad)
    {
        gamePoints = gamePoints + cantidad;
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

}
