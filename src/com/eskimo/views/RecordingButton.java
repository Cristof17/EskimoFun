package com.eskimo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Button;

public class RecordingButton  extends Button{

	private Context context ;
	
	public RecordingButton(Context context) {
		super(context);
		this.context = context ;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint p = new Paint();
		p.setColor(Color.YELLOW);
		p.setStrokeWidth(3f);
		
		Paint text_paint = new Paint();
		text_paint.setColor(Color.GRAY);
		text_paint.setTextSize(100);
		
		canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2, canvas.getWidth()/2, p);
		canvas.drawText(getText().toString(), canvas.getWidth()/2, canvas.getHeight()/2, text_paint);
	}
	
	

}
