package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.ArrayList;

public class Menu {
	ArrayList<Game> games;
	
	public Menu () {
		games= new ArrayList<>();
	}
	
	public ArrayList<Game> getGames() {
		return games;
	}
	
	public void addGame(Game g) {
		games.add(g);
	}
	
	public Integer getNumGames() {
		return games.size();
	}
	
}
