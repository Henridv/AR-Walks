package com.vop.tools.matrix;

class MatrixDimensionError extends Error {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Dimension A;
	public Dimension B;

	public MatrixDimensionError(Matrix A, Matrix B) {
		this.A = new Dimension(A.cols(), A.rows());
		this.B = new Dimension(B.cols(), B.rows());
		// {{INIT_CONTROLS
		// }}
	}

	public MatrixDimensionError(Matrix A, Vector B) {
		this.A = new Dimension(A.cols(), A.rows());
		this.B = new Dimension(1, B.size());
	}

	public MatrixDimensionError(Vector A, Matrix B) {
		this.A = new Dimension(A.size(), 1);
		this.B = new Dimension(B.cols(), B.rows());
	}
	// {{DECLARE_CONTROLS
	// }}
}