package com.eskimo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import com.eskimo.data.Point;

public class KneePreviewSide extends View  {
	
	private static final String TAG = "KneePreviewSide" ;
	
	private Point knee; 
	private Point foot; 
	private Context context ;

	public KneePreviewSide(Context context) {
		super(context);
		this.context = context ;
		this.knee = new Point();
		this.foot = new Point();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
		
	}
	
	public void setKneePoint(Point knee){
		this.knee = knee ;
	}
	
	public void setFoot(Point foot ){
		this.foot = foot ;
	}

	public void logPosition(){
		Log.d(TAG, "Knee x position is "+knee.x  + " and y is "+knee.y);
		Log.d(TAG, "Foot x position is "+foot.x  + " and y is "+foot.y);
	}
	
	
}
