package es.upm.pproject.parkingjam.parking_jam.controller;


import java.io.IOException;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;


import javax.swing.JFrame;

import org.apache.log4j.Logger;

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
import es.upm.pproject.parkingjam.parking_jam.view.LevelView;
import javafx.util.Pair;

public class Controller {
	private static final Logger LOGGER = Logger.getLogger(Controller.class);
	JFrame f;
	Game g;
	Menu m;
	LevelView v;
	Level lvl;
	GamesList gl;
	Pair<Integer, Integer> click;
	Vehicle vehicleClicked;
	int punt;
	int cellSize;
	Pair<Integer, Integer> actLabel;
	Pair<Integer, Integer> prevLabel;
	Pair<Integer, Integer> conflLabelF;
	Pair<Integer, Integer> conflLabelB;
	Pair<Integer, Integer> mPr;
	boolean avanza;
	Integer lvlAct;
	Integer estado =null;

	public Controller() { 
		f = new JFrame();
		f.setTitle("Parking Game");
		f.setSize(700, 700);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		f.setResizable(false);
		punt = 0;
		gl = new GamesList();
		gl.loadList();

		new StartView(f,this);

	}
	//Method that shows a level clicked on the interface, collecting it from the model and showing it in the view
	public int showLevel(int n) throws  IOException {
		if(g.getLastLevel()!=null&&g.getLastLevel().getNLevel()==n) lvl=g.getLastLevel();
		else lvl = new Level(n);
		if(lvl.getBoard()==null) {
			new LevelsMenuView(f,g,this);
			return 1;
		}
		
		estado=n;
		cellSize = (400 + (lvl.getDimensionX() / 2)) / (lvl.getDimensionX() - 2);
		lvlAct = n;
		Map<Character, Vehicle> vehicles = lvl.getCars();
		Map<Character, Pair<Integer, Integer>> mapPositions = new HashMap<>();
		for (char key : vehicles.keySet()) {
			mapPositions.put(key, vehicles.get(key).getbackLabel());
		}
		mapPositions.put('@', lvl.getExit());
		v = new LevelView(f, mapPositions, lvl, this, g.getGamePoints());
	
		LOGGER.info(" The view of the level " + n + " has been initializated");
		return 0;
	}
	//Method that returns the square on the board in which a pair of pixels are located 
	private Pair<Integer, Integer> convertToGrid(int x, int y) {
		int row;
		int col;
		if (x < 150)
			row = 0;
		else if (x > 550)
			row = lvl.getDimensionX() - 1;
		else
			row = ((x - 150) / cellSize) + 1;
		
		if (y < 50)
			col = 0;
		else if (y > 450)
			col = lvl.getDimensionY() - 1;
		else
			col = ((y - 50) / cellSize) + 1;
	
		return new Pair<>(row, col);
	}
	
	//Method that detects if a vehicle is being clicked
	public Character click(Pair<Integer, Integer> clicked) {
		click = clicked;
		mPr=clicked;
		Pair<Integer, Integer> click1 = convertToGrid(clicked.getKey(), clicked.getValue());
		char res = ' ';
		Map<Character, Vehicle> vehicles = lvl.getCars();
		for (Vehicle ve : vehicles.values()) {
			if (ve.getPosition().contains(click1)) {
				this.vehicleClicked = ve;
				this.vehicleClicked.setPix(v.devuelveCoordenadas(vehicleClicked.getId()));
			}
		} 
		if (vehicleClicked != null) 
			res = vehicleClicked.getId();
	
		return res;
	}
	
	//Method that controlls when a vehicle is being dragged
	public Pair<Integer, Integer> hold(Pair<Integer, Integer> m) {
		if (vehicleClicked != null) {
			if (vehicleClicked.getDimension().getKey() == 1) {// moves up or down
				if (mPr.getValue() < m.getValue()) {// down
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
								this.prevLabel = new Pair<>(newLabel.getKey(),newLabel.getValue()-1);
								this.actLabel = newLabel;
							
							}
							return new Pair<>(0, vehicleClicked.getPix().getValue() + punt);
						}

					} else {
						if(conflLabelF==null&&(conflLabelB==null||(!conflLabelB.getValue().equals( newLabel.getValue()) && newLabel.getValue() > conflLabelB.getValue()))) 
							conflLabelF=newLabel;
						prevLabel = actLabel;
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					}

				} else if(mPr.getValue() > m.getValue()){// up
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
								this.prevLabel = new Pair<>(newLabel.getKey(),newLabel.getValue()+1);
								this.actLabel = newLabel;
							}
							return new Pair<>(0, vehicleClicked.getPix().getValue() + punt);
						}
					} else {
						if(conflLabelB==null&&(conflLabelF==null||(!conflLabelF.getValue().equals( newLabel.getValue()) && newLabel.getValue() < conflLabelF.getValue()))) 
							conflLabelB=newLabel;
						prevLabel = actLabel;
						return new Pair<>(0, v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
					}

				}

			} else {// moves left or right
				if (mPr.getKey() < m.getKey()) {// right
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

					if (newLabel.getKey() >= lvl.getDimensionX() || newLabel.getKey() < 0)
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);

					if (actLabel == null)
						actLabel = vehicleClicked.getfrontLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if((conflLabelF==null||newLabel.getKey()<conflLabelF.getKey())
								&&(conflLabelB==null||(newLabel.getKey()-vehicleClicked.getDimension().getKey())>conflLabelB.getKey())){			
							if (!newLabel.equals(actLabel)) {
								this.prevLabel = new Pair<>(newLabel.getKey()-1,newLabel.getValue());
								this.actLabel = newLabel;
							}
							return new Pair<>(vehicleClicked.getPix().getKey() + punt, 0);
						}
					} else {
						if(conflLabelF==null&&(conflLabelB==null||(!conflLabelB.getKey().equals( newLabel.getKey()) && newLabel.getKey()>conflLabelB.getKey()))) 
							conflLabelF=newLabel;
						prevLabel = actLabel;
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					}

				}

				else if(mPr.getKey() > m.getKey()){// left
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
					if (newLabel.getKey() >= lvl.getDimensionX() || newLabel.getKey() < 0)
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					if (actLabel == null)
						actLabel = vehicleClicked.getbackLabel();

					if (lvl.posicionValida(vehicleClicked, newLabel)) {
						if((conflLabelF==null||(newLabel.getKey()+vehicleClicked.getDimension().getKey())<conflLabelF.getKey())
								&&(conflLabelB==null||newLabel.getKey()>conflLabelB.getKey())) {				
							if (!newLabel.equals(actLabel)) {
								this.prevLabel = new Pair<>(newLabel.getKey()+1,newLabel.getValue());
								this.actLabel = newLabel;
							}
							return new Pair<>(vehicleClicked.getPix().getKey() + punt, 0);
						}
					} else {
						if(conflLabelB==null&&(conflLabelF==null||(! conflLabelF.getKey().equals( newLabel.getKey()) && newLabel.getKey()<conflLabelF.getKey()))) 
							conflLabelB=newLabel;
						prevLabel = actLabel;
						return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), 0);
					}

				}
			}

		}
		 if(vehicleClicked!=null)return new Pair<>(v.devuelveCoordenadas(vehicleClicked.getId()).getKey(), v.devuelveCoordenadas(vehicleClicked.getId()).getValue());
		else return null;
	}
	
	//Method that controlls when a car that was previously clicked is released, check and update where the vehicle is released
	public Pair<Pair<Integer, Integer>, Pair<Integer, Boolean>> drop(Pair<Integer, Integer> posF) {
		if (vehicleClicked == null)
			return null;
		Pair<Pair<Integer, Integer>, Pair<Integer, Boolean>> mv;
		int punt2 = punt;
		
		if (actLabel != null && prevLabel != null) {

			punt2 = Math.abs(punt);
			if (punt2 % cellSize > cellSize / 2||actLabel.equals(lvl.getExit())) {
				if (punt>0&&avanza) {
					moveVehicle(vehicleClicked.getfrontLabel(), actLabel);
			
				}
				else if(punt>0&&!avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
					moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
					
				}
				else if(punt<0&&avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
					moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
				
				}
				else {
					moveVehicle(vehicleClicked.getbackLabel(), actLabel);
					
				}


			} else {
				if (punt>0&&avanza) {
					moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
				
				}
				else if(punt>0&&!avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getfrontLabel(), prevLabel);
					moveVehicle(vehicleClicked.getbackLabel(), actLabel);
				

				}
				else if(punt<0&&avanza) {
					if(prevLabel.equals(actLabel))moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
					moveVehicle(vehicleClicked.getfrontLabel(), actLabel);
					}

				else {
					moveVehicle(vehicleClicked.getbackLabel(), prevLabel);
				}
			}
		}
		Pair<Integer, Boolean> res = new Pair<>(lvl.getLevelPoint(),
				vehicleClicked.getPosition().contains(lvl.getExit()));
		mv = new Pair<>(vehicleClicked.getbackLabel(), res);
		if (res.getValue().booleanValue()) {
			if (g.getLevelPoints(lvlAct) == null || g.getLevelPoints(lvlAct) >= lvl.getLevelPoint()) {
				g.actualizarGamePoints(lvlAct, lvl.getLevelPoint());
			}
			g.setLevel(lvlAct, lvl);
			int lastLevel = g.getUltimoLevelPassed();
			lastLevel = Math.max(lastLevel, lvlAct);
			g.setUltimoLevelPassed(lastLevel);
			estado=null;
			if(g.getLastLevel()!=null&&g.getLastLevel().getNLevel().equals(lvlAct))g.setLastLevel(null);
		}
		vehicleClicked.setPix(v.devuelveCoordenadas(vehicleClicked.getId()));
		this.vehicleClicked = null;
		punt = 0;
		actLabel = null;
		prevLabel = null;
		conflLabelF=null;
		conflLabelB=null;
		return mv;
	}
	
	//Method that uses model class 'level' to update the position of a vehicle moved
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
		LOGGER.info("A vehicle was moved from a position to another");
		return lvl.move(vehicleClicked, direction, distance);
	}
	//Method that controlls when the level menu button is clicked in the view
	public void levelMenuButon() {
		estado=null;
		LOGGER.info("The Menu Level view has been initializated");
		new LevelsMenuView(f, g, this);
	}
	//Method that controlls when the next level button is clicked in the view
	public int nextLevel() throws  IOException {
		if(lvlAct<4) {
			int n =lvlAct + 1;
			int r = showLevel(n);
			if(r!=0) {
				g.setOkLevel(n, false);
				g.setUltimoLevelPassed(n);
				return n;
			}
			else return 0;
		}
		else {
			LOGGER.info("End Game view has been initializated, because there isnt more level in the game");
			endGame();
			return 0;
		}
	}
	//Method that controlls when the undo button is clicked in the view
	public Pair<Pair<Character, Integer>, Pair<Integer, Integer>> undo() {
		Character c = lvl.undo();
		if (c.equals(' ')) {
			return new Pair<>(
					new Pair<>(' ', lvl.getLevelPoint()), new Pair<>(0, 0));
		}
		Pair<Pair<Character, Integer>, Pair<Integer, Integer>> res = new Pair<>(
				new Pair<>(c, lvl.getLevelPoint()), lvl.getCars().get(c).getbackLabel());
		LOGGER.info("An undo of the vehicle with id: " +c+" has been done");
		return res;
	}
	
	//Method that controlls when the restart level button is clicked in the view
	public void restart() {
		lvl.reset();
		try {
			showLevel(lvlAct);
			LOGGER.info("The level has been restarted");
		} catch (IOException e) {
			LOGGER.error("The level could not be restarted", e);
			e.printStackTrace();
		}
	}
	//Method that controlls when the save game button is clicked in the view
	public void saveGame() {
		if(estado==null) {
			try {
				g.guardarGame(null);
				gl.addGame(g.getName());
				LOGGER.info("The game has been saved correctly");
			} catch (IOException e) {
				LOGGER.error("The game could not be saved", e);
				e.printStackTrace();
			}
		} else  { //level a medias
			try {
				g.guardarGame(lvl);
				gl.addGame(g.getName());
				LOGGER.info("The game has been saved correctly");
			} catch (IOException e) {
				LOGGER.error("The game could not be saved", e);
				e.printStackTrace();
			}
		}
	}
	//Method that controlls when a saved game is loaded
	public int openSavedGame(String name) {
		int r = 1;
		boolean exist=false;
		Iterator<Game> it = m.getGames().iterator();
		while(!exist && it.hasNext()) {
			if(it.next().getName().equals(name)) exist=true;
		}		
		if(!exist) {
			Game game= new Game(name);
			Level lv=game.cargarGame(name);
			game.setLastLevel(lv);
			m.addGame(game);
			new GamesMenuView(f, m, this);
			r=0;
			LOGGER.info("The game has been open correctly");
		} else {
			new SavedGamesView(f, gl.getList(), this);
			LOGGER.info("The game already exists");
		}
		return r;
	}
	public void overwrite(String name) {		
		Game game= new Game(name);
		Level lv=game.cargarGame(name);
		game.setLastLevel(lv);
		m.removeGame(name);
		m.addGame(game);
		new GamesMenuView(f, m, this);		
	}
	
	//Method that controlls when the load game button is clicked in the view
	public void openSavedGames() {
		new SavedGamesView(f, gl.getList(), this);
		LOGGER.info("The games saved  has been opened correctly");
	}
	
	//Method that controlls when the new game button is clicked in the view
	public int newGame(String name) {
		int r = 1;
		boolean exist=false;
		Iterator<Game> it = m.getGames().iterator();
		while(!exist && it.hasNext()) {
			if(it.next().getName().equals(name)) exist=true;
		}		
		if(!exist) {
			Game game = new Game(name);
			m.addGame(game);
			r=0;
		}
		new GamesMenuView(f, m, this);
		LOGGER.info("A new game has been created correctly");
		return r;
	}
	
	//Method that controlls when a game is clicked in the view
	public void openGame(Game game) {
		this.g=game;
		new LevelsMenuView(f, game, this);
		LOGGER.info("The game has been opened correctly");
	}
	
	//Method that controlls when the games menu button is clicked in the view
	public void gamesMenuButton() {
		estado=null;
		new GamesMenuView(f,m,this);
		LOGGER.info("The game Menu has been opened");
	}
	//Method that controlls when the game is started clicking start button in the view
	public void gamesMenu(Menu menu) {
		this.m=menu;
		new GamesMenuView(f,m,this);
		LOGGER.info("The game Menu has been opened");
	}
	
	//Method that controlls when the user completes the game
	public void endGame() {
		new EndGameView(f,g,this);
		LOGGER.info("The En game view has been opened");
	}
	
	//Main method that start the game
	public static void main(String[] args) {
		new Controller();
	}

}