package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.Set;
import javafx.util.Pair;

public class Vehicle {
	Character id;
	boolean redCar;
	Pair<Integer,Integer> dimension;
	Set<Pair<Integer,Integer>> position;
	Pair<Integer,Integer> frontLabel;
	Pair<Integer,Integer> backLabel;
	Pair<Integer,Integer> pix;	
	
	public Vehicle(Character id ,boolean redCar,Pair<Integer,Integer> dimension, Set<Pair<Integer,Integer>> position) {
		this.redCar=redCar;
		this.id=id;
		this.dimension=dimension;
		this.position=position;
		this.backLabel = new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.frontLabel = new Pair<>(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (Pair<Integer, Integer> p : position) {
            if (p.getKey() < backLabel.getKey() || 
                (p.getKey().equals(backLabel.getKey()) && p.getValue() < backLabel.getValue())) {
                this.backLabel = p;
            }
            if (p.getKey() > frontLabel.getKey() || 
                (p.getKey().equals(frontLabel.getKey()) && p.getValue() > frontLabel.getValue())) {
                this.frontLabel = p;
            }
        }
		
	}
	// Set the position of a car
	public void setPosition(Set<Pair<Integer,Integer>> position) {
		this.position=position;
		this.backLabel = new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.frontLabel = new Pair<>(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (Pair<Integer, Integer> p : position) {
            if (p.getKey() < backLabel.getKey() || 
                (p.getKey().equals(backLabel.getKey()) && p.getValue() < backLabel.getValue())) {
                this.backLabel = p;
            }

            if (p.getKey() > frontLabel.getKey() || 
                (p.getKey().equals(frontLabel.getKey()) && p.getValue() > frontLabel.getValue())) {
                this.frontLabel = p;
            }
        }
	}
	// Getters and Setters 
	public void setDimension(Pair<Integer,Integer> d) {
		dimension=d;
	}
	public Character getId() { 
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
	public Pair<Integer,Integer> getfrontLabel(){
		return this.frontLabel;
	}
	public Pair<Integer,Integer> getbackLabel(){
		return this.backLabel;
	}
	
	public void setPix(Pair<Integer, Integer> p){
		this.pix=p;
	}
	public Pair<Integer,Integer> getPix(){
		return this.pix;
	}
	
}
