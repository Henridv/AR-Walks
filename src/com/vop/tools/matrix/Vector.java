/**
 * Vector.java
 *
 * Vector class for representing and manipulating mathematical
 * vector's of real (float) numbers.
 *
 * @ Authors:
 *
 * @ Wayne Witzel
 * @ University of Utah, Aspire
 * @
 * @ Copyright 1999, All Rights Reserved
 */

package com.vop.tools.matrix;

// Vector class with some common functions that
// apply to vectors of any dimension.
public class Vector {

	private float[] data;

	// create a vector of the given dimension initialized
	// to the ZERO vector in that dimensionality.
	public Vector(int size) {
		data = new float[size];
		for (int i = 0; i < size; i++)
			data[i] = 0;
		// {{INIT_CONTROLS
		// }}
	}

	/**
	 * Construct a vector with given data
	 * 
	 * @param data
	 */
	public Vector(float[] data) {
		this.data = data;
	}

	public Vector assign(Vector v2) {
		if (size() != v2.size())
			throw new VectorDimensionError(this, v2);
		for (int i = 0; i < size(); i++)
			setComponent(i, v2.get(i));
		return this;
	}

	// return the size of the vector
	public int size() {
		return data.length;
	}

	// return component i
	public float get(int i) {
		return data[i];
	}

	// set component i
	public void setComponent(int i, float value) {
		data[i] = value;
	}

	/* vector addition */
	public Vector add(Vector v2) throws VectorDimensionError {
		if (size() != v2.size())
			throw new VectorDimensionError(this, v2);

		Vector result = new Vector(size());
		for (int i = 0; i < size(); i++)
			result.setComponent(i, get(i) + v2.get(i));
		return result;
	}

	public static Vector add(Vector v1, Vector v2) {
		return v1.add(v2);
	}

	/* vector subtraction */
	public Vector subtract(Vector v2) throws VectorDimensionError {
		if (size() != v2.size())
			throw new VectorDimensionError(this, v2);

		Vector result = new Vector(size());
		for (int i = 0; i < size(); i++)
			result.setComponent(i, get(i) - v2.get(i));
		return result;
	}

	public static Vector subtract(Vector v1, Vector v2) {
		return v1.subtract(v2);
	}

	/* scalar multiplication */
	public Vector mult(float a) {
		Vector result = new Vector(size());
		for (int i = 0; i < size(); i++)
			result.setComponent(i, get(i) * a);
		return result;
	}

	public static Vector mult(Vector v, float a) {
		return v.mult(a);
	}

	/* vector dot product (component-wise multiplication) */
	public float dotProduct(Vector v2) throws VectorDimensionError {
		if (size() != v2.size())
			throw new VectorDimensionError(this, v2);

		float result = 0;
		for (int i = 0; i < size(); i++)
			result += get(i) * v2.get(i);
		return result;
	}

	public static float dotProduct(Vector v1, Vector v2) {
		return v1.dotProduct(v2);
	}

	/*
	 * implemented functions that make use of one or more of the abstract
	 * functions
	 */

	public float squared() {
		return this.dotProduct(this);
	}

	public float length() {
		return (float) Math.pow(this.dotProduct(this), .5);
	}

	public static Vector normalized(Vector v) {
		return v.mult(1 / v.length());
	}

	public static Vector rescaled(Vector v, float newLength) {
		return v.mult(newLength / v.length());
	}

	// Reflect an incident vector about a surface whose
	// normal vector is provided (normal should point in
	// direction of reflection and should NOT be zero).
	// If the incident vector is pointing away from the
	// surface (in similar direction as the normal) then
	// this will simple return the original incident vector.
	public Vector reflected(Vector normal) {
		float i_dot_n = this.dotProduct(normal);
		if (i_dot_n > 0)
			return this;
		else {
			// reflected = incident + 2*incident*cos(angle)*(normal/|normal|)
			float scale = -2 * i_dot_n / normal.squared();
			return this.add(normal.mult(scale));
		}
	}

	// returns the t such that x + v * t = R.
	// If there is no such t, then Double.NEGATIVE_INFINITY
	// is returned.
	public float radiusImpactParam(Vector v, float R) {
		// |d| = |(x0, y0)| = R
		// x = x0 + v.x * t
		// y = y0 + v.y * t
		// (v*v) * t^2 + 2*(d*v) * t + d*d - R^2 = 0
		// a * t^2 + b * t + c = 0
		float a = v.squared();
		float b = 2 * this.dotProduct(v);

		if (b >= 0)
			return -1; // obj is moving away so there won't be a collision

		float c = this.squared() - R * R;
		float det = b * b - 4 * a * c;
		if (det > 0) {
			float t = (float) ((-b - Math.pow(det, .5)) / (2 * a));
			return (t < 0) ? -t : t;
		} else
			return Float.NEGATIVE_INFINITY; // doesn't collide - return
		// negative infinity
	}
	// {{DECLARE_CONTROLS
	// }}
}