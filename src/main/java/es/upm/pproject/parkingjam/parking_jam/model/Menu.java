package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	ArrayList<Game> games;
	
	public Menu () {
		games= new ArrayList<>();
	}
	
	public List<Game> getGames() {
		return games;
	}
	
	public void addGame(Game g) {
		games.add(g);
	}
	
	public Integer getNumGames() {
		return games.size();
	}
	
}
