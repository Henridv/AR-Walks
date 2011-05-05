package com.vop.tools;

import java.util.EventListener;

/**
 * A listener interface for location updates
 * 
 * @author henridv
 * 
 */
public interface LocationListener extends EventListener {

	/**
	 * This method is called when the location is updated
	 */
	void locationUpdated();
}
