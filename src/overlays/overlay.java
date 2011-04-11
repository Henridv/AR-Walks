package overlays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.vop.tools.VopApplication;

public class overlay extends View {
	static private Context kontekst;

	public overlay(Context context) {
		super(context);
		VopApplication app = (VopApplication) context;
		app.putState("first", Boolean.toString(true));
		kontekst = context;
	}

	public overlay(Context context, AttributeSet attrs) {
		super(context, attrs);
		VopApplication app = (VopApplication) context;
		app.putState("first", Boolean.toString(true));
		kontekst = context;
	}

	public overlay(Context context, AttributeSet ats, int defaultStyle) {
		super(context, ats, defaultStyle);
		VopApplication app = (VopApplication) context;
		app.putState("first", Boolean.toString(true));
		kontekst = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// instellingen van de paint die gebruikt wordt voor de cirkels en tekst
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 20,
				paint);

	}
}
