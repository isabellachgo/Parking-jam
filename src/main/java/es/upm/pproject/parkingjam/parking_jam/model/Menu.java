package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Represents a game smenu
public class Menu {
	ArrayList<Game> games;

	public Menu() {
		games = new ArrayList<>();
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

	public void removeGame(String name) {
		boolean enc = false;
		Iterator<Game> it = games.iterator();
		while (!enc && it.hasNext()) {
			Game g = it.next();
			if (g.getName().equals(name)) {
				games.remove(g);
				enc = true;
			}
		}
	}

}
