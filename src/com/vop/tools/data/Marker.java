package com.vop.tools.data;

import android.location.Location;

public class Marker implements Comparable<Marker> {
	public static double angle_of_view_horizontal = 54.4 / 2;
	public static double angle_of_view_vertical = 37.8 / 2;
	
	private double lat;
	private double lng;
	private double alt;
	private String title;
	private String info;
	private float distance, max_distance, min_distance;
	private float angle;

	@Override
	public int compareTo(Marker marker2) {
		if (this.distance > marker2.getDistance())
			return 1;
		else if (this.distance < marker2.getDistance())
			return -1;
		else
			return 0;
	}

	public double getAlt() {
		return alt;
	}

	public String getInfo() {
		return info;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public String getTitle() {
		return title;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Construct a Marker object.
	 * 
	 * @param l
	 *            Point of interest
	 * @param lat
	 *            current latitude
	 * @param lng
	 *            current longitude
	 * @param alt
	 *            current altitude
	 */
	public Marker(com.vop.tools.data.Location l, float lat, float lng, float alt) {
		this.title = l.getName();
		this.lat = l.getLatitute();
		this.lng = l.getLongitude();
		max_distance = 5000f;
		min_distance = 1f;

		update(lat, lng);
	}

	public float getDistance() {
		return distance;
	}

	public float getRotation() {
		return angle;
	}

	/**
	 * Updates the distance and bearing
	 * 
	 * @param lat
	 *            - current latitude
	 * @param lng
	 *            - current longitude
	 */
	public void update(float lat, float lng) {
		float[] results = new float[3];
		Location.distanceBetween(lat, lng, this.lat, this.lng, results);
		this.distance = results[0];
		this.angle = ((results[1] + results[2]) / 2f + 360f) % 360f;
	}

	public boolean isVisible(float azimuth) {
		if (distance > max_distance || distance < min_distance)
			return false;
		else
			return Math.abs(angle - azimuth) < angle_of_view_horizontal;
	}

	@Override
	public String toString() {
		return title + " (" + distance + "m, " + angle + "°)";
	}
}
