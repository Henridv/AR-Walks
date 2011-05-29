package com.vop.map.overlays;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.vop.tools.data.Location;

public class CustomOverlayItem extends OverlayItem {
	private Location l;
	public CustomOverlayItem(GeoPoint point,Location l) {
		super(point, l.getName(), "");
		this.l=l;
	}
	public Location getLocation(){
		return l;
	}

}
