package com.vop.tools.data;

public class Point {
	protected Double latitute;
	protected Double longitude;
	protected Double altitude;

	/**
	 * @param latitute
	 * @param longitude
	 * @param altitude
	 */
	public Point(Double latitute, Double longitude, Double altitude) {
		super();
		this.latitute = latitute;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	public Point() {
		this.latitute = 0.0;
		this.longitude = 0.0;
		this.altitude = 0.0;
	}

	public Double getAltitude() {
		return altitude;
	}

	public Double getLatitute() {
		return latitute;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public void setLatitute(Double latitute) {
		this.latitute = latitute;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "{" + latitute + ", " + longitude + ", " + altitude + "}";
	}

}
