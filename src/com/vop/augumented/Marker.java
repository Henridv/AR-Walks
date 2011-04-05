package com.vop.augumented;

import com.vop.tools.VopApplication;

import android.content.Context;
import android.location.Location;

public class Marker {
	private double lat;
	private double lng;
	private double alt;
	private boolean zichtbaarheid;
	private double afstand_marker;
	private float afstand_x;
	private float afstand_y;
	private Boolean binnen_afstand;

	public Boolean getBinnen_afstand() {
		return binnen_afstand;
	}

	public float getAfstand_x() {
		return afstand_x;
	}

	public float getAfstand_y() {
		return afstand_y;
	}

	public double getAfstand_marker() {
		return afstand_marker;
	}

	public boolean isZichtbaarheid() {
		return zichtbaarheid;
	}

	private double verticale_positie;

	public double getVerticale_positie() {
		return verticale_positie;
	}

	public void setVerticale_positie(double verticale_positie) {
		this.verticale_positie = verticale_positie;
	}

	public double getHorizontale_positie() {
		return horizontale_positie;
	}

	public void setHorizontale_positie(double horizontale_positie) {
		this.horizontale_positie = horizontale_positie;
	}

	private double horizontale_positie;
	private double PI = 4.0 * Math.atan(1.0);
	private String titel;

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	private String info;

	double angle_of_view_horizontal = 54.4 / 2;
	double angle_of_view_vertical = 37.8 / 2;
	private static double afstand;

	public static void setAfstand(double afstand) {
		Marker.afstand = afstand;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public Marker(String naam, String com, double longitude, double latitude,
			double altitude, double lat_loc, double lng_loc, double alt_loc,
			double roll) {
		titel = naam;
		info = com;
		lat = latitude;
		lng = longitude;
		alt = altitude;
		bereken_zichtbaarheid(lat_loc, lng_loc, alt_loc, roll);
	}

	public Marker(String naam, String com, double longitude, double latitude,
			double altitude, Context context) {
		titel = naam;
		info = com;
		lat = latitude;
		lng = longitude;
		alt = altitude;

		VopApplication app = (VopApplication) context;
		double lng_loc = app.getLng();
		double lat_loc = app.getLat();

		float afstand_tot_punt[] = new float[3];
		Location.distanceBetween(lat, lng, lat, lng_loc, afstand_tot_punt);
		afstand_x = afstand_tot_punt[0];
		Location.distanceBetween(lat, lng_loc, lat_loc, lng_loc,
				afstand_tot_punt);
		afstand_y = afstand_tot_punt[0];
		Location.distanceBetween(lat, lng, lat_loc, lng_loc, afstand_tot_punt);
		afstand_marker = afstand_tot_punt[0];
		afstand_x = (float) (afstand_x * 1.0 / afstand_marker);
		afstand_y = (float) (afstand_y * 1.0 / afstand_marker);
		if (lat - lat_loc < 0)
			afstand_y *= -1;
		if (lng - lng_loc < 0)
			afstand_x *= -1;
	}

	public void bereken_zichtbaarheid(double lat_loc, double lng_loc,
			double alt_loc, double roll) {
		double hoek;
		zichtbaarheid = false;
		horizontale_positie = -1;
		double x = Math.abs(lng - lng_loc);
		double y = Math.abs(lat - lat_loc);

		float afstand_tot_punt[] = new float[3];
		Location.distanceBetween(lat, lng, lat, lng_loc, afstand_tot_punt);
		afstand_x = afstand_tot_punt[0];
		Location.distanceBetween(lat, lng_loc, lat_loc, lng_loc,
				afstand_tot_punt);
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
		if (afstand_tot_punt[0] < afstand) {
			binnen_afstand = true;
			if (lat - lat_loc > 0) {
				if (lng - lng_loc > 0) {
					hoek = Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						horizontale_positie = (hoek - roll)
								/ (angle_of_view_horizontal);
					else if (Math.abs(hoek + (360 - roll)) < angle_of_view_horizontal)
						horizontale_positie = Math.abs(hoek + (360 - roll))
								/ (angle_of_view_horizontal);
				} else if (lng - lng_loc < 0) {
					hoek = 360 - Math.atan(x / y) / (2 * PI) * 360;
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						horizontale_positie = (hoek - roll)
								/ (angle_of_view_horizontal);
					else {
						hoek = Math.atan(x / y) / (2 * PI) * 360;
						if (Math.abs(hoek + roll) < angle_of_view_horizontal)
							horizontale_positie = -Math.abs(hoek + roll)
									/ (angle_of_view_horizontal / 2);
					}
				} else if (roll < angle_of_view_horizontal)
					horizontale_positie = -roll / (angle_of_view_horizontal);
				else if (Math.abs(360 - roll) < angle_of_view_horizontal)
					horizontale_positie = Math.abs(roll - 360)
							/ (angle_of_view_horizontal);
			} else if (lat - lat_loc < 0) {
				if (lng - lng_loc > 0) {
					hoek = 90 + Math.abs(Math.atan(y / x) / (2 * PI) * 360);
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						horizontale_positie = (hoek - roll)
								/ (angle_of_view_horizontal);
				} else if (lng - lng_loc < 0) {
					hoek = 180 + ((Math.atan(x / y) / (2 * PI) * 360));
					if (Math.abs(hoek - roll) < angle_of_view_horizontal)
						horizontale_positie = (hoek - roll)
								/ (angle_of_view_horizontal);
				} else if (Math.abs(roll - 180) < angle_of_view_horizontal)
					horizontale_positie = (180 - roll)
							/ (angle_of_view_horizontal);
			} else if (lng - lng_loc > 0) {
				if (Math.abs(roll - 90) < angle_of_view_horizontal)
					horizontale_positie = (90 - roll)
							/ (angle_of_view_horizontal);
			} else if (lng - lng_loc < 0) {
				if (Math.abs(roll - 270) < angle_of_view_horizontal)
					horizontale_positie = (270 - roll)
							/ (angle_of_view_horizontal);
			}
			if (horizontale_positie != -1) {
				zichtbaarheid = true;
				verticale_positie = 0;
			}
		}
	}
}
