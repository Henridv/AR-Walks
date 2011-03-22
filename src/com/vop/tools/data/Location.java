package com.vop.tools.data;

public class Location {
	private final Integer id;
	private String name;
	private String description;
	private Double latitute;
	private Double longitude;
	private Double altitude;
	private String date;

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param latitute
	 * @param longitude
	 * @param altitude
	 * @param date
	 */
	public Location(Integer id, String name, String description,
			Double latitute, Double longitude, Double altitude, String date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.latitute = latitute;
		this.longitude = longitude;
		this.altitude = altitude;
		this.date = date;
	}

	/**
	 * @param name
	 * @param description
	 * @param latitute
	 * @param longitude
	 * @param altitude
	 * @param date
	 */
	public Location(String name, String description, Double latitute,
			Double longitude, Double altitude, String date) {
		super();
		this.id = null;
		this.name = name;
		this.description = description;
		this.latitute = latitute;
		this.longitude = longitude;
		this.altitude = altitude;
		this.date = date;
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
}
