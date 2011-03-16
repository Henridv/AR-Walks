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

	private final Integer id;
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

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Person getPerson() {
		return person;
	}

	public ArrayList<Pair<Double, Double>> getWalk() {
		return walk;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setWalk(ArrayList<Pair<Double, Double>> walk) {
		this.walk = walk;
	}

}
