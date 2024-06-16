package es.upm.pproject.parkingjam.parking_jam.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.GamesList;
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.model.Menu;
import es.upm.pproject.parkingjam.parking_jam.model.Vehicle;
import es.upm.pproject.parkingjam.parking_jam.view.EndGameView;
import es.upm.pproject.parkingjam.parking_jam.view.GamesMenuView;
import es.upm.pproject.parkingjam.parking_jam.view.LevelsMenuView;
import es.upm.pproject.parkingjam.parking_jam.view.SavedGamesView;
import es.upm.pproject.parkingjam.parking_jam.view.StartView;
import es.upm.pproject.parkingjam.parking_jam.view.view;
import javafx.util.Pair;

public class controller {
	JFrame f;
	Game g;
	Menu m;
	view v;
	Level lvl;
	GamesList gl;
	Pair<Integer, Integer> click;
	Vehicle vehicleClicked;
	int punt;
	//Set<Pair<Integer, Integer>> casillaBuff;
	int cellSize;
	Pair<Integer, Integer> actLabel;
	Pair<Integer, Integer> prevLabel;
	Pair<Integer, Integer> conflLabelF;
	Pair<Integer, Integer> conflLabelB;
	Pair<Integer, Integer> mPr;
	boolean avanza;
	Integer lvlAct;
	Integer estado =null;

	public controller() { 
		f = new JFrame();
		f.setTitle("Parking Game");
		f.setSize(700, 700);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		f.setResizable(false);

		punt = 0;
		
		//cargar partidas guardadas
		gl = new GamesList();
		gl.loadList();
		
		//casillaBuff = new HashSet<Pair<Integer, Integer>>();
		

		//g = new Game("Lucas"); // TODO en menu de partidas
		StartView sv= new StartView(f,this);

	}

	public void showLevel(int n) throws FileNotFoundException, IOException {
		if(g.getLastLevel()!=null) lvl=g.getLastLevel();
		else lvl = new Level(n);
		estado=n;
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
		mPr=clicked;
		Pair<Integer, Integer> click1 = convertToGrid(clicked.getKey(), clicked.getValue());
		//if(actLabel.getKey()==null)System.out.println("aaaaaaaaaaaaaaaaaaaaa");
		//this.actLabel = new Pair(0,0);
		char res = ' ';
		Map<Character, Vehicle> vehicles = lvl.getCars();
		for (Vehicle ve : vehicles.values()) {
			if (ve.getPosition().contains(click1)) {
				this.vehicleClicked = ve;
				this.vehicleClicked.setPix(v.devuelveCoordenadas(vehicleClicked.getId()));
			}
		} // casillaBuff.add(label);
		if (vehicleClicked != null) 
			res = vehicleClicked.getId();
			//for (Pair<Integer, Integer> position : vehicles.get(vehicleClicked.getId()).getPosition())
				//casillaBuff.add(position);
		//}
		return res;
	}

	public Pair<Integer, Integer> hold(Pair<Integer, Integer> m) {
		if (vehicleClicked != null) {
			if (vehicleClicked.getDimension().getKey() == 1) {// Se mueve de arriba a abajo
				if (mPr.getValue() < m.getValue()) {// abajo
					mPr=m;
					avanza=true;
					punt = m.getValue() - click.getValue();
					Pair<Integer, Integer> newLabel;
					if(punt>0) {			
						newLabel = new Pair<>(vehicleClicked.getbackLabel().getKey(),
							vehicleClicked.getfrontLabel().getValue() + ((punt) / cellSize) +1);}
					else {
						newLabel = new Pair<>(vehicleClicked.getbackLabel().getKey(),
								vehicleClicked.getfrontLabel().getValue() + (punt/cellSize));
					}
				
					if (newLabel.getValue() >= lvl.getDimensionY() || newLabel.getValue() < 0)
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					if (actLabel==null)
						actLabel = vehicleClicked.getfrontLabel();
					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if((conflLabelF==null||newLabel.getValue()<conflLabelF.getValue())
							&&(conflLabelB==null||(newLabel.getValue()-vehicleClicked.getDimension().getValue())>conflLabelB.getValue())){					
						if (!newLabel.equals(actLabel)) {
							this.prevLabel = new Pair(newLabel.getKey(),newLabel.getValue()-1);Integer a=(punt/cellSize);
							this.actLabel = newLabel;//System.out.println("Bajo "+newLabel.toString()+" "+conflLabelB.toString());
							//if(conflLabelF!=null)
							System.out.println("bajo "+actLabel.toString()+" "+prevLabel.toString() /*+" conf "+ conflLabelF.toString()*/);
						}
						return new Pair<>(0, vehicleClicked.getPix().getValue() + punt);
						}
						
					} else {
						if(conflLabelF==null&&(conflLabelB==null||(conflLabelB.getValue()!=newLabel.getValue()&&newLabel.getValue()>conflLabelB.getValue()))) 
							conflLabelF=newLabel;
						prevLabel = actLabel;
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					}

				} else if(mPr.getValue() > m.getValue()){// arriba
					mPr=m;
					avanza=false;
					punt = m.getValue() - click.getValue();
					Pair<Integer, Integer> newLabel;
					if(punt<0) {			
						newLabel = new Pair<>(vehicleClicked.getbackLabel().getKey(),
							vehicleClicked.getbackLabel().getValue() + ((punt) / cellSize) -1);}
					else {
						newLabel = new Pair<>(vehicleClicked.getbackLabel().getKey(),
								vehicleClicked.getbackLabel().getValue() + ((punt) / cellSize) );
					
					}
					if (newLabel.getValue() >= lvl.getDimensionY() || newLabel.getValue() < 0)
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());

					if (actLabel==null)
						actLabel = vehicleClicked.getbackLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if((conflLabelF==null||(newLabel.getValue()+vehicleClicked.getDimension().getValue())<conflLabelF.getValue())
							&&(conflLabelB==null||newLabel.getValue()>conflLabelB.getValue())) {				
						if (!newLabel.equals(actLabel)) {
							this.prevLabel = new Pair(newLabel.getKey(),newLabel.getValue()+1);
							this.actLabel = newLabel;if(actLabel!=null)System.out.println("Subo "+actLabel.toString()+" "+prevLabel.toString());
							
						}
						return new Pair<>(0, vehicleClicked.getPix().getValue() + punt);
						}
					} else {
						if(conflLabelB==null&&(conflLabelF==null||(conflLabelF.getValue()!=newLabel.getValue()&&newLabel.getValue()<conflLabelF.getValue()))) 
							conflLabelB=newLabel;
						prevLabel = actLabel;
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					}

				}

			} else {// Se mueve de izq a derecha
				if (mPr.getKey() < m.getKey()) {// derecha
					mPr=m;
					avanza=true;
					punt = m.getKey() - click.getKey();
					Pair<Integer, Integer> newLabel;
					if(punt>0) {			
						newLabel = new Pair<>(
								vehicleClicked.getfrontLabel().getKey() + (punt / cellSize) + 1,
								vehicleClicked.getfrontLabel().getValue());}
					else {
						newLabel = new Pair<>(vehicleClicked.getfrontLabel().getKey()+((punt) / cellSize),
								vehicleClicked.getfrontLabel().getValue());
					
					}
					System.out.println(newLabel.toString());

					if (newLabel.getKey() >= lvl.getDimensionX() || newLabel.getKey() < 0)
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);

					if (actLabel == null)
						actLabel = vehicleClicked.getfrontLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if((conflLabelF==null||newLabel.getKey()<conflLabelF.getKey())
								&&(conflLabelB==null||(newLabel.getKey()-vehicleClicked.getDimension().getKey())>conflLabelB.getKey())){			
						if (!newLabel.equals(actLabel)) {
							this.prevLabel = new Pair(newLabel.getKey()-1,newLabel.getValue());
							this.actLabel = newLabel;if(actLabel!=null)System.out.println("Dch "+actLabel.toString()+" "+prevLabel.toString());
						}
						return new Pair<>(vehicleClicked.getPix().getKey() + punt, 0);
						}
					} else {
						if(conflLabelF==null&&(conflLabelB==null||(conflLabelB.getKey()!=newLabel.getKey()&&newLabel.getKey()>conflLabelB.getKey()))) 
							conflLabelF=newLabel;
						prevLabel = actLabel;
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					}

				}

				else if(mPr.getKey() > m.getKey()){// izq
					mPr=m;
					avanza=false;
					punt = m.getKey() - click.getKey();
					Pair<Integer, Integer> newLabel;
					if(punt<0) {			
						newLabel = new Pair<>(
								vehicleClicked.getbackLabel().getKey() + (punt / cellSize) -1,
								vehicleClicked.getbackLabel().getValue());}
					else {
						newLabel = new Pair<>(vehicleClicked.getbackLabel().getKey()+((punt) / cellSize),
								vehicleClicked.getbackLabel().getValue());
					
					}
					System.out.println(newLabel.toString());
					if (newLabel.getKey() >= lvl.getDimensionX() || newLabel.getKey() < 0)
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					if (actLabel == null)
						actLabel = vehicleClicked.getbackLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if((conflLabelF==null||(newLabel.getKey()+vehicleClicked.getDimension().getKey())<conflLabelF.getKey())
							&&(conflLabelB==null||newLabel.getKey()>conflLabelB.getKey())) {				
						if (!newLabel.equals(actLabel)) {
							this.prevLabel = new Pair(newLabel.getKey()+1,newLabel.getValue());
							this.actLabel = newLabel;System.out.println("Izq "+actLabel.toString()+" "+prevLabel.toString());
						}
						return new Pair<>(vehicleClicked.getPix().getKey() + punt, 0);
						}
					} else {
						if(conflLabelB==null&&(conflLabelF==null||(conflLabelF.getKey()!=newLabel.getKey()&&newLabel.getKey()<conflLabelF.getKey()))) 
							conflLabelB=newLabel;
						prevLabel = actLabel;
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					}

				}
			}

		}
		return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
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
				if (punt>0&&avanza) {
					moveVehicle(vehicleClicked.getfrontLabel(), actLabel);
				System.out.println("1 "+punt2%cellSize + " "+actLabel.toString());
				}
				else if(punt>0&&!avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
					moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
				System.out.println("2 "+punt2%cellSize + " "+prevLabel.toString());
				}
				else if(punt<0&&avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
					moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
				System.out.println("3 "+punt2%cellSize + " "+prevLabel.toString());
				}
				else {
					moveVehicle(vehicleClicked.getbackLabel(), actLabel);
				System.out.println("4 "+punt2%cellSize + " "+actLabel.toString());
				}


			} else {
				// if(click.equals(prevLabel))return vehicleClicked.getBack();
				if (punt>0&&avanza) {
					moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
				System.out.println("5 "+punt2%cellSize + " "+prevLabel.toString());
				}
				else if(punt>0&&!avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
					moveVehicle(vehicleClicked.getbackLabel(), actLabel);
				System.out.println("6 "+punt2%cellSize + " "+actLabel.toString());

				}
				else if(punt<0&&avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
					moveVehicle(vehicleClicked.getfrontLabel(), actLabel);
				System.out.println("7 "+punt2%cellSize + " "+actLabel.toString());}

				else {
					moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
				System.out.println("8 "+punt2%cellSize + " "+prevLabel.toString());}
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
			estado=null;
			lvl=null;
		}
		vehicleClicked.setPix(v.devuelveCoordenadas(vehicleClicked.getId()));
		//casillaBuff.clear();
		this.vehicleClicked = null;
		punt = 0;
		actLabel = null;
		prevLabel = null;
		conflLabelF=null;
		conflLabelB=null;
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
		estado=null;
		LevelsMenuView lmv = new LevelsMenuView(f, g, this);
	}

	public void nextLevel() throws FileNotFoundException, IOException {
	 if(lvlAct<4)	showLevel(lvlAct + 1);
	 else endGame();
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
	
	public void saveGame() {
		if(estado==null) {
			try {
				g.guardarGame(null);
				gl.addGame(g.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(estado!=null) { //level a medias
			try {
				g.guardarGame(lvl);
				gl.addGame(g.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void openSavedGame(String name) {
		
		Game game= new Game(name);
		Level lv=game.cargarGame(name);
		game.setLastLevel(lv);
		m.addGame(game);
		GamesMenuView gmv = new GamesMenuView(f, m, this);
	}
	
	public void openSavedGames() {
		SavedGamesView sgv = new SavedGamesView(f, gl.getList(), this);
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
		estado=null;
		GamesMenuView gmv = new GamesMenuView(f,m,this);
	}
	public void gamesMenu(Menu menu) {
		this.m=menu;
		GamesMenuView gm = new GamesMenuView(f,m,this);
	}
	public void endGame() {
		EndGameView ev= new EndGameView(f,m,g,this);
	}

	public static void main(String[] args) {
		controller cont = new controller();
	}

}