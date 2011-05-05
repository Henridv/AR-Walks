package com.vop.tools.data;

/**
 * Represents a Location. It has a Point and extra information.
 * 
 * @author henridv
 * 
 */
public class Location extends Point {
	private final Integer id;
	private String name;
	private String description;
	private String date;
	private Integer pers_id;

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param latitute
	 * @param longitude
	 * @param altitude
	 * @param date
	 * @param persId
	 */
	public Location(Integer id, String name, String description,
			Double latitute, Double longitude, Double altitude, String date,
			Integer persId) {
		super(latitute, longitude, altitude);
		this.id = id;
		this.name = name;
		this.description = description;
		this.date = date;
		pers_id = persId;
	}

	/**
	 * @param name
	 * @param description
	 * @param latitute
	 * @param longitude
	 * @param altitude
	 * @param date
	 * @param persId
	 */
	public Location(String name, String description, Double latitute,
			Double longitude, Double altitude, String date, Integer persId) {
		super(latitute, longitude, altitude);
		this.id = null;
		this.name = name;
		this.description = description;
		this.date = date;
		pers_id = persId;
	}

	public String getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getPersId() {
		return pers_id;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersId(Integer persId) {
		pers_id = persId;
	}
}
