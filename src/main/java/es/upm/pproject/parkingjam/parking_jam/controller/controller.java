package es.upm.pproject.parkingjam.parking_jam.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;
import es.upm.pproject.parkingjam.parking_jam.model.Vehicle;
import es.upm.pproject.parkingjam.parking_jam.view.GamesMenuView;
import es.upm.pproject.parkingjam.parking_jam.view.LevelsMenuView;
import es.upm.pproject.parkingjam.parking_jam.view.view;
import javafx.util.Pair;

public class controller {
	view v;
	Level lvl;
	Pair<Integer, Integer> click;
	Vehicle vehicleClicked;
	int punt;
	Set<Pair<Integer, Integer>> casillaBuff;
	int cellSize;
	Pair<Integer, Integer> actLabel;
	Pair<Integer, Integer> prevLabel;
	JFrame f;
	Game g;
	Menu m;
	int lvlAct;

	public controller() {
		f = new JFrame();
		f.setTitle("Parking Game");
		f.setSize(700, 700);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		f.setResizable(false);

		punt = 0;
		casillaBuff = new HashSet<Pair<Integer, Integer>>();
		actLabel = new Pair<Integer, Integer>(null, null);
		click = new Pair<Integer, Integer>(null, null);
		prevLabel = new Pair<Integer, Integer>(null, null);

		//g = new Game("Lucas"); // TODO en menu de partidas
		//LevelsMenuView lmv = new LevelsMenuView(f, g, this);
		m = new Menu();
		GamesMenuView gmv= new GamesMenuView(f,m,this);
	}

	public void showLevel(int n) throws FileNotFoundException, IOException {
		lvl = new Level(n);
		cellSize = (400 + (lvl.getDimensionX() / 2)) / (lvl.getDimensionX() - 2);
		g.setLevel(n, lvl);
		lvlAct = n;
		Map<Character, Vehicle> vehicles = lvl.getCars();
		Map<Character, Pair<Integer, Integer>> mapPositions = new HashMap<>();
		for (char key : vehicles.keySet()) {
			mapPositions.put(key, vehicles.get(key).getbackLabel());
		}
		mapPositions.put('@', lvl.getExit());
		v = new view(f, mapPositions, lvl, this, g.getGamePoints());

	}

	private Pair<Integer, Integer> convertToGrid(int x, int y) {
		int row, col;
		if (x < 150)
			row = 0;
		else if (x > 550)
			row = lvl.getDimensionX() - 1;
		else
			row = ((x - 150) / cellSize) + 1;
		System.out.println(" la x es = " + row);
		if (y < 50)
			col = 0;
		else if (y > 450)
			col = lvl.getDimensionY() - 1;
		else
			col = ((y - 50) / cellSize) + 1;
		System.out.println(" la y es = " + col);
		return new Pair<>(row, col);
	}

	public Character click(Pair<Integer, Integer> clicked) {
		click = clicked;
		Pair<Integer, Integer> click1 = convertToGrid(clicked.getKey(), clicked.getValue());
		// this.actLabel = click1;
		char res = ' ';
		Map<Character, Vehicle> vehicles = lvl.getCars();
		for (Vehicle ve : vehicles.values()) {
			if (ve.getPosition().contains(click1)) {
				this.vehicleClicked = ve;
				this.vehicleClicked.setPix(v.devuelveCoordenadas(vehicleClicked.getId()));
			}
		} // casillaBuff.add(label);
		if (vehicleClicked != null) {
			res = vehicleClicked.getId();
			for (Pair<Integer, Integer> position : vehicles.get(vehicleClicked.getId()).getPosition())
				casillaBuff.add(position);
		}
		return res;
	}

	public Pair<Integer, Integer> hold(Pair<Integer, Integer> m) {
		if (vehicleClicked != null) {
			if (vehicleClicked.getDimension().getKey() == 1) {// Se mueve de arriba a abajo

				if (click.getValue() < m.getValue()) {// abajo
					punt = m.getValue() - click.getValue();
					Pair<Integer, Integer> newLabel = new Pair<>(vehicleClicked.getfrontLabel().getKey(),
							vehicleClicked.getfrontLabel().getValue() + (punt / cellSize) + 1);
					if (newLabel.getValue() >= lvl.getDimensionY() || newLabel.getValue() < 0)
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					if (actLabel == null)
						actLabel = vehicleClicked.getfrontLabel();
					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if (!newLabel.equals(actLabel)) {
							this.prevLabel = this.actLabel;
							this.actLabel = newLabel;
							System.out.println(actLabel.toString() + prevLabel.toString());
						}
						return new Pair<>(0, vehicleClicked.getPix().getValue() + punt);
					} else {
						prevLabel = actLabel;
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					}

				} else {// arriba
					punt = m.getValue() - click.getValue();
					Pair<Integer, Integer> newLabel = new Pair<>(vehicleClicked.getbackLabel().getKey(),
							vehicleClicked.getbackLabel().getValue() + ((punt + 1) / cellSize) - 1);
					if (newLabel.getValue() >= lvl.getDimensionY() || newLabel.getValue() < 0)
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());

					if (actLabel == null)
						actLabel = vehicleClicked.getbackLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {

						if (!newLabel.equals(actLabel)) {
							this.prevLabel = this.actLabel;
							this.actLabel = newLabel;
							System.out.println(actLabel.toString() + prevLabel.toString());
						}
						return new Pair<>(0, vehicleClicked.getPix().getValue() + punt);
					} else {
						prevLabel = actLabel;
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					}

				}

			} else {// Se mueve de izq a derecha
				if (click.getKey() < m.getKey()) {// derecha
					punt = m.getKey() - click.getKey();
					Pair<Integer, Integer> newLabel = new Pair<>(
							vehicleClicked.getfrontLabel().getKey() + (punt / cellSize) + 1,
							vehicleClicked.getfrontLabel().getValue());
					if (newLabel.getKey() >= lvl.getDimensionX() || newLabel.getKey() < 0)
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);

					if (actLabel == null)
						actLabel = vehicleClicked.getfrontLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if (!newLabel.equals(actLabel)) {
							this.prevLabel = this.actLabel;
							this.actLabel = newLabel;
						}
						return new Pair<>(vehicleClicked.getPix().getKey() + punt, 0);
					} else {
						prevLabel = actLabel;
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					}

				}

				else {// izq
					punt = m.getKey() - click.getKey();
					Pair<Integer, Integer> newLabel = new Pair<>(
							vehicleClicked.getbackLabel().getKey() + ((punt + 1) / cellSize) - 1,
							vehicleClicked.getbackLabel().getValue());
					if (newLabel.getKey() >= lvl.getDimensionX() || newLabel.getKey() < 0)
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					if (actLabel == null)
						actLabel = vehicleClicked.getbackLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if (!newLabel.equals(actLabel)) {
							this.prevLabel = this.actLabel;
							this.actLabel = newLabel;
						}
						return new Pair<>(vehicleClicked.getPix().getKey() + punt, 0);
					} else {
						prevLabel = actLabel;
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					}

				}
			}

		}
		return new Pair<>(0, 0);
	}

	public Pair<Pair<Integer, Integer>, Pair<Integer, Boolean>> drop(Pair<Integer, Integer> posF) {
//		if (click.equals(convertToGrid(posF.getKey(), posF.getValue())))
//			return vehicleClicked.getBack();
		// boolean res;
		if (vehicleClicked == null)
			return null;
		Pair<Pair<Integer, Integer>, Pair<Integer, Boolean>> mv;
		int punt2 = punt;
		if (actLabel != null && prevLabel != null) {

			punt2 = Math.abs(punt);
			if (punt2 % cellSize > cellSize / 2||actLabel.equals(lvl.getExit())) {
				// if(click.equals(actLabel))return vehicleClicked.getBack();
				if (punt > 0)
					moveVehicle(vehicleClicked.getfrontLabel(), actLabel);
				else
					moveVehicle(vehicleClicked.getbackLabel(), actLabel);
			} else {
				// if(click.equals(prevLabel))return vehicleClicked.getBack();
				if (punt > 0)
					moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
				else
					moveVehicle(vehicleClicked.getbackLabel(), prevLabel);

			}
		}
		Pair<Integer, Boolean> res = new Pair<>(lvl.getLevelPoint(),
				vehicleClicked.getPosition().contains(lvl.getExit()));
		mv = new Pair<>(vehicleClicked.getbackLabel(), res);
		if (res.getValue()) {
			if (g.getLevelPoints(lvlAct) == null || g.getLevelPoints(lvlAct) >= lvl.getLevelPoint()) {
				g.actualizarGamePoints(lvlAct, lvl.getLevelPoint());
			}
			g.setLevel(lvlAct, lvl);
			int lastLevel = g.getUltimoLevelPassed();
			lastLevel = Math.max(lastLevel, lvlAct);
			g.setUltimoLevelPassed(lastLevel);
		}
		vehicleClicked.setPix(v.devuelveCoordenadas(vehicleClicked.getId()));
		casillaBuff.clear();
		this.vehicleClicked = null;
		punt = 0;
		actLabel = null;
		prevLabel = null;
		return mv;
	}

	public boolean moveVehicle(Pair<Integer, Integer> initPos, Pair<Integer, Integer> endPos) {
		char direction;
		int distance = 0;
		if (initPos.getKey().equals(endPos.getKey()) && !initPos.getValue().equals(endPos.getValue())) {
			if (initPos.getValue() < endPos.getValue()) {
				direction = 'D';
				distance = endPos.getValue() - initPos.getValue();
			} else {
				direction = 'U';
				distance = initPos.getValue() - endPos.getValue();
			}
		} else if (!initPos.getKey().equals(endPos.getKey()) && initPos.getValue().equals(endPos.getValue())) {
			if (initPos.getKey() < endPos.getKey()) {
				direction = 'R';
				distance = endPos.getKey() - initPos.getKey();
			} else {
				direction = 'L';
				distance = initPos.getKey() - endPos.getKey();
			}
		} else
			return false;// en cualquier otro caso el coche no se ha movido

		return lvl.move(vehicleClicked, direction, distance);
	}

	public void levelMenuButon() {
		LevelsMenuView lmv = new LevelsMenuView(f, g, this);
	}

	public void nextLevel() throws FileNotFoundException, IOException {
		showLevel(lvlAct + 1);
	}

	public Pair<Pair<Character, Integer>, Pair<Integer, Integer>> undo() {
		Character c = lvl.undo();
		if (c.equals(' ')) {
			return new Pair<Pair<Character, Integer>, Pair<Integer, Integer>>(
					new Pair<Character, Integer>(' ', lvl.getLevelPoint()), new Pair<Integer, Integer>(0, 0));
		}
		Pair<Pair<Character, Integer>, Pair<Integer, Integer>> res = new Pair<Pair<Character, Integer>, Pair<Integer, Integer>>(
				new Pair<Character, Integer>(c, lvl.getLevelPoint()), lvl.getCars().get(c).getbackLabel());
		return res;
	}

	public void restart() {
		lvl.reset();
		try {
			showLevel(lvlAct);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void newGame(String name) {
		Game game = new Game(name);
		m.addGame(game);
		GamesMenuView gmv = new GamesMenuView(f, m, this);
	}
	
	public void openGame(Game game) {
		this.g=game;
		LevelsMenuView lmv = new LevelsMenuView(f, game, this);
	}
	
	public void gamesMenuButton() {
		GamesMenuView gmv = new GamesMenuView(f,m,this);
	}

	
	public static void main(String[] args) {
		controller cont = new controller();
	}

}