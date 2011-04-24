package com.vop.overlays;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.vop.augumented.R;
import com.vop.augumented.R.drawable;
import com.vop.tools.VopApplication;

import android.app.Activity;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.widget.Toast;

public class TrajectRender implements Renderer {
	Activity activiteit;
	private Quad quad;
	private Quad quad_selected;


	public TrajectRender(Activity act) {
		activiteit = act;
		quad = new Quad();
		quad_selected=new Quad();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_BLEND); // enable transparency blending
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); // enable
		// transparency
		InputStream is = activiteit.getResources().openRawResource(R.drawable.markerandroid);
		InputStream is_select = activiteit.getResources().openRawResource(R.drawable.markerandroid_selected);
		quad.loadGLTexture(gl, this.activiteit,is);
		quad_selected.loadGLTexture(gl, this.activiteit,is_select);
		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Yellow Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_ALWAYS); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	public void onDrawFrame(GL10 gl) {
		try {
			// Clears the screen and depth buffer.
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			VopApplication app = (VopApplication) activiteit
					.getApplicationContext();
			float rot;
			Marker POI[];
			POI = app.getPunten();
			for (int i = 0; i < POI.length; i++) {
				gl.glLoadIdentity();
				if (app.getRotationMatrix() != null) {
					gl.glLoadMatrixf(app.getRotationMatrix(), 0);
					gl.glTranslatef(POI[i].getAfstand_x()*20f,
							POI[i].getAfstand_y()*20f, 0);
					
					if(POI[i].getAfstand_x() <0 && POI[i].getAfstand_y()>0) rot = (float) (180- (Math.toDegrees(Math.atan(POI[i].getAfstand_y()/POI[i].getAfstand_x()))));
					else if(POI[i].getAfstand_x() <0 && POI[i].getAfstand_y()<0) rot = (float) (180+ (Math.toDegrees(Math.atan(POI[i].getAfstand_y()/POI[i].getAfstand_x()))));
					else if(POI[i].getAfstand_x() >0 && POI[i].getAfstand_y()<0) rot = (float) (360- (Math.toDegrees(Math.atan(POI[i].getAfstand_y()/POI[i].getAfstand_x()))));
					else rot = (float) (Math.toDegrees(Math.atan(POI[i].getAfstand_y()/POI[i].getAfstand_x())));
					gl.glRotatef(rot, 0.0f, 0.0f, 1.0f); // Z
					//if(i != app.getDichtste_punt()) quad.draw(gl);
					//else quad_selected.draw(gl);
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
