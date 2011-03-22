package com.vop.augumented;

import android.location.Location;

public class Marker {
	private double lat;
	private double lng;
	private double alt;
	private boolean zichtbaarheid;
	private double afstand_marker;

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

	public Marker(String naam, String com,double longitude, double latitude, double altitude,
			double lat_loc, double lng_loc, double alt_loc, double roll) {
		titel = naam;
		info = com;
		lat = latitude;
		lng = longitude;
		alt = altitude;
		bereken_zichtbaarheid(lat_loc, lng_loc, alt_loc, roll);
	}

	public void bereken_zichtbaarheid(double lat_loc, double lng_loc,
			double alt_loc, double roll) {
		double hoek;
		horizontale_positie = -1;
		double x = Math.abs(lng - lng_loc);
		double y = Math.abs(lat - lat_loc);
		float afstand_tot_punt[] = new float[3];
		Location.distanceBetween(lat, lng, lat_loc, lng_loc, afstand_tot_punt);
		afstand_marker = afstand_tot_punt[0];
		if (afstand_tot_punt[0] < afstand) {

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
				boolean boven;
				double hoogte_verschil = (alt_loc - alt);
				if (hoogte_verschil < 0)
					boven = true;
				else
					boven = false;
				hoogte_verschil = Math.abs(hoogte_verschil);
				double hoek_vert = Math.atan(hoogte_verschil
						/ afstand_tot_punt[0])
						/ (2 * PI) * 360;
				if (hoek_vert < angle_of_view_vertical) {
					verticale_positie = hoek_vert / angle_of_view_vertical;
					if (boven)
						verticale_positie *= -1;
					zichtbaarheid = true;
				}
			}
		}
	}
}
