package com.vop.tools.data;

public class Location {
	private final Integer id;
	private String name;
	private String description;
	private Double latitute;
	private Double longitude;
	private Double altitude;
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
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.latitute = latitute;
		this.longitude = longitude;
		this.altitude = altitude;
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
		super();
		this.id = null;
		this.name = name;
		this.description = description;
		this.latitute = latitute;
		this.longitude = longitude;
		this.altitude = altitude;
		this.date = date;
		pers_id = persId;
	}

	public Double getAltitude() {
		return altitude;
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

	public Double getLatitute() {
		return latitute;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getName() {
		return name;
	}

	public Integer getPersId() {
		return pers_id;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLatitute(Double latitute) {
		this.latitute = latitute;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPersId(Integer persId) {
		pers_id = persId;
	}
}
