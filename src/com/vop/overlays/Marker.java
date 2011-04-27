package com.vop.overlays;

import android.content.Context;
import android.location.Location;

import com.vop.tools.VopApplication;

public class Marker implements Comparable<Marker> {
	private double lat;
	private double lng;
	private double alt;
	private boolean zichtbaarheid;
	private double afstand_marker;
	private float afstand_x;
	private float afstand_y;
	private Boolean binnen_afstand;
	private double verticale_positie;
	private double horizontale_positie;
	private double PI = 4.0 * Math.atan(1.0);
	private String title;
	private String info;
	double angle_of_view_horizontal = 54.4 / 2;
	double angle_of_view_vertical = 37.8 / 2;
	private float distance, max_distance, min_distance;
	private float angle;

	public Marker(String title, String info, double longitude, double latitude,
			double altitude, Context context) {
		this.title = title;
		this.info = info;
		lat = latitude;
		lng = longitude;
		alt = altitude;
		VopApplication app = (VopApplication) context;
		double lat_loc = app.getLat();
		double lng_loc = app.getLng();
		double alt_loc = app.getAlt();

		bereken_zichtbaarheid(lat_loc, lng_loc, alt_loc, app.getAzimuth());
	}

	public Marker(String title, String info, double longitude, double latitude,
			double altitude, double lat_loc, double lng_loc, double alt_loc,
			double azimuth) {
		this.title = title;
		this.info = info;
		lat = latitude;
		lng = longitude;
		alt = altitude;
		bereken_zichtbaarheid(lat_loc, lng_loc, alt_loc, azimuth);
	}

	public void bereken_zichtbaarheid(double lat_loc, double lng_loc,
			double alt_loc, double azimuth) {
		double hoek;
		zichtbaarheid = false;
		horizontale_positie = -1;
		double x = Math.abs(lng - lng_loc);
		double y = Math.abs(lat - lat_loc);

		float afstand_tot_punt[] = new float[3];
		Location.distanceBetween(lat, lng, lat, lng_loc, afstand_tot_punt);
		afstand_x = afstand_tot_punt[0];
		Location.distanceBetween(lat, lng_loc, lat_loc, lng_loc, afstand_tot_punt);
		afstand_y = afstand_tot_punt[0];
		Location.distanceBetween(lat, lng, lat_loc, lng_loc, afstand_tot_punt);
		afstand_marker = afstand_tot_punt[0];
		afstand_x = (float) (afstand_x * 1.0 / afstand_marker);
		afstand_y = (float) (afstand_y * 1.0 / afstand_marker);
		if (lat - lat_loc < 0)
			afstand_y *= -1;
		if (lng - lng_loc < 0)
			afstand_x *= -1;
		binnen_afstand = false;
		if (afstand_tot_punt[0] < 10000000) {
			binnen_afstand = true;
			if (lat - lat_loc > 0) {
				if (lng - lng_loc > 0) {
					hoek = Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - azimuth) < angle_of_view_horizontal)
						horizontale_positie = (hoek - azimuth)
								/ (angle_of_view_horizontal);
					else if (Math.abs(hoek + (360 - azimuth)) < angle_of_view_horizontal)
						horizontale_positie = Math.abs(hoek + (360 - azimuth))
								/ (angle_of_view_horizontal);
				} else if (lng - lng_loc < 0) {
					hoek = 360 - Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - azimuth) < angle_of_view_horizontal)
						horizontale_positie = (hoek - azimuth)
								/ (angle_of_view_horizontal);
					else {
						hoek = Math.atan(x / y) / (2 * PI) * 360;
						if (Math.abs(hoek + azimuth) < angle_of_view_horizontal)
							horizontale_positie = -Math.abs(hoek + azimuth)
									/ (angle_of_view_horizontal / 2);
					}
				} else if (azimuth < angle_of_view_horizontal)
					horizontale_positie = -azimuth / (angle_of_view_horizontal);
				else if (Math.abs(360 - azimuth) < angle_of_view_horizontal)
					horizontale_positie = Math.abs(azimuth - 360)
							/ (angle_of_view_horizontal);
			} else if (lat - lat_loc < 0) {
				if (lng - lng_loc > 0) {
					hoek = 90 + Math.abs(Math.atan(y / x) / (2 * PI) * 360);
					if (Math.abs(hoek - azimuth) < angle_of_view_horizontal)
						horizontale_positie = (hoek - azimuth)
								/ (angle_of_view_horizontal);
				} else if (lng - lng_loc < 0) {
					hoek = 180 + ((Math.atan(x / y) / (2 * PI) * 360));
					if (Math.abs(hoek - azimuth) < angle_of_view_horizontal)
						horizontale_positie = (hoek - azimuth)
								/ (angle_of_view_horizontal);
				} else if (Math.abs(azimuth - 180) < angle_of_view_horizontal)
					horizontale_positie = (180 - azimuth)
							/ (angle_of_view_horizontal);
			} else if (lng - lng_loc > 0) {
				if (Math.abs(azimuth - 90) < angle_of_view_horizontal)
					horizontale_positie = (90 - azimuth)
							/ (angle_of_view_horizontal);
			} else if (lng - lng_loc < 0) {
				if (Math.abs(azimuth - 270) < angle_of_view_horizontal)
					horizontale_positie = (270 - azimuth)
							/ (angle_of_view_horizontal);
			}
			if (horizontale_positie != -1) {
				zichtbaarheid = true;
				verticale_positie = 0;
			}
		}
	}

	@Override
	public int compareTo(Marker another) {
		if (this.afstand_marker > another.getAfstand_marker())
			return 1;
		else if (this.afstand_marker > another.getAfstand_marker())
			return -1;
		else
			return 0;
	}

	public double getAfstand_marker() {
		return afstand_marker;
	}

	public float getAfstand_x() {
		return afstand_x;
	}

	public float getAfstand_y() {
		return afstand_y;
	}

	public double getAlt() {
		return alt;
	}

	public Boolean getBinnen_afstand() {
		return binnen_afstand;
	}

	public double getHorizontale_positie() {
		return horizontale_positie;
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

	public double getVerticale_positie() {
		return verticale_positie;
	}

	public boolean isZichtbaarheid() {
		return zichtbaarheid;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public void setHorizontale_positie(double horizontale_positie) {
		this.horizontale_positie = horizontale_positie;
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

	public void setVerticale_positie(double verticale_positie) {
		this.verticale_positie = verticale_positie;
	}

	/**
	 * @deprecated
	 */
	public static float getAfstand() {
		return 0;
	}
	
	/**
	 * @deprecated
	 */
	public static void setAfstand(float a) {
		return;
	}
	
	/**
	 * Deel Henri
	 */

	public Marker(com.vop.tools.data.Location l, float lat, float lng, float alt) {
		this.title = l.getName();
		this.info = l.getDescription();
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
	 * @param lat - current latitude
	 * @param lng - current longitude
	 */
	public void update(float lat, float lng) {
		float[] results = new float[3];
		Location.distanceBetween(lat, lng, this.lat, this.lng, results);
		this.distance = results[0];
		this.angle = ((results[1] + results[2]) / 2f + 360f)%360f;
	}

	public boolean isVisible(float azimuth) {
		if (distance > max_distance || distance < min_distance) return false;
		else return true;
//		else return Math.abs(angle - azimuth) < angle_of_view_horizontal;
	}
	
	@Override
	public String toString() {
		return title + " (" + distance + ", " + angle + ")";
	}
}
