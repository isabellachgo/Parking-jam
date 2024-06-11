package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private Integer gamePoints;
    private String gameName;
    private Integer ultimoLevelPassed;
    private HashMap <Integer,Level> listaLevels;
    private ArrayList <Integer> listaPoints;

    public Game( String name)
    {
        gameName=name;
        gamePoints=0;
        ultimoLevelPassed = 0;
        if (listaLevels==null) listaLevels=new HashMap<>();
        if (listaPoints==null) listaPoints=new ArrayList<Integer>();
    }

    public Integer getGamePoints ()
    {
        return gamePoints;
    }

    public void actualizarGamePoints (int id , int points)
    {
        listaPoints.add(id, points);
        gamePoints=0;
        for(int i=0; i< listaPoints.size();i++)
        {
            if(listaPoints.get(i)!=null) gamePoints=gamePoints+ listaPoints.get(i);
        }
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
