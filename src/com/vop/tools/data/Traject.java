package com.vop.tools.data;

import java.util.ArrayList;

import android.util.Pair;

/**
 * Represents a Traject
 * 
 * @author henri
 * 
 */
public class Traject {

	private Integer id;
	private String name;
	private Person person;
	private ArrayList<Pair<Double, Double>> walk;

	public Traject(Integer id, String name, Person person,
			ArrayList<Pair<Double, Double>> walk) {
		super();
		this.id = id;
		this.name = name;
		this.person = person;
		this.walk = walk;
	}

	public Traject(String name, Person person,
			ArrayList<Pair<Double, Double>> walk) {
		super();
		this.id = null;
		this.name = name;
		this.person = person;
		this.walk = walk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public ArrayList<Pair<Double, Double>> getWalk() {
		return walk;
	}

	public void setWalk(ArrayList<Pair<Double, Double>> walk) {
		this.walk = walk;
	}
	/**
	 * saves traject data to database
	 * @return true if successful
	 */
	public boolean save() {
		if (id == null) ; //make new user
		else ; // edit existing user
		return true;
	}
	
	/**
	 * load traject data from database
	 * @param id
	 * @return
	 */
	public boolean load(int id) {
		this.id = id;
		return true;
	}
}
