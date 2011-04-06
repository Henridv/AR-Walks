package com.vop.tools.data;

import java.util.ArrayList;

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
	private ArrayList<Point> walk;

	public Traject(Integer id, String name, Person person,
			ArrayList<Point> walk) {
		super();
		this.id = id;
		this.name = name;
		this.person = person;
		this.walk = walk;
	}

	public Traject(String name, Person person,
			ArrayList<Point> walk) {
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

	public ArrayList<Point> getWalk() {
		return walk;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setWalk(ArrayList<Point> walk) {
		this.walk = walk;
	}
	
	public void addPoint(Point p) {
		walk.add(p);
	}

}
