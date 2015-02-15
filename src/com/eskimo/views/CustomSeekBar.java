package com.eskimo.views;

import android.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class CustomSeekBar extends SeekBar{

	private Context context ;
	
	public CustomSeekBar(Context context) {
		super(context);
		this.context = context ;
	}
	
	public CustomSeekBar(Context c, AttributeSet set , int a){
		super(c, set,a);
	}
		
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		Paint paint =  new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(25f);
		
		Paint progress = new Paint();
		progress.setColor(Color.RED);
		progress.setStrokeWidth(25f);
		
		canvas.drawLine(0, canvas.getHeight()/2, canvas.getWidth(), canvas.getHeight()/2,paint);
		float ratio = getRatio();
		float width = getWidth();
		float width_modified =getWidth() *ratio ;
		canvas.drawLine(0, canvas.getHeight()/2 + 20, canvas.getWidth() * getRatio(), canvas.getHeight()/2 + 20, progress);
		
		invalidate();
	}
	
	private float getRatio(){
		int max = getMax();
		int progress = getProgress();
		return (float)(((float)getProgress()/(float)getMax()));
	}
	

}
