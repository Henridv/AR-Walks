package com.vop.tools.data;

import android.util.Pair;

public class Location {
	private final Integer id;
	private String name;
	private Pair<Double, Double> point;
	private String date;

	/**
	 * @param id
	 * @param name
	 * @param point
	 * @param date
	 */
	public Location(Integer id, String name, Pair<Double, Double> point,
			String date) {
		super();
		this.id = id;
		this.name = name;
		this.point = point;
		this.date = date;
	}

	/**
	 * @param name
	 * @param point
	 * @param date
	 */
	public Location(String name, Pair<Double, Double> point, String date) {
		super();
		this.id = null;
		this.name = name;
		this.point = point;
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Pair<Double, Double> getPoint() {
		return point;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoint(Pair<Double, Double> point) {
		this.point = point;
	}
}
