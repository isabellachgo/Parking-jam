package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.Set;
import javafx.util.Pair;

public class Vehicle {
	int id;
	boolean redCar;
	Pair<Integer,Integer> dimension;
	Set<Pair<Integer,Integer>> position;
	
	public Vehicle( int id ,boolean redCar,Pair<Integer,Integer> dimension, Set<Pair<Integer,Integer>> position) {
		this.redCar=redCar;
		this.id=id;
		this.dimension=dimension;
		this.position=position;
	}
	public void setPosition(Set<Pair<Integer,Integer>> position) {
		this.position=position;
	}
	public void setDimension(Pair<Integer,Integer> d) {
		dimension=d;
	}
	public int getId() { 
		return this.id;
		}
	public boolean getRedCar() {
		return this.redCar;
	}
	public Pair<Integer,Integer> getDimension(){
		return this.dimension;
	}
	public Set<Pair<Integer,Integer>> getPosition() {
		return this.position;
	}
}
