package es.upm.pproject.parkingjam.parking_jam.controller;

import es.upm.pproject.parkingjam.parking_jam.view.view;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import es.upm.pproject.parkingjam.parking_jam.model.*;

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

	public boolean getBoard() throws FileNotFoundException, IOException {
		lvl = new Level(1);
		cellSize = 400 / lvl.getDimensionX() - 2;
		return lvl == null;
	}

	public void showLevel() {
		Map<Character, Vehicle> vehicles = lvl.getCars();
		// Pair<Integer, Integer> dimension = new Pair<Integer,
		// Integer>(lvl.getDimensionX(), lvl.getDimensionY());
		Map<Character, Pair<Integer, Integer>> mapPositions = new HashMap<>();
		for (char key : vehicles.keySet()) {
			// Pair<Integer, Integer> smallestPosition = new Pair<>(Integer.MAX_VALUE,
			// Integer.MAX_VALUE);
			mapPositions.put(key, vehicles.get(key).getBack());
		}
		mapPositions.put('@', lvl.getExit());
		// v=new view(vehicles, mapPositions,dimension,this);
	}

	private Pair<Integer, Integer> convertToGrid(int x, int y) {
		// cellSize = 400 / lvl.getDimensionX()-2;
		int row,col;
		if(x<150)row=0;
		else if(x>550)row=lvl.getDimensionX()-1;
		else row = ((x-150) / cellSize)+1;
		if(y<50)col=0;
		else if(y>450)col=lvl.getDimensionY()-1;
		else col = ((x-50) / cellSize)+1;
		return new Pair<>(row, col);
	}

	public Pair<Integer, Integer> click(Pair<Integer, Integer> click) {
		this.click = convertToGrid(click.getKey(), click.getValue());
		this.actLabel = this.click;
		Pair<Integer, Integer> res = null;
		Map<Character, Vehicle> vehicles = lvl.getCars();
		for (Vehicle v : vehicles.values()) {
			if (v.getPosition().contains(this.click))
				this.vehicleClicked = v;
		} // casillaBuff.add(label);
		if (vehicleClicked != null) {
			res = vehicleClicked.getBack();
			for (Pair<Integer, Integer> position : vehicles.get(vehicleClicked.getId()).getPosition())
				casillaBuff.add(position);
		}
		return res;
	}

	public Pair<Integer, Integer> hold(Pair<Integer, Integer> mprev, Pair<Integer, Integer> m) {
		if (vehicleClicked != null) {
			if (vehicleClicked.getDimension().getKey() == 1) {// Se mueve de arriba a abajo
				if (mprev.getValue() < m.getValue()) {// abajo
					punt++;
					if (punt % cellSize == 0) {
						Pair<Integer, Integer> newLabel = new Pair<>(vehicleClicked.getFront().getKey(),
								vehicleClicked.getFront().getValue() + (punt / cellSize) + 1);
						this.prevLabel = this.actLabel;
						this.actLabel = newLabel;

						if (casillaBuff.contains(newLabel))
							return new Pair<>(0, 1);
						else if (lvl.posicionValida(vehicleClicked, newLabel)) {
							casillaBuff.add(newLabel);
							return new Pair<>(0, 1);
						} else {punt--;
							return new Pair<>(0, 0);}
					} else if (casillaBuff.contains(actLabel))
						return new Pair<>(0, 1);
					else {punt--;
					return new Pair<>(0, 0);}
				} else {// arriba
					punt--;
					if (punt + 1 % cellSize == 0) {
						Pair<Integer, Integer> newLabel = new Pair<>(vehicleClicked.getBack().getKey(),
								vehicleClicked.getBack().getValue() + ((punt + 1) / cellSize) - 1);
						this.prevLabel = this.actLabel;
						this.actLabel = newLabel;
						if (casillaBuff.contains(newLabel))
							return new Pair<>(0, -1);
						else if (lvl.posicionValida(vehicleClicked, newLabel)) {
							casillaBuff.add(newLabel);
							return new Pair<>(0, -1);
						} else {punt++;
						return new Pair<>(0, 0);}
					} else if (casillaBuff.contains(actLabel))
						return new Pair<>(0, -1);
					
						else {punt++;
						return new Pair<>(0, 0);}
				}

			} else {// Se mueve de izq a derecha
				if (mprev.getKey() < m.getKey()) {// derecha
					punt++;
					if (punt % cellSize == 0) {
						Pair<Integer, Integer> newLabel = new Pair<>(
								vehicleClicked.getFront().getKey() + (punt / cellSize) + 1,
								vehicleClicked.getFront().getValue());
						this.prevLabel = this.actLabel;
						this.actLabel = newLabel;
						if (casillaBuff.contains(newLabel))
							return new Pair<>(1, 0);
						else if (lvl.posicionValida(vehicleClicked, newLabel)) {
							casillaBuff.add(newLabel);
							return new Pair<>(1, 0);
						} else {punt--;
						return new Pair<>(0, 0);}
					} else if (casillaBuff.contains(actLabel))
						return new Pair<>(1, 0);
					else {punt--;
					return new Pair<>(0, 0);}
				} else {// izq
					punt--;
					if (punt + 1 % cellSize == 0) {
						Pair<Integer, Integer> newLabel = new Pair<>(
								vehicleClicked.getBack().getKey() + ((punt + 1) / cellSize) - 1,
								vehicleClicked.getBack().getValue());
						this.prevLabel = this.actLabel;
						this.actLabel = newLabel;
						if (casillaBuff.contains(newLabel))
							return new Pair<>(-1, 0);
						else if (lvl.posicionValida(vehicleClicked, newLabel)) {
							casillaBuff.add(newLabel);
							return new Pair<>(-1, 0);
						} else {punt++;
						return new Pair<>(0, 0);}
					} else if (casillaBuff.contains(actLabel))
						return new Pair<>(-1, 0);
					else {punt++;
					return new Pair<>(0, 0);}
				}
			}

		}
		return new Pair<>(0, 0);
	}

	public Pair<Integer, Integer> drop(Pair<Integer, Integer> posF) {
//		if (click.equals(convertToGrid(posF.getKey(), posF.getValue())))
//			return vehicleClicked.getBack();
		// boolean res;
		Pair<Integer, Integer> mv;
		int punt2 = punt;
		
		if (punt < 0)
			punt2 = Math.abs(punt);
		if (punt2 % cellSize > cellSize / 2) {
			//if(click.equals(actLabel))return vehicleClicked.getBack();
			if (punt > 0)
				moveVehicle(vehicleClicked.getFront(), actLabel);
			else
				moveVehicle(vehicleClicked.getBack(), actLabel);
		} else {
			//if(click.equals(prevLabel))return vehicleClicked.getBack();
			if (punt > 0)
				moveVehicle(vehicleClicked.getFront(), prevLabel);
			else
				moveVehicle(vehicleClicked.getBack(), prevLabel);
			
		}
		// if(!res)return ;
		mv = vehicleClicked.getBack();
		casillaBuff.clear();
		vehicleClicked = null;
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

}
