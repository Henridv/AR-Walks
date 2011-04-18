/**
 * Matrix.java
 *
 * Matrix class for representing and manipulating mathematical
 * m by n matrices of real (float) numbers.
 *
 * @ Authors:
 *
 * @ Wayne Witzel
 * @ University of Utah, Aspire
 * @
 * @ Copyright 1999, All Rights Reserved
 */

package com.vop.tools.matrix;

import java.io.*;

public class Matrix {
	private float[][] data;

	// create a square matrix initialized to the identity
	public Matrix(int dimension) {
		data = new float[dimension][dimension];
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				data[i][j] = ((i == j) ? 1 : 0);
		// {{INIT_CONTROLS
		// }}
	}

	// create a matrix of any dimensions initialized
	// to all zeroes.
	public Matrix(int cols, int rows) {
		data = new float[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				data[i][j] = 0;
	}

	public Matrix(Matrix M) {
		data = new float[M.rows()][M.cols()];
		copy(M);
	}

	/**
	 * Construct a matrix and fill it with the given data.
	 * 
	 * @param data
	 * @param m - rows
	 * @param n - columns
	 */
	public Matrix(float[] data, int m, int n) {
		this.data = new float[m][n];
		for (int i = 0; i<m; i++) {
			for (int j = 0; j<n; j++)
				this.data[i][j] = data[i+j];
		}
	}
	// copy each cell from M to this, throwing an error
	// if M and this don't have the same dimensions.
	public void copy(Matrix M) throws MatrixDimensionError {
		if (M.rows() != rows() || M.cols() != cols())
			throw new MatrixDimensionError(this, M);

		subcopy(M);
	}

	// copy each cell from M to this (or a sub-matrix of
	// this with the dimensions of M), assuming M has
	// less then or equal number of rows and columns as this.
	public void subcopy(Matrix M) throws MatrixDimensionError {
		for (int i = 0; i < M.rows(); i++)
			for (int j = 0; j < M.cols(); j++)
				setCell(i, j, M.cell(i, j));
	}

	public int cols() {
		return data.length;
	}

	public int rows() {
		return (data.length > 0) ? data[0].length : 0;
	}

	// return the cell at the ith row and jth column
	public float cell(int i, int j) {
		return data[j][i];
	}

	// set the value of the cell at the ith row and jth column
	public void setCell(int i, int j, float value) {
		data[j][i] = value;
	}

	/* Matrix addition */
	public Matrix add(Matrix M2) throws MatrixDimensionError {
		if (rows() != M2.rows() || cols() != M2.cols())
			throw new MatrixDimensionError(this, M2);

		Matrix result = new Matrix(rows(), cols());
		for (int i = 0; i < cols(); i++)
			for (int j = 0; j < rows(); j++)
				result.setCell(i, j, cell(i, j) + M2.cell(i, j));
		return result;
	}

	public static Matrix add(Matrix M1, Matrix M2) throws MatrixDimensionError {
		return M1.add(M2);
	}

	/* matrix subtraction */
	public Matrix subtract(Matrix M2) throws MatrixDimensionError {
		if (rows() != M2.rows() || cols() != M2.cols())
			throw new MatrixDimensionError(this, M2);

		Matrix result = new Matrix(rows(), cols());
		for (int i = 0; i < cols(); i++)
			for (int j = 0; j < rows(); j++)
				result.setCell(i, j, cell(i, j) - M2.cell(i, j));
		return result;
	}

	public static Matrix subtract(Matrix M1, Matrix M2)
			throws MatrixDimensionError {
		return M1.subtract(M2);
	}

	/* scalar multiplication */
	public Matrix mult(float a) {
		Matrix result = new Matrix(rows(), cols());
		result.copy(this);
		result.selfMult(a);
		return result;
	}

	public void selfMult(float a) {
		for (int i = 0; i < cols(); i++)
			for (int j = 0; j < rows(); j++)
				data[j][i] *= a;
	}

	/* vector multiplication */
	public static Vector mult(Matrix M1, Vector v2) throws MatrixDimensionError {
		if (M1.cols() != v2.size())
			throw new MatrixDimensionError(M1, v2);

		Vector result = new Vector(M1.rows());
		for (int j = 0; j < M1.rows(); j++) {
			float tmp = 0;
			for (int i = 0; i < M1.cols(); i++)
				tmp += M1.cell(i, j) * v2.get(i);
			result.setComponent(j, tmp);
		}
		return result;
	}

	public static Vector mult(Vector v1, Matrix M2) throws MatrixDimensionError {
		if (v1.size() != M2.rows())
			throw new MatrixDimensionError(v1, M2);

		Vector result = new Vector(M2.cols());
		for (int i = 0; i < M2.cols(); i++) {
			float tmp = 0;
			for (int j = 0; j < M2.rows(); j++)
				tmp += M2.cell(i, j) * v1.get(j);
			result.setComponent(i, tmp);
		}
		return result;
	}

	/* matrix multiplication */
	public Matrix mult(Matrix M2) throws MatrixDimensionError {
		if (cols() != M2.rows())
			throw new MatrixDimensionError(this, M2);

		Matrix result = new Matrix(M2.cols(), rows());
		for (int j = 0; j < result.rows(); j++) {
			for (int i = 0; i < result.cols(); i++) {
				float tmp = 0;
				for (int k = 0; k < cols(); k++)
					tmp += cell(k, j) * M2.cell(i, k);
				result.setCell(i, j, tmp);
			}
		}
		return result;
	}

	public static Matrix mult(Matrix M1, Matrix M2) throws MatrixDimensionError {
		return M1.mult(M2);
	}

	// return the inverse of this matrix.
	public Matrix inverse() {
		Matrix inv = new Matrix(rows());
		return inv.inverse(this);
	}

	/**
	 * Transpose the matrix
	 */
	public void transpose() {
		float a11 = data[0][0];
		float a12 = data[0][1];
		float a13 = data[0][2];

		float a21 = data[1][0];
		float a22 = data[1][1];
		float a23 = data[1][2];

		float a31 = data[2][0];
		float a32 = data[2][1];
		float a33 = data[2][2];
		
		data[0][0] = a11;
		data[0][1] = a21;
		data[0][2] = a31;
		
		data[1][0] = a12;
		data[1][1] = a22;
		data[1][2] = a32;
		
		data[2][0] = a13;
		data[2][1] = a23;
		data[2][2] = a33;
		
	}
	// sets this Matrix to the inverse of the original Matrix
	// and returns this.
	public Matrix inverse(Matrix original) {
		if (original.rows() < 1 || original.rows() != original.cols()
				|| rows() != original.rows() || rows() != cols())
			return this;
		int n = rows();
		copy(new Matrix(n)); // make identity matrix

		if (rows() == 1) {
			setCell(0, 0, 1 / original.cell(0, 0));
			return this;
		}

		Matrix b = new Matrix(original);

		for (int i = 0; i < n; i++) {
			// find pivot
			float mag = 0;
			int pivot = -1;

			for (int j = i; j < n; j++) {
				float mag2 = Math.abs(b.cell(j, i));
				if (mag2 > mag) {
					mag = mag2;
					pivot = j;
				}
			}

			// no pivot (error)
			if (pivot == -1 || mag == 0) {
				return this;
			}

			// move pivot row into position
			if (pivot != i) {
				float temp;
				for (int j = i; j < n; j++) {
					temp = b.cell(i, j);
					setCell(i, j, b.cell(pivot, j));
					b.setCell(pivot, j, temp);
				}

				for (int j = 0; j < n; j++) {
					temp = cell(i, j);
					setCell(i, j, cell(pivot, j));
					setCell(pivot, j, temp);
				}
			}

			// normalize pivot row
			mag = b.cell(i, i);
			for (int j = i; j < n; j++)
				b.setCell(i, j, b.cell(i, j) / mag);
			for (int j = 0; j < n; j++)
				setCell(i, j, cell(i, j) / mag);

			// eliminate pivot row component from other rows
			for (int k = 0; k < n; k++) {
				if (k == i)
					continue;
				float mag2 = b.cell(k, i);

				for (int j = i; j < n; j++)
					b.setCell(k, j, b.cell(k, j) - mag2 * b.cell(i, j));
				for (int j = 0; j < n; j++)
					setCell(k, j, cell(k, j) - mag2 * cell(i, j));
			}
		}
		return this;
	}

	public void print(PrintStream out) {
		String ln;
		for (int j = 0; j < rows(); j++) {
			ln = "";
			for (int i = 0; i < rows(); i++)
				ln += cell(i, j) + "\t";
			out.println(ln);
		}
	}

}
