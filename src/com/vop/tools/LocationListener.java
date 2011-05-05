package com.vop.tools;

import java.util.EventListener;

public interface LocationListener extends EventListener {

	/**
	 * This method is called when the location is updated
	 */
	void locationUpdated();
}
