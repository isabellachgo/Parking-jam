package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.Set;
import javafx.util.Pair;

public class Vehicle {
	Character id;
	boolean redCar;
	Pair<Integer,Integer> dimension;
	Set<Pair<Integer,Integer>> position;
	Pair<Integer,Integer> front;
	Pair<Integer,Integer> back;
	
	public Vehicle(Character id ,boolean redCar,Pair<Integer,Integer> dimension, Set<Pair<Integer,Integer>> position) {
		this.redCar=redCar;
		this.id=id;
		this.dimension=dimension;
		setPosition(position);
		
	}
	public void setPosition(Set<Pair<Integer,Integer>> position) {
		this.position=position;
		this.back = new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.front = new Pair<>(Integer.MIN_VALUE, Integer.MIN_VALUE);

        // Buscar el par menor y el par mayor
        for (Pair<Integer, Integer> p : position) {
            // Comparar para encontrar el menor par
            if (p.getKey() < back.getKey() || 
                (p.getKey().equals(back.getKey()) && p.getValue() < back.getValue())) {
                this.back = p;
            }
            // Comparar para encontrar el mayor par
            if (p.getKey() > front.getKey() || 
                (p.getKey().equals(front.getKey()) && p.getValue() > front.getValue())) {
                this.front = p;
            }
        }
	}
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
	public Pair<Integer,Integer> getFront(){
		return this.front;
	}
	public Pair<Integer,Integer> getBack(){
		return this.back;
	}
}
