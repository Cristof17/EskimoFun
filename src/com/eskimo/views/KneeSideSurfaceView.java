package com.eskimo.views;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.eskimo.data.Point;

@SuppressLint("WrongCall")
public class KneeSideSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private Context context ;
	private Point knee;  
	private SurfaceHolder holder ;
	Boolean running ;
	
	public KneeSideSurfaceView(Context context) {
		super(context);
		this.context = context ;
		holder = getHolder();
		holder.addCallback(this);
	}

	public static final String TAG = "KNEESIDESURFACEVIEW";
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Knee Side Surface created ");
		
				
		UpdateThread thread = new UpdateThread(); 
		thread.start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Knee Side Surface changed ");
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Knee Side Surface destroyed ");
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Random r =new Random();
		
		canvas.drawARGB(255, 255, 0, 0);
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.YELLOW);
		paint.setStrokeWidth(255);
		
		canvas.drawPoint(knee.x, knee.y, paint);
		canvas.drawLine(knee.x, knee.y, r.nextInt() % 400, r.nextInt() %400, paint);
		
	}
	
	public void setKneePoint(Point knee){
		this.knee = knee ;
	}
	
	public void logPosition(){
		Log.d(TAG, "Knee x position is "+knee.x  + " and y is "+knee.y);
	}

	public void setRunning(Boolean value){
		this.running = value ;
	}
	
	
	private class UpdateThread extends Thread{
	
		@Override
		public void run() {
			while(running){
				Log.d(TAG,"Knee side thread Running");
				
				Canvas c = holder.lockCanvas(null);
				onDraw(c);
				holder.unlockCanvasAndPost(c);

				try{
					Thread.sleep(1500);
				}catch(InterruptedException e){
					Log.e(TAG, "Error at knee side update thread ");
				}
			}
			super.run();
		}
		
		
		
	}
	
}
