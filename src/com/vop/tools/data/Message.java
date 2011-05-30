package com.vop.tools.data;

import java.io.File;

public class Message extends Location {

	private String description;

	/**
	 * Create a Message with all fields
	 * 
	 * @param id
	 * @param name
	 * @param latitute
	 * @param longitude
	 * @param altitude
	 * @param date
	 * @param persId
	 * @param img
	 */
	public Message(Integer id, String name, Double latitute, Double longitude,
			Double altitude, String date, Integer persId, File img,
			String description) {
		super(id, name, latitute, longitude, altitude, date, persId, img);

		this.description = description;
	}

	/**
	 * Create a new Message with an image
	 * 
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 * @param persId
	 * @param img
	 */
	public Message(String name, Double latitude, Double longitude,
			Double altitude, Integer persId, File img, String description) {
		super(name, latitude, longitude, altitude, persId, img);

		this.description = description;
	}

	/**
	 * Create a new image without an image
	 * 
	 * @param name
	 * @param latitute
	 * @param longitude
	 * @param altitude
	 * @param persId
	 */
	public Message(String name, Double latitute, Double longitude,
			Double altitude, Integer persId, String description) {
		super(name, latitute, longitude, altitude, persId);

		this.description = description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
