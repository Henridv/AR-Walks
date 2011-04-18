package com.vop.tools.matrix;

class VectorDimensionError extends Error {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int size1;
	public int size2;

	public VectorDimensionError(Vector v1, Vector v2) {
		size1 = v1.size();
		size2 = v2.size();
		// {{INIT_CONTROLS
		// }}
	}
	// {{DECLARE_CONTROLS
	// }}
}