/**
 * Copyright 2010 Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vop.augumented;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.vop.tools.VopApplication;

import android.app.Activity;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.widget.Toast;

public class OpenGLRenderer implements Renderer {
	private Cube cube;
	Activity activiteit;
	private Quad quad;
	private float xrot;


	public OpenGLRenderer(Activity act) {
		// cube = new Cube(1, 1, 1);
		activiteit = act;
		quad = new Quad();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_BLEND); // enable transparency blending
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); // enable
		// transparency
		quad.loadGLTexture(gl, this.activiteit);

		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Yellow Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	public void onDrawFrame(GL10 gl) {
		try {
			// Clears the screen and depth buffer.
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			VopApplication app = (VopApplication) activiteit
					.getApplicationContext();

			Marker POI[];
			POI = app.getPunten();
			for (int i = 0; i < POI.length; i++) {
				gl.glLoadIdentity();
				if (app.getValues() != null) {
					// gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //Z
					gl.glLoadMatrixf(app.getValues(), 0);
					// gl.glTranslatef(0.0f, 0.0f, -1.0f); // Move 5 units into
					// the
					// screen
					// gl.glScalef(100f, 100f, 100f);
					gl.glTranslatef(POI[i].getAfstand_x() * 10f,
							POI[i].getAfstand_y() * 10f, 0);
					//gl.glRotatef(app.getRoll(), 0.0f, 0.0f, 1.0f); // Z
					quad.draw(gl);
				}
			}
		} catch (Exception e) {

		}

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity(); // Reset The Modelview Matrix
	}
}
