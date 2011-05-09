package com.vop.ar.overlays;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

import com.vop.tools.VopApplication;
import com.vop.tools.data.Point;
import com.vop.tools.data.Traject;

/**
 * An OpenGL Renderer that draws a track on the screen
 * 
 * @author henridv
 * 
 */
public class TrackRenderer extends GLSurfaceView implements Renderer {
	private Placemarker placemarker;
	private VopApplication app;
	private Traject track;

	public TrackRenderer(Context appContext) {
		super(appContext);
		app = (VopApplication) appContext.getApplicationContext();
		placemarker = new Placemarker();
		track = app.getTraject();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		int center = -1;
		float diff_min = 360f;
		// Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity(); // Reset The Current Modelview Matrix

		float[] rotMatrix = app.getRotationMatrix();
		List<Point> POI = track.getWalk();
		// if (rotMatrix != null) {
		// for (int i = 0; i < POI.size(); i++) {
		// // float alt_diff = (float) (app.getAlt() - POI[i].getAlt());
		// if (!POI.get(i).isVisible(app.getAzimuth()))
		// continue;
		//
		// if (Math.abs(POI.get(i).getRotation() - app.getAzimuth()) < diff_min)
		// {
		// diff_min = Math.abs(POI.get(i).getRotation() - app.getAzimuth());
		// center = i;
		// }
		// gl.glLoadMatrixf(rotMatrix, 0);
		// gl.glRotatef(-POI.get(i).getRotation(), 0, 0, 1.0f);
		// gl.glTranslatef(0, POI.get(i).getDistance() / 5000f * 10f + 10f, 0f);
		// placemarker.draw(gl);
		// }
		// }
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		placemarker.loadGLTexture(gl, this.getContext());
		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

	}
}
