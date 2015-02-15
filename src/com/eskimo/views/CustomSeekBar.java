package com.eskimo.views;

import com.eskimo.eskimo.RecordActivity;
import com.eskimo.sensors.SensorsUtils;

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
	
	private float start_ratio ;
	
	
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
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(50f);
		
		Paint progress = new Paint();
		progress.setColor(whatColorShouldBe());
		progress.setStrokeWidth(50f);
		
		canvas.drawLine(0, canvas.getHeight()/2, canvas.getWidth(), canvas.getHeight()/2,paint);
		float ratio = getRatio();
		float width = getWidth();
		float width_modified =getWidth() *ratio ;
		canvas.drawLine(start_ratio * canvas.getWidth(), canvas.getHeight()/2, canvas.getWidth() * getRatio(), canvas.getHeight()/2, progress);
		
		invalidate();
	}
	
	private float getRatio(){
		int max = getMax();
		int progress = getProgress();
		return (float)(((float)getProgress()/(float)getMax()));
	}
	
	public void setStartingPoint(int position){
		float ratio = (float)position/getMax();
		float start = ratio * getMax();
		this.start_ratio = ratio;
		return ;
	}
	
	public int whatColorShouldBe(){
		if((SensorsUtils.offset >= 0 && SensorsUtils.offset <= 25) || (SensorsUtils.offset < 0 && SensorsUtils.offset >= -25)){
			return Color.GREEN;
		}else if((SensorsUtils.offset > 25 && SensorsUtils.offset <= 50) || (SensorsUtils.offset < -25 && SensorsUtils.offset >= -50)){
			return Color.YELLOW;
		}if((SensorsUtils.offset > 50 && SensorsUtils.offset <= 100) || (SensorsUtils.offset < -50 && SensorsUtils.offset >= -100)){
			return Color.RED;
		}
		
		return Color.GREEN;
	}

}
